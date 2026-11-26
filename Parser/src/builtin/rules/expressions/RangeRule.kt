package builtin.rules.expressions

import ExpressionNode
import Parser
import ParserReader
import PostExpressionRule
import builtin.nodes.expressions.RangeExpression
import builtin.tokentypes.SymbolTokenType

class RangeRule(override val parser: Parser): PostExpressionRule {
    override fun tryExpression(reader: ParserReader, expression: ExpressionNode): ExpressionNode? {
        val start = reader.currentPos

        reader.hope(SymbolTokenType.Range) ?: return null
        val to = parser.requestExpression(reader)

        return RangeExpression(expression, to, reader.range(start))
    }
}