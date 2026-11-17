package builtin.nodes.expressions

import ExpressionNode
import TextRange
import builtin.nodes.Block
import builtin.nodes.Identifier

data class NumberOfExpression(
    val callee: ExpressionNode,
    override val pos: TextRange
): ExpressionNode(pos)
