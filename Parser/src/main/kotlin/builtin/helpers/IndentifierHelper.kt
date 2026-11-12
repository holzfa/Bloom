package builtin.helpers

import ParserReader
import builtin.nodes.Identifier
import builtin.tokentypes.KeywordTokenType
import builtin.tokentypes.SymbolTokenType
import builtin.tokentypes.VuraTokenType

fun parseIdentifier(reader: ParserReader): Identifier? {
    val parts = mutableListOf<String>()

    parts += reader.expect(VuraTokenType.Identifier)?.value ?: return null
    while (!reader.isAtEnd && reader.hasSequence(SymbolTokenType.Dot, VuraTokenType.Identifier)) {
        reader.expect(SymbolTokenType.Dot) // Consume the '.'
        parts += reader.expect(VuraTokenType.Identifier)?.value ?: return Identifier(parts)
    }

    return if (parts.isNotEmpty()) Identifier(parts) else null
}