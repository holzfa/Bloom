package builtin.rules.expressions

import ExpressionNode
import Parser
import ParserReader
import ParserRule
import builtin.nodes.expressions.LiteralExpression
import builtin.tokentypes.LiteralTokenType

class LiteralRule(override val parser: Parser): ParserRule() {
    override fun tryExpression(reader: ParserReader): ExpressionNode? {
        val start = reader.currentPos

        val literal = reader.advance()
        return if (literal.type is LiteralTokenType) {
            LiteralExpression(literal.type as LiteralTokenType, literal.value, reader.range(start))
        } else null
    }
}