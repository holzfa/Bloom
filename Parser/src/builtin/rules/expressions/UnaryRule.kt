package builtin.rules.expressions

import ExpressionNode
import Parser
import ParserReader
import PostExpressionRule
import PreExpressionRule
import builtin.helpers.parseUnaryOperator
import builtin.nodes.expressions.UnaryExpression
import builtin.tokentypes.SymbolTokenType

class UnaryRule(override val parser: Parser): PostExpressionRule {
    override fun tryExpression(reader: ParserReader, expression: ExpressionNode): ExpressionNode? {
        val start = reader.currentPos

        val op = parseUnaryOperator(reader) ?: return null

        return UnaryExpression(expression, op, true, reader.range(start))
    }
}