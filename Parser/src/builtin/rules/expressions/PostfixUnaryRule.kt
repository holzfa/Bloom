package builtin.rules.expressions

import ExpressionNode
import Parser
import ParserReader
import PreExpressionRule
import builtin.helpers.parseUnaryOperator
import builtin.nodes.expressions.UnaryExpression

class PostfixUnaryRule(override val parser: Parser): PreExpressionRule {
    override fun tryExpression(reader: ParserReader): ExpressionNode? {
        val start = reader.currentPos

        val op = parseUnaryOperator(reader) ?: return null
        val expression = parser.requestExpression(reader) ?: return null

        return UnaryExpression(expression, op, false, reader.range(start))
    }
}