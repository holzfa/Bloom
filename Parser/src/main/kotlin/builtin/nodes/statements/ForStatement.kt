package builtin.nodes.statements

import ExpressionNode
import StatementNode
import TextRange
import builtin.nodes.Block
import builtin.nodes.Identifier

data class ForStatement(
    val itemName: Identifier?,
    val collection: ExpressionNode?,
    val body: Block?,
    override val pos: TextRange
): StatementNode(pos)