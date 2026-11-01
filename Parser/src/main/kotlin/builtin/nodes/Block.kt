package builtin.nodes

import AstNode
import StatementNode
import TextRange

data class Block(
    val statements: List<StatementNode>,
    override val pos: TextRange
): AstNode(pos)