package builtin.rules.expressions

import ExpressionNode
import Parser
import ParserReader
import ParserRule
import builtin.helpers.parseAssignmentMod
import builtin.helpers.parseIdentifier
import builtin.nodes.expressions.IdentifierExpression
import builtin.nodes.expressions.RangeExpression
import builtin.tokentypes.SymbolTokenType

class RangeRule(override val parser: Parser): ParserRule() {
    override fun tryExpression(reader: ParserReader): ExpressionNode? {
        val start = reader.currentPos

        val from = parser.requestExpression(reader)
        reader.hope(SymbolTokenType.Range) ?: return null
        val to = parser.requestExpression(reader)

        return RangeExpression(from, to, reader.range(start))
    }
}