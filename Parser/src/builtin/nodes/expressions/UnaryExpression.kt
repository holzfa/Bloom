package builtin.nodes.expressions

import ExpressionNode
import TextRange
import builtin.nodes.BinaryOperator
import builtin.nodes.LiteralType
import builtin.nodes.UnaryOperator

data class UnaryExpression(
    val operand: ExpressionNode,
    val operator: UnaryOperator,
    val isPostFix: Boolean,
    override val pos: TextRange
): ExpressionNode(pos)