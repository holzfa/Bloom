package builtIn

import TokenType

enum class SymbolTokenType: TokenType {
    LeftShiftAssign,

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
    LogicalAnd,
    LogicalOr,
    LogicalXor,
    Increment,
    Decrement,

    LeftShift,

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

    Cast,
    Arrow,
    Range,
    NumberOf,

    Dot,
    Comma,
    Semicolon,
    Colon,

    Underscore,

    ParenthesisOpen,
    ParenthesisClose,
    BracketOpen,
    BracketClose,
    SquareOpen,
    SquareClose
}

// '>>' and '>>=' are intentionally missing: '>' is always a single token so nested generics
// like Array<Array<Int>> work. The parser combines adjacent '>' tokens into a shift.
val symbols: Map<String, TokenType> = mapOf(
    Pair("<<=", SymbolTokenType.LeftShiftAssign),

    Pair("+=", SymbolTokenType.AddAssign),
    Pair("-=", SymbolTokenType.SubtractAssign),
    Pair("*=", SymbolTokenType.MultiplyAssign),
    Pair("/=", SymbolTokenType.DivideAssign),
    Pair("%=", SymbolTokenType.ModuloAssign),

    Pair("&=", SymbolTokenType.AndAssign),
    Pair("|=", SymbolTokenType.OrAssign),
    Pair("^=", SymbolTokenType.XorAssign),

    Pair("==", SymbolTokenType.Equality),
    Pair("!=", SymbolTokenType.Inequality),
    Pair("<=", SymbolTokenType.LessThanEqual),

    Pair("&&", SymbolTokenType.LogicalAnd),
    Pair("||", SymbolTokenType.LogicalOr),
    Pair("^^", SymbolTokenType.LogicalXor),

    Pair("++", SymbolTokenType.Increment),
    Pair("--", SymbolTokenType.Decrement),

    Pair("<<", SymbolTokenType.LeftShift),

    Pair("->", SymbolTokenType.Arrow),
    Pair("::", SymbolTokenType.Cast),
    Pair("..", SymbolTokenType.Range),
    Pair("#", SymbolTokenType.NumberOf),

    Pair("+", SymbolTokenType.Add),
    Pair("-", SymbolTokenType.Subtract),
    Pair("*", SymbolTokenType.Multiply),
    Pair("/", SymbolTokenType.Divide),
    Pair("%", SymbolTokenType.Modulo),

    Pair("=", SymbolTokenType.Assign),

    Pair("<", SymbolTokenType.LessThan),
    Pair(">", SymbolTokenType.GreaterThan),
    Pair("!", SymbolTokenType.LogicalNot),
    Pair("&", SymbolTokenType.BitwiseAnd),
    Pair("|", SymbolTokenType.BitwiseOr),
    Pair("^", SymbolTokenType.BitwiseXor),
    Pair("~", SymbolTokenType.Tilde),
    Pair("?", SymbolTokenType.Nullable),
    Pair("@", SymbolTokenType.At),

    Pair(".", SymbolTokenType.Dot),
    Pair(",", SymbolTokenType.Comma),
    Pair(";", SymbolTokenType.Semicolon),
    Pair(":", SymbolTokenType.Colon),

    Pair("_", SymbolTokenType.Underscore),

    Pair("(", SymbolTokenType.ParenthesisOpen),
    Pair(")", SymbolTokenType.ParenthesisClose),
    Pair("{", SymbolTokenType.BracketOpen),
    Pair("}", SymbolTokenType.BracketClose),
    Pair("[", SymbolTokenType.SquareOpen),
    Pair("]", SymbolTokenType.SquareClose),
)