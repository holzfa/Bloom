package builtin.helpers

import ErrorType
import ParserReader
import TypeNode
import builtin.nodes.types.GenericType
import builtin.nodes.types.LambdaType
import builtin.nodes.types.ScalarType
import builtin.tokentypes.SymbolTokenType
import builtin.tokentypes.VuraTokenType
import java.io.Reader

fun parseType(reader: ParserReader): TypeNode? {
    val start = reader.currentPos
    return if (reader.peek().type == SymbolTokenType.ParenthesisOpen) {
        parseLambda(reader)
    } else {
        parseScalarOrGeneric(reader)
    }
}
private fun parseScalarOrGeneric(reader: ParserReader): TypeNode? {
    val start = reader.currentPos
    val name = parseIdentifier(reader) ?: return null

    if (reader.hope(SymbolTokenType.LessThan) != null) {
        val generics = mutableListOf<TypeNode>()

        while (!reader.isAtEnd && reader.hope(SymbolTokenType.GreaterThan) != null) {
            val type = parseType(reader)
            if (type != null) generics.add(type)
        }

        val isNullable = reader.hope(SymbolTokenType.Nullable) != null
        return GenericType(name, generics, isNullable, reader.range(start))
    }

    val isNullable = reader.hope(SymbolTokenType.Nullable) != null
    return ScalarType(name, isNullable, reader.range(start))
}

private fun parseLambda(reader: ParserReader): LambdaType? {
    val start = reader.currentPos
    reader.expect(SymbolTokenType.ParenthesisOpen)

    val params = mutableListOf<TypeNode>()
    while (!reader.isAtEnd && reader.hope(SymbolTokenType.ParenthesisClose) != null) {
        val type = parseType(reader)
        if (type != null) params.add(type)
    }

    reader.expect(SymbolTokenType.Subtract)
    reader.expect(SymbolTokenType.GreaterThan)
    val isNullable = reader.hope(SymbolTokenType.Nullable) != null

    val returnType = parseType(reader) ?: return null
    return LambdaType(params, returnType, isNullable, reader.range(start))
}