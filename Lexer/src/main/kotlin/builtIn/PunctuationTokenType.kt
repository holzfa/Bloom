package builtIn

import TokenType

enum class PunctuationTokenType: TokenType {
    Dot,
    Comma,
    Semicolon,
    Colon,

    ParenthesisOpen,
    ParenthesisClose,
    BracketOpen,
    BracketClose,
    SquareOpen,
    SquareClose
}

val punctuation: Map<Char, TokenType> = mapOf(
    Pair('.', PunctuationTokenType.Dot),
    Pair(',', PunctuationTokenType.Comma),
    Pair(';', PunctuationTokenType.Semicolon),
    Pair(':', PunctuationTokenType.Colon),

    Pair('(', PunctuationTokenType.ParenthesisOpen),
    Pair(')', PunctuationTokenType.ParenthesisClose),
    Pair('{', PunctuationTokenType.BracketOpen),
    Pair('}', PunctuationTokenType.BracketClose),
    Pair('[', PunctuationTokenType.SquareOpen),
    Pair(']', PunctuationTokenType.SquareClose),
)