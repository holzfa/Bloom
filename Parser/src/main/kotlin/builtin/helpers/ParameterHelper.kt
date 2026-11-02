package builtin.helpers

import ParserReader
import builtin.nodes.Parameter
import builtin.tokentypes.SymbolTokenType

fun parseParametersDeclaration(reader: ParserReader): List<Parameter>? {
    if (reader.hope(SymbolTokenType.ParenthesisOpen) != null) {
        val params = mutableListOf<Parameter>()

        while (!reader.isAtEnd && reader.hope(SymbolTokenType.ParenthesisClose) == null) {
            val param = parseParameterDeclaration(reader)
            if (param != null) params.add(param)
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
    val type = parseType(reader)

    return Parameter(varMod, name, type, reader.range(start))
}