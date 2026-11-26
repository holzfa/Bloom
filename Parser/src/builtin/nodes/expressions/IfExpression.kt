package builtin.nodes.expressions

import ExpressionNode
import TextRange
import builtin.nodes.Block

data class IfExpression(
    val condition: ExpressionNode?,
    val thenBranch: Block?,
    val elseBranch: Block?,
    override val pos: TextRange
): ExpressionNode(pos)
