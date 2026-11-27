package io.holzfa.bloom.debug

import Token
import TokenType
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import builtin.tokentypes.KeywordTokenType
import builtin.tokentypes.LiteralTokenType
import builtin.tokentypes.VuraTokenType

fun highlight(source: String, tokens: List<Token>, lineStarts: IntArray): AnnotatedString = buildAnnotatedString {
    append(source)

    for (token in tokens) {
        val color = colorOf(token.type) ?: continue
        val start = offsetOf(lineStarts, token.pos)
        val end = minOf(start + token.pos.length, source.length)
        if (start in 0 until end) addStyle(SpanStyle(color = color), start, end)
    }
}

private fun colorOf(type: TokenType): Color? = when (type) {
    is KeywordTokenType -> IslandsDark.keyword
    LiteralTokenType.String, LiteralTokenType.Char -> IslandsDark.string
    is LiteralTokenType -> IslandsDark.number
    VuraTokenType.Comment -> IslandsDark.comment
    VuraTokenType.Documentation -> IslandsDark.documentation
    else -> null
}
