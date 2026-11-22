package builtin.nodes

import builtin.tokentypes.KeywordTokenType
import builtin.tokentypes.LiteralTokenType

enum class LiteralType {
    UInt,
    Int,
    ULong,
    Long,
    Float,
    Double,
    String,
    Char,

    It,
    Self,
    Null,
    True,
    False;

    companion object {
        fun fromLiteral(literalTokenType: LiteralTokenType) = when (literalTokenType) {
            LiteralTokenType.UInt -> UInt
            LiteralTokenType.Int -> Int
            LiteralTokenType.ULong -> ULong
            LiteralTokenType.Long -> Long
            LiteralTokenType.Float -> Float
            LiteralTokenType.Double -> Double
            LiteralTokenType.String -> String
            LiteralTokenType.Char -> Char
        }

        fun fromKeyword(keyword: KeywordTokenType): LiteralType? = when (keyword) {
            KeywordTokenType.It -> It
            KeywordTokenType.Self -> Self
            KeywordTokenType.Null -> Null
            KeywordTokenType.True -> True
            KeywordTokenType.False -> False
            else -> null
        }
    }
}