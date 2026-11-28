package builtin.rules.expressions

import ExpressionNode
import Parser
import ParserReader
import PostExpressionRule
import PreExpressionRule
import builtin.helpers.parseExpressionList
import builtin.nodes.expressions.ArrayLiteralExpression
import builtin.tokentypes.SymbolTokenType

class ArrayLiteralRule(override val parser: Parser): PreExpressionRule {
    override fun tryExpression(reader: ParserReader): ExpressionNode? {
        val start = reader.currentPos

        val elements = parseExpressionList(SymbolTokenType.SquareOpen, SymbolTokenType.SquareClose, reader, parser) ?: return null

        return ArrayLiteralExpression(elements, reader.range(start))
    }
}