package builtin.tokentypes

import TokenType

enum class LiteralTokenType: TokenType {
    UInt,
    Int,
    ULong,
    Long,
    Float,
    Double,

    String,
    Char
}