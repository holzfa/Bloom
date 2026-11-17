package builtin.rules.expressions

import ExpressionNode
import Parser
import ParserReader
import ParserRule
import builtin.helpers.parseIdentifier
import builtin.nodes.expressions.IndexExpression
import builtin.tokentypes.SymbolTokenType

class IndexRule(override val parser: Parser): ParserRule() {
    override fun tryExpression(reader: ParserReader): ExpressionNode? {
        val start = reader.currentPos

        val callee = parser.requestExpression(reader) ?: return null

        reader.hope(SymbolTokenType.SquareOpen) ?: return null
        val index = parser.requestExpression(reader) ?: return null
        reader.expect(SymbolTokenType.SquareOpen)

        return IndexExpression(callee, index, reader.range(start))
    }
}