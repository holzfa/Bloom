package builtin.rules.expressions

import ExpressionNode
import Parser
import ParserReader
import PreExpressionRule
import builtin.helpers.parseIdentifier
import builtin.nodes.expressions.IdentifierExpression

class IdentifierRule(override val parser: Parser): PreExpressionRule {
    override fun tryExpression(reader: ParserReader): ExpressionNode? {
        val start = reader.currentPos

        val identifier = parseIdentifier(reader) ?: return null

        return IdentifierExpression(identifier, reader.range(start))
    }
}