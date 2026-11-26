package builtin.rules.statements

import Parser
import ParserReader
import StatementNode
import StatementRule
import builtin.nodes.statements.ReturnStatement
import builtin.tokentypes.KeywordTokenType

class ReturnRule(override val parser: Parser): StatementRule {
    override fun tryStatement(reader: ParserReader): StatementNode? {
        val start = reader.currentPos

        if (reader.hope(KeywordTokenType.Return) == null) return null
        val expr = parser.requestExpression(reader)

        return ReturnStatement(expr, reader.range(start))
    }
}