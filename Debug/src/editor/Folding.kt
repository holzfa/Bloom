package io.holzfa.bloom.debug

import DefaultTokenTypes
import Token
import TokenType
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.input.OffsetMapping
import androidx.compose.ui.text.input.TransformedText
import androidx.compose.ui.text.withStyle
import builtin.tokentypes.KeywordTokenType
import builtin.tokentypes.LiteralTokenType
import builtin.tokentypes.SymbolTokenType
import builtin.tokentypes.VuraTokenType

/** A multi-line bracket pair, `open` and `close` are the offsets of the bracket characters */
data class FoldRegion(val open: Int, val close: Int)

/** The part of the source that is replaced by the placeholder while a region is folded, `end` is exclusive */
data class Hidden(val start: Int, val end: Int)

class GutterLine(val number: Int, val region: FoldRegion?, val folded: Boolean)

private const val PLACEHOLDER = " … "

private val closerOf: Map<TokenType, TokenType> = mapOf(
    SymbolTokenType.BraceOpen to SymbolTokenType.BraceClose,
    SymbolTokenType.ParenthesisOpen to SymbolTokenType.ParenthesisClose,
    SymbolTokenType.SquareOpen to SymbolTokenType.SquareClose
)
private val closers: Set<TokenType> = closerOf.values.toSet()

private val declarationKeywords: Set<TokenType> = setOf(
    KeywordTokenType.Function,
    KeywordTokenType.Struct,
    KeywordTokenType.Class,
    KeywordTokenType.Trait,
    KeywordTokenType.Enum
)

fun findFoldRegions(source: String, tokens: List<Token>, lineStarts: IntArray): List<FoldRegion> {
    val sig = tokens.filter {
        it.type != VuraTokenType.Whitespace
                && it.type != VuraTokenType.Comment
                && it.type != VuraTokenType.Documentation
                && it.type != DefaultTokenTypes.Eof
    }

    val stack = ArrayList<Int>()
    val regions = mutableListOf<FoldRegion>()

    for ((i, token) in sig.withIndex()) {
        if (token.type in closerOf) {
            stack.add(i)
        } else if (token.type in closers) {
            val idx = stack.indexOfLast { closerOf[sig[it].type] == token.type }
            if (idx < 0) continue

            val openIdx = stack[idx]
            while (stack.size > idx) stack.removeAt(stack.lastIndex)
            if (!isFoldable(sig, openIdx)) continue

            val open = offsetOf(lineStarts, sig[openIdx].pos)
            val close = offsetOf(lineStarts, token.pos)
            if (open < 0 || close <= open) continue

            if (source.indexOf('\n', open) in open until close) regions.add(FoldRegion(open, close))
        }
    }

    return regions.sortedBy { it.open }
}

private fun isFoldable(sig: List<Token>, openIdx: Int): Boolean = when (sig[openIdx].type) {
    SymbolTokenType.BraceOpen -> true
    SymbolTokenType.SquareOpen -> !isIndex(sig, openIdx)
    else -> !isDeclarationParameters(sig, openIdx)
}

private fun isIndex(sig: List<Token>, openIdx: Int): Boolean {
    val prev = sig.getOrNull(openIdx - 1)?.type
    return prev == VuraTokenType.Identifier
            || prev == SymbolTokenType.ParenthesisClose
            || prev == SymbolTokenType.SquareClose
            || prev == KeywordTokenType.Self
            || prev == KeywordTokenType.It
            || prev is LiteralTokenType
}

/** Function parameters and primary constructors: `fun name<T>(`, `struct Name<T>(`, `fun +(` ... */
private fun isDeclarationParameters(sig: List<Token>, openIdx: Int): Boolean {
    var i = openIdx - 1

    if (sig.getOrNull(i)?.type == SymbolTokenType.GreaterThan) {
        var depth = 0
        while (i >= 0) {
            val type = sig[i].type
            if (type == SymbolTokenType.GreaterThan) {
                depth++
            } else if (type == SymbolTokenType.LessThan) {
                depth--
                if (depth == 0) break
            }
            i--
        }
        i--
    }

    val name = sig.getOrNull(i)?.type ?: return false
    if (name == VuraTokenType.Identifier) {
        while (i >= 2 && sig[i - 1].type == SymbolTokenType.Dot && sig[i - 2].type == VuraTokenType.Identifier) i -= 2
    } else if (name !is SymbolTokenType) {
        return false
    }
    i--

    val keyword = sig.getOrNull(i)?.type ?: return false
    return keyword in declarationKeywords
}

/** Turns the folded regions into hidden ranges, regions nested in an already folded one are ignored */
fun hiddenRanges(regions: List<FoldRegion>, folded: Set<Int>): List<Hidden> {
    val result = mutableListOf<Hidden>()
    var lastEnd = -1

    for (region in regions) {
        if (region.open !in folded || region.open < lastEnd) continue
        result.add(Hidden(region.open + 1, region.close))
        lastEnd = region.close
    }

    return result
}

fun gutterLines(analysis: SourceAnalysis, hidden: List<Hidden>, folded: Set<Int>): List<GutterLine> {
    val lineStarts = analysis.lineStarts

    val regionByLine = HashMap<Int, FoldRegion>()
    for (region in analysis.regions) {
        if (hidden.any { region.open >= it.start && region.open < it.end }) continue
        regionByLine.putIfAbsent(lineIndexOf(lineStarts, region.open), region)
    }

    val lines = ArrayList<GutterLine>()
    for (i in lineStarts.indices) {
        val start = lineStarts[i]
        // The line break in front of this line is hidden, so it is merged into the previous line
        if (hidden.any { it.start < start && start <= it.end }) continue

        val region = regionByLine[i]
        lines.add(GutterLine(i + 1, region, region != null && region.open in folded))
    }

    return lines
}

fun foldedText(text: AnnotatedString, hidden: List<Hidden>): TransformedText {
    if (hidden.isEmpty()) return TransformedText(text, OffsetMapping.Identity)

    val placeholderStyle = SpanStyle(color = IslandsDark.comment, background = IslandsDark.foldPlaceholder)
    val folded = buildAnnotatedString {
        var pos = 0
        for (h in hidden) {
            append(text.subSequence(pos, h.start))
            withStyle(placeholderStyle) { append(PLACEHOLDER) }
            pos = h.end
        }
        append(text.subSequence(pos, text.length))
    }

    val mapping = object : OffsetMapping {
        override fun originalToTransformed(offset: Int): Int {
            var shift = 0
            for (h in hidden) {
                if (offset <= h.start) break
                if (offset < h.end) return h.start + shift
                shift += PLACEHOLDER.length - (h.end - h.start)
            }
            return offset + shift
        }

        override fun transformedToOriginal(offset: Int): Int {
            var shift = 0
            for (h in hidden) {
                val start = h.start + shift
                if (offset <= start) break
                if (offset < start + PLACEHOLDER.length) return h.start
                shift += PLACEHOLDER.length - (h.end - h.start)
            }
            return offset - shift
        }
    }

    return TransformedText(folded, mapping)
}

/** Keeps fold keys (offsets of the opening brackets) valid across an edit, folds touched by the edit are dropped */
fun shiftFolds(old: String, new: String, folded: Set<Int>): Set<Int> {
    if (folded.isEmpty()) return folded

    val limit = minOf(old.length, new.length)
    var prefix = 0
    while (prefix < limit && old[prefix] == new[prefix]) prefix++
    var suffix = 0
    while (suffix < limit - prefix && old[old.length - 1 - suffix] == new[new.length - 1 - suffix]) suffix++

    val oldEnd = old.length - suffix
    val delta = new.length - old.length

    return folded.mapNotNullTo(HashSet<Int>()) { offset ->
        when {
            offset < prefix -> offset
            offset >= oldEnd -> offset + delta
            else -> null
        }
    }
}
