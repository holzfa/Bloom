package builtin.nodes.statements

import ExpressionNode
import StatementNode
import TextRange
import builtin.nodes.AssignmentModifier

data class AssignmentStatement(
    val location: ExpressionNode?,
    val mod: AssignmentModifier,
    val value: ExpressionNode?,
    override val pos: TextRange
): StatementNode(pos)
