package builtin.rules.expressions

import ExpressionNode
import Parser
import ParserReader
import PreExpressionRule
import builtin.helpers.parseIdentifier
import builtin.nodes.expressions.IdentifierExpression
import builtin.rules.expressions.MemberAccessRule.Companion.allowedChildren
import builtin.tokentypes.SymbolTokenType

class BoxedRule(override val parser: Parser): PreExpressionRule {
    override fun tryExpression(reader: ParserReader): ExpressionNode? {
        reader.hope(SymbolTokenType.ParenthesisOpen) ?: return null
        val expr = parser.requestExpression(reader)
        reader.expect(SymbolTokenType.ParenthesisClose)

        return expr
    }
}