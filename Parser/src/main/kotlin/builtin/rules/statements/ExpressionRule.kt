package builtin.rules.statements

import Parser
import ParserReader
import ParserRule
import StatementNode
import builtin.nodes.statements.AssignmentStatement
import builtin.nodes.statements.ExpressionStatement
import builtin.nodes.statements.ReturnStatement
import builtin.tokentypes.KeywordTokenType

class ExpressionRule(override val parser: Parser): ParserRule() {
    override fun tryStatement(reader: ParserReader): StatementNode? {
        val start = reader.currentPos

        val expr = parser.requestExpression(reader) ?: return null

        return ExpressionStatement(expr, reader.range(start))
    }
}