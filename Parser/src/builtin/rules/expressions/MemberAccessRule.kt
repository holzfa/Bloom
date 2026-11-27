package builtin.rules.expressions

import ExpressionNode
import ExpressionRule
import Parser
import ParserReader
import PostExpressionRule
import PreExpressionRule
import builtin.helpers.parseIdentifier
import builtin.helpers.parseList
import builtin.nodes.expressions.CallExpression
import builtin.nodes.expressions.IdentifierExpression
import builtin.nodes.expressions.LiteralExpression
import builtin.nodes.expressions.MemberAccessExpression
import builtin.tokentypes.SymbolTokenType
import builtin.tokentypes.VuraTokenType
import kotlin.reflect.KClass

class MemberAccessRule(override val parser: Parser): PostExpressionRule {
    override fun tryExpression(reader: ParserReader, expression: ExpressionNode): ExpressionNode? {
        val start = reader.currentPos

        if (reader.hope(SymbolTokenType.Dot) == null) return null
        val child = parser.requestExpression(reader, allowedChildren)

        return MemberAccessExpression(expression, child, reader.range(start))
    }

    companion object {
        val allowedChildren = setOf<KClass<out ExpressionRule>>(
            MemberAccessRule::class,
            IdentifierRule::class,
            CallRule::class,
            IndexRule::class,
            NumberOfRule::class
        )
    }
}