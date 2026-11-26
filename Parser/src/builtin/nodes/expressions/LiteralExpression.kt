package builtin.nodes.expressions

import ExpressionNode
import TextRange
import builtin.nodes.LiteralType

data class LiteralExpression(
    val type: LiteralType,
    val textRepresentation: String,
    override val pos: TextRange
): ExpressionNode(pos)