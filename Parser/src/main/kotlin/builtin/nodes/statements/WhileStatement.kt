package builtin.nodes.statements

import ExpressionNode
import StatementNode
import TextRange
import builtin.nodes.Block
import builtin.nodes.Identifier

data class WhileStatement(
    val condition: ExpressionNode?,
    val body: Block?,
    override val pos: TextRange
): StatementNode(pos)