package builtin.helpers

import ErrorType
import ParserReader
import TokenType
import builtin.nodes.Parameter
import builtin.tokentypes.KeywordTokenType
import builtin.tokentypes.SymbolTokenType
import builtin.tokentypes.VuraTokenType

private val acceptedStarts = setOf<TokenType>(
    KeywordTokenType.Const,
    KeywordTokenType.Val,
    KeywordTokenType.Var,
    VuraTokenType.Identifier
)

fun parseParametersDeclaration(reader: ParserReader): List<Parameter>? {
    if (reader.hope(SymbolTokenType.ParenthesisOpen) != null) {
        val params = mutableListOf<Parameter>()

        while (!reader.isAtEnd && reader.hope(SymbolTokenType.ParenthesisClose) == null) {
            val param = parseParameterDeclaration(reader)
            if (param != null) params.add(param)

            if (reader.peek(1).type in acceptedStarts) {
                reader.expect(SymbolTokenType.Comma)
            } else {
                reader.hope(SymbolTokenType.Comma)
            }
        }

        return params
    } else {
        return null
    }
}

fun parseParameterDeclaration(reader: ParserReader): Parameter? {
    val start = reader.currentPos

    val varMod = parseVariableMod(reader)
    val name = parseIdentifier(reader) ?: return null
    reader.expect(SymbolTokenType.Colon)

    val typeStart = reader.currentPos
    val type = parseType(reader) ?: ErrorType(reader.range(typeStart))

    return Parameter(varMod, name, type, reader.range(start))
}