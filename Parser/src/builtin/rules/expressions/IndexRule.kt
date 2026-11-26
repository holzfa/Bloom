package builtin.rules.expressions

import ExpressionNode
import Parser
import ParserReader
import PostExpressionRule
import builtin.nodes.expressions.IndexExpression
import builtin.tokentypes.SymbolTokenType

class IndexRule(override val parser: Parser): PostExpressionRule {
    override fun tryExpression(reader: ParserReader, expression: ExpressionNode): ExpressionNode? {
        val start = reader.currentPos

        reader.hope(SymbolTokenType.SquareOpen) ?: return null
        val index = parser.requestExpression(reader) ?: return null
        reader.expect(SymbolTokenType.SquareClose)

        return IndexExpression(expression, index, reader.range(start))
    }
}