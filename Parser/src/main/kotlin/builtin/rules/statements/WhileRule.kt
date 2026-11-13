package builtin.rules.statements

import Parser
import ParserReader
import ParserRule
import StatementNode
import builtin.helpers.parseBlock
import builtin.nodes.statements.WhileStatement
import builtin.tokentypes.KeywordTokenType
import builtin.tokentypes.SymbolTokenType

class WhileRule(override val parser: Parser): ParserRule() {
    override fun tryStatement(reader: ParserReader): StatementNode? {
        val start = reader.currentPos
        reader.hope(KeywordTokenType.While) ?: return null

        reader.expect(SymbolTokenType.ParenthesisOpen)
        val condition = parser.requestExpression(reader)
        reader.expect(SymbolTokenType.ParenthesisClose)

        val body = parseBlock(reader, parser)

        return WhileStatement(condition, body, reader.range(start))
    }
}