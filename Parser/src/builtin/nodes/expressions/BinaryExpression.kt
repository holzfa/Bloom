package builtin.nodes.expressions

import ExpressionNode
import TextRange
import builtin.nodes.BinaryOperator
import builtin.nodes.LiteralType

data class BinaryExpression(
    val left: ExpressionNode,
    val operator: BinaryOperator,
    val right: ExpressionNode?,
    override val pos: TextRange
): ExpressionNode(pos)