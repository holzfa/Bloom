package io.holzfa.bloom.debug

import Lexer
import MessageBag
import TextPos
import Token
import androidx.compose.ui.text.AnnotatedString
import builtin.VuraLexerHandler

class SourceAnalysis(val source: String) {
    val lineStarts: IntArray = lineStartsOf(source)
    val tokens: List<Token> = Lexer(MessageBag()).also { VuraLexerHandler.register(it) }.tokenize(source)
    val highlighted: AnnotatedString = highlight(source, tokens, lineStarts)
    val regions: List<FoldRegion> = findFoldRegions(source, tokens, lineStarts)
}

fun lineStartsOf(source: String): IntArray {
    val starts = ArrayList<Int>()
    starts.add(0)
    source.forEachIndexed { index, c -> if (c == '\n') starts.add(index + 1) }
    return starts.toIntArray()
}

/** Zero based index of the line that contains the offset */
fun lineIndexOf(lineStarts: IntArray, offset: Int): Int {
    val index = java.util.Arrays.binarySearch(lineStarts, offset)
    return if (index >= 0) index else -index - 2
}

/** Converts the 1-based line/column of a token into a character offset, or -1 if it is out of range */
fun offsetOf(lineStarts: IntArray, pos: TextPos): Int {
    val lineStart = lineStarts.getOrNull(pos.line - 1) ?: return -1
    return lineStart + pos.col - 1
}
