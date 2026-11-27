package builtin.nodes.expressions

import ExpressionNode
import TextRange
import builtin.nodes.LiteralType

data class MemberAccessExpression(
    val parent: ExpressionNode,
    val child: ExpressionNode?,
    override val pos: TextRange
): ExpressionNode(pos)