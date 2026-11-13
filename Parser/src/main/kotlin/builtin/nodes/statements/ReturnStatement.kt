package builtin.nodes.statements

import ExpressionNode
import StatementNode
import TextRange

data class ReturnStatement(
    val value: ExpressionNode?,
    override val pos: TextRange
): StatementNode(pos)