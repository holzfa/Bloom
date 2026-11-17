package builtin.rules.expressions

import ExpressionNode
import Parser
import ParserReader
import ParserRule
import builtin.helpers.parseIdentifier
import builtin.nodes.expressions.NumberOfExpression
import builtin.tokentypes.SymbolTokenType

class NumberOfRule(override val parser: Parser): ParserRule() {
    override fun tryExpression(reader: ParserReader): ExpressionNode? {
        val start = reader.currentPos

        val callee = parser.requestExpression(reader) ?: return null
        reader.hope(SymbolTokenType.NumberOf) ?: return null

        return NumberOfExpression(callee, reader.range(start))
    }
}