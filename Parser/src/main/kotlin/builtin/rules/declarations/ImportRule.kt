package builtin.rules.declarations

import Parser
import ParserReader
import ParserRule
import builtin.helpers.parseIdentifier
import builtin.nodes.Identifier
import builtin.nodes.declarations.ImportDeclaration
import builtin.tokentypes.KeywordTokenType
import builtin.tokentypes.SymbolTokenType
import builtin.tokentypes.VuraTokenType

internal class ImportRule(override val parser: Parser): ParserRule() {
    override fun tryDeclaration(reader: ParserReader): ImportDeclaration? {
        val startType = reader.peek().type
        if (startType != KeywordTokenType.Import && startType != KeywordTokenType.From) return null

        val start = reader.currentPos
        reader.advance()

        if (startType == KeywordTokenType.Import) {
            val path = parseIdentifier(reader)
            reader.expectTerminator()

            return if (path != null) {
                ImportDeclaration(path, null, reader.range(start))
            } else {
                null
            }
        } else {
            val path = parseIdentifier(reader) ?: return null

            reader.expect(KeywordTokenType.Import) ?: return null

            val selection = mutableListOf<Identifier>()
            while (!reader.isAtEnd) {
                val symbol = parseIdentifier(reader)
                if (symbol != null) {
                    selection.add(symbol)
                    if (!reader.isTerminator() && reader.hasSequence(SymbolTokenType.Comma, VuraTokenType.Identifier)) {
                        reader.expect(SymbolTokenType.Comma)
                    }
                } else {
                    break
                }
            }
            reader.hope(SymbolTokenType.Comma) // Consume trailing comma
            reader.expectTerminator()

            return ImportDeclaration(path, selection, reader.range(start))
        }
    }
}