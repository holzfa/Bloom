package builtin.nodes.statements

import ExpressionNode
import StatementNode
import TextRange

data class ExpressionStatement(
    val expr: ExpressionNode,
    override val pos: TextRange
): StatementNode(pos)