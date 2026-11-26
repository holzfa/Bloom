package builtin.rules.expressions

import ExpressionNode
import Parser
import ParserReader
import PreExpressionRule
import builtin.nodes.expressions.NumberOfExpression
import builtin.tokentypes.SymbolTokenType

class NumberOfRule(override val parser: Parser): PreExpressionRule {
    override fun tryExpression(reader: ParserReader): ExpressionNode? {
        val start = reader.currentPos

        reader.hope(SymbolTokenType.NumberOf) ?: return null
        val expression = parser.requestExpression(reader) ?: return null

        return NumberOfExpression(expression, reader.range(start))
    }
}