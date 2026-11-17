package builtin.rules.expressions

import ExpressionNode
import Parser
import ParserReader
import ParserRule
import builtin.helpers.parseAssignmentMod
import builtin.helpers.parseIdentifier
import builtin.nodes.expressions.IdentifierExpression

class IdentifierRule(override val parser: Parser): ParserRule() {
    override fun tryExpression(reader: ParserReader): ExpressionNode? {
        val start = reader.currentPos

        val identifier = parseIdentifier(reader) ?: return null

        return IdentifierExpression(identifier, reader.range(start))
    }
}