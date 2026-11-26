package builtin.nodes.expressions

import ExpressionNode
import TextRange

data class IndexExpression(
    val callee: ExpressionNode,
    val index: ExpressionNode,
    override val pos: TextRange
): ExpressionNode(pos)
