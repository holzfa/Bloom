package builtin.rules.declarations

import DeclarationRule
import Parser
import ParserReader
import builtin.helpers.parseIdentifier
import builtin.helpers.parseList
import builtin.helpers.parsePath
import builtin.nodes.Identifier
import builtin.nodes.declarations.ImportDeclaration
import builtin.tokentypes.KeywordTokenType
import builtin.tokentypes.SymbolTokenType
import builtin.tokentypes.VuraTokenType

internal class ImportRule(override val parser: Parser): DeclarationRule {
    override fun tryDeclaration(reader: ParserReader): ImportDeclaration? {
        val startType = reader.peek().type
        if (startType != KeywordTokenType.Import && startType != KeywordTokenType.From) return null

        val start = reader.currentPos
        reader.advance()

        if (startType == KeywordTokenType.Import) {
            val path = parsePath(reader)
            reader.expectTerminator()

            return if (path != null) {
                ImportDeclaration(path, null, reader.range(start))
            } else {
                null
            }
        } else {
            val path = parsePath(reader) ?: return null

            reader.expect(KeywordTokenType.Import) ?: return null
            val selection = parseList(reader) { parseIdentifier(reader) }

            return ImportDeclaration(path, selection, reader.range(start))
        }
    }
}