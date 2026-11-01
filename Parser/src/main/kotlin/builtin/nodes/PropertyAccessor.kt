package builtin.nodes

import AstNode
import TextRange

data class PropertyAccessor(
    val modifier: Modifier,
    val body: Block?,
    override val pos: TextRange
): AstNode(pos)