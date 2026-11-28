package builtin.rules.expressions

import ExpressionNode
import Parser
import ParserReader
import PostExpressionRule
import PreExpressionRule
import builtin.helpers.parseBinaryOperator
import builtin.helpers.parseUnaryOperator
import builtin.nodes.expressions.BinaryExpression
import builtin.nodes.expressions.UnaryExpression
import builtin.tokentypes.SymbolTokenType

class BinaryRule(override val parser: Parser): PostExpressionRule {
    override fun tryExpression(reader: ParserReader, expression: ExpressionNode): ExpressionNode? {
        val start = reader.currentPos

        val op = parseBinaryOperator(reader) ?: return null
        val right = parser.requestExpression(reader)

        return BinaryExpression(expression, op, right, reader.range(start))
    }
}