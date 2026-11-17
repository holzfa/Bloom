package builtin.nodes.expressions

import ExpressionNode
import TextRange
import builtin.nodes.Block
import builtin.nodes.Identifier

data class IndexExpression(
    val callee: ExpressionNode,
    val index: ExpressionNode,
    override val pos: TextRange
): ExpressionNode(pos)
