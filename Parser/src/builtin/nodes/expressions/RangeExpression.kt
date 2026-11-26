package builtin.nodes.expressions

import ExpressionNode
import TextRange

data class RangeExpression(
    val from: ExpressionNode?,
    val to: ExpressionNode?,
    override val pos: TextRange
): ExpressionNode(pos)
