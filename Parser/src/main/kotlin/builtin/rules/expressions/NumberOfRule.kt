package builtin.rules.expressions

import ExpressionNode
import Parser
import ParserReader
import PostExpressionRule
import builtin.nodes.expressions.NumberOfExpression
import builtin.tokentypes.SymbolTokenType

class NumberOfRule(override val parser: Parser): PostExpressionRule {
    override fun tryExpression(reader: ParserReader, expression: ExpressionNode): ExpressionNode? {
        val start = reader.currentPos

        reader.hope(SymbolTokenType.NumberOf) ?: return null

        return NumberOfExpression(expression, reader.range(start))
    }
}