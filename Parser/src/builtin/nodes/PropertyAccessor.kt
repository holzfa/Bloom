package builtin.nodes

import AstNode
import TextRange

data class PropertyAccessor(
    val body: Block?,
    override val pos: TextRange
): AstNode(pos)