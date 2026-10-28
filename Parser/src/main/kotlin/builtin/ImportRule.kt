package builtin

import Parser
import ParserRule
import ParserReader
import builtin.tokentypes.KeywordTokenType
import builtin.tokentypes.KlarTokenType
import builtin.tokentypes.SymbolTokenType
import builtin.nodes.ImportStatement

internal class ImportRule(override val parser: Parser): ParserRule<ImportStatement> {
    /**
     * EXAMPLE: `import SomeModule` or `from SomeModule import test, test2`
     */
    override fun tryMatch(reader: ParserReader): ImportStatement? {
        val startType = reader.peek().type
        if (startType != KeywordTokenType.Import && startType != KeywordTokenType.From) return null

        val start = reader.currentPos
        reader.advance()

        if (startType == KeywordTokenType.Import) {
            val pathToken = reader.expect(KlarTokenType.Identifier, "Expected a path to import")
            reader.expectTerminator()
            return if (pathToken != null) {
                ImportStatement(pathToken.value, null, reader.range(start))
            } else {
                null
            }
        } else {
            val pathToken = reader.expect(KlarTokenType.Identifier, "Expected a path to import") ?: return null

            reader.expect(KeywordTokenType.Import) ?: return null

            val selection = mutableListOf<String>()
            while (!reader.isAtEnd) {
                val symbolToken = reader.hope(KlarTokenType.Identifier)
                if (symbolToken != null) {
                    selection.add(symbolToken.value)
                    if (!reader.isTerminator() && reader.peek(1).type == KlarTokenType.Identifier) {
                        reader.expect(SymbolTokenType.Comma)
                    }
                } else {
                    break
                }
            }
            reader.hope(SymbolTokenType.Comma) // Consume trailing comma
            reader.expectTerminator()

            return ImportStatement(pathToken.value, selection, reader.range(start))
        }
    }
}