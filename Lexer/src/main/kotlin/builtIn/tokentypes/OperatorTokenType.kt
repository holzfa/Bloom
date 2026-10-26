package builtIn.tokentypes

import TokenType

enum class OperatorTokenType: TokenType {
    LeftShiftAssign,
    RightShiftAssign,

    AddAssign,
    SubtractAssign,
    MultiplyAssign,
    DivideAssign,
    ModuloAssign,

    AndAssign,
    OrAssign,
    XorAssign,

    Equality,
    Inequality,
    LessThanEqual,
    GreaterThanEqual,
    LogicalAnd,
    LogicalOr,
    LogicalXor,
    Increment,
    Decrement,

    LeftShift,
    RightShift,

    Add,
    Subtract,
    Multiply,
    Divide,
    Modulo,

    Assign,

    LessThan,
    GreaterThan,
    LogicalNot,
    BitwiseAnd,
    BitwiseOr,
    BitwiseXor,
    Tilde,
    Nullable,
    At,

    CastSymbol,
    ArrowSymbol,
    RangeSymbol
}

val singleOps: Map<Char, TokenType> = mapOf(
    Pair('+', OperatorTokenType.Add),
    Pair('-', OperatorTokenType.Subtract),
    Pair('*', OperatorTokenType.Multiply),
    Pair('/', OperatorTokenType.Divide),
    Pair('%', OperatorTokenType.Modulo),

    Pair('=', OperatorTokenType.Assign),

    Pair('<', OperatorTokenType.LessThan),
    Pair('>', OperatorTokenType.GreaterThan),
    Pair('!', OperatorTokenType.LogicalNot),
    Pair('&', OperatorTokenType.BitwiseAnd),
    Pair('|', OperatorTokenType.BitwiseOr),
    Pair('^', OperatorTokenType.BitwiseXor),
    Pair('~', OperatorTokenType.Tilde),
    Pair('?', OperatorTokenType.Nullable),
    Pair('@', OperatorTokenType.At),
)

val doubleOps: Map<Pair<Char, Char>, TokenType> = mapOf(
    Pair(Pair('+', '='), OperatorTokenType.AddAssign),
    Pair(Pair('-', '='), OperatorTokenType.SubtractAssign),
    Pair(Pair('*', '='), OperatorTokenType.MultiplyAssign),
    Pair(Pair('/', '='), OperatorTokenType.DivideAssign),
    Pair(Pair('%', '='), OperatorTokenType.ModuloAssign),

    Pair(Pair('&', '='), OperatorTokenType.AndAssign),
    Pair(Pair('|', '='), OperatorTokenType.OrAssign),
    Pair(Pair('^', '='), OperatorTokenType.XorAssign),

    Pair(Pair('=', '='), OperatorTokenType.Equality),
    Pair(Pair('!', '='), OperatorTokenType.Inequality),
    Pair(Pair('<', '='), OperatorTokenType.LessThanEqual),
    Pair(Pair('>', '='), OperatorTokenType.GreaterThanEqual),

    Pair(Pair('&', '&'), OperatorTokenType.LogicalAnd),
    Pair(Pair('|', '|'), OperatorTokenType.LogicalOr),
    Pair(Pair('^', '^'), OperatorTokenType.LogicalXor),

    Pair(Pair('+', '+'), OperatorTokenType.Increment),
    Pair(Pair('-', '-'), OperatorTokenType.Decrement),

    Pair(Pair('<', '<'), OperatorTokenType.LeftShift),
    Pair(Pair('>', '>'), OperatorTokenType.RightShift),

    Pair(Pair('-', '>'), OperatorTokenType.ArrowSymbol),
    Pair(Pair(':', ':'), OperatorTokenType.CastSymbol),
    Pair(Pair('.', '.'), OperatorTokenType.RangeSymbol),
)

val tripleOps: Map<Triple<Char, Char, Char>, TokenType> = mapOf(
    Pair(Triple('<', '<', '='), OperatorTokenType.LeftShiftAssign),
    Pair(Triple('>', '>', '='), OperatorTokenType.RightShiftAssign),
)