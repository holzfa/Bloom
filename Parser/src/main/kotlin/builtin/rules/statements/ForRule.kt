package builtin.rules.statements

import Parser
import ParserReader
import ParserRule
import StatementNode
import builtin.helpers.parseBlock
import builtin.helpers.parseIdentifier
import builtin.nodes.statements.ForStatement
import builtin.nodes.statements.WhileStatement
import builtin.tokentypes.KeywordTokenType
import builtin.tokentypes.SymbolTokenType

class ForRule(override val parser: Parser): ParserRule() {
    override fun tryStatement(reader: ParserReader): StatementNode? {
        val start = reader.currentPos
        reader.hope(KeywordTokenType.For) ?: return null

        reader.expect(SymbolTokenType.ParenthesisOpen)
        val itemName = parseIdentifier(reader)
        reader.expect(KeywordTokenType.In)
        val collection = parser.requestExpression(reader)
        reader.expect(SymbolTokenType.ParenthesisClose)

        val body = parseBlock(reader, parser)

        return ForStatement(itemName, collection, body, reader.range(start))
    }
}