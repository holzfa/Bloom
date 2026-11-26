package builtin.rules.expressions

import ExpressionNode
import Parser
import ParserReader
import PostExpressionRule
import builtin.helpers.parseExpressionList
import builtin.nodes.expressions.ArrayLiteralExpression
import builtin.tokentypes.SymbolTokenType

class ArrayLiteralRule(override val parser: Parser): PostExpressionRule {
    override fun tryExpression(reader: ParserReader, expression: ExpressionNode): ExpressionNode? {
        val start = expression.pos.start

        val elements = parseExpressionList(SymbolTokenType.SquareOpen, SymbolTokenType.SquareClose, reader, parser) ?: return null

        return ArrayLiteralExpression(elements, reader.range(start))
    }
}