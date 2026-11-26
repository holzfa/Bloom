package builtin.nodes.expressions

import ExpressionNode
import TextRange

data class NumberOfExpression(
    val callee: ExpressionNode,
    override val pos: TextRange
): ExpressionNode(pos)
