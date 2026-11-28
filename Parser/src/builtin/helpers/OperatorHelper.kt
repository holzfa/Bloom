package builtin.helpers

import ParserReader
import builtin.nodes.BinaryOperator
import builtin.tokentypes.KeywordTokenType
import builtin.tokentypes.SymbolTokenType

fun parseBinaryOp(reader: ParserReader): BinaryOperator? {
    val tokenType = reader.peek().type

    val result = when (tokenType) {
        SymbolTokenType.Equality        -> BinaryOperator.Equality
        SymbolTokenType.Inequality      -> BinaryOperator.Inequality
        SymbolTokenType.LessThanEqual   -> BinaryOperator.LessThanEqual
        SymbolTokenType.LogicalAnd      -> BinaryOperator.LogicalAnd
        SymbolTokenType.LogicalOr       -> BinaryOperator.LogicalOr
        SymbolTokenType.LogicalXor      -> BinaryOperator.LogicalXor
        SymbolTokenType.LeftShift       -> BinaryOperator.LeftShift
        SymbolTokenType.Add             -> BinaryOperator.Add
        SymbolTokenType.Subtract        -> BinaryOperator.Subtract
        SymbolTokenType.Multiply        -> BinaryOperator.Multiply
        SymbolTokenType.Divide          -> BinaryOperator.Divide
        SymbolTokenType.Modulo          -> BinaryOperator.Modulo
        SymbolTokenType.LessThan        -> BinaryOperator.LessThan
        SymbolTokenType.GreaterThan     -> {
            if (reader.peek().type == SymbolTokenType.GreaterThan) BinaryOperator.RightShift
            else BinaryOperator.GreaterThan
        }
        SymbolTokenType.LogicalNot      -> BinaryOperator.LogicalNot
        SymbolTokenType.BitwiseAnd      -> BinaryOperator.BitwiseAnd
        SymbolTokenType.BitwiseOr       -> BinaryOperator.BitwiseOr
        SymbolTokenType.BitwiseXor      -> BinaryOperator.BitwiseXor
        KeywordTokenType.Is             -> BinaryOperator.Is
        else -> null
    }
    if (result != null) reader.advance()
    return result
}