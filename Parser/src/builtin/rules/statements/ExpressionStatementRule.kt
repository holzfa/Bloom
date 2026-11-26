package builtin.rules.statements

import Parser
import ParserReader
import StatementNode
import StatementRule
import builtin.nodes.statements.ExpressionStatement

class ExpressionStatementRule(override val parser: Parser): StatementRule {
    override fun tryStatement(reader: ParserReader): StatementNode? {
        val start = reader.currentPos

        val expr = parser.requestExpression(reader) ?: return null

        return ExpressionStatement(expr, reader.range(start))
    }
}