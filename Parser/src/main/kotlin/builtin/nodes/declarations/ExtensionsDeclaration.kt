package builtin.nodes.declarations

import DeclarationNode
import TextRange
import TypeNode

data class ExtensionsDeclaration(
    val targetType: TypeNode,
    val declarations: List<DeclarationNode>?,
    override val pos: TextRange
): DeclarationNode(pos)