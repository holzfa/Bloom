package builtin.helpers

import ParserReader
import builtin.nodes.Identifier
import builtin.nodes.IdentifierPath
import builtin.tokentypes.SymbolTokenType
import builtin.tokentypes.VuraTokenType

fun parseIdentifier(reader: ParserReader): Identifier? {
    return if (reader.isNext(VuraTokenType.Identifier)) Identifier(reader.advance().value)
    else null
}

fun parsePath(reader: ParserReader): IdentifierPath? {
    return IdentifierPath(
        parseList(reader, SymbolTokenType.Dot) {
            parseIdentifier(reader)
        } ?: return null
    )
}