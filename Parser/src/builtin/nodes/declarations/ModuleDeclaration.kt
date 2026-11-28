package builtin.nodes.declarations

import DeclarationNode
import TextRange
import builtin.nodes.Identifier
import builtin.nodes.IdentifierPath

data class ModuleDeclaration(
    val path: IdentifierPath,
    val declarations: List<DeclarationNode>,
    override val pos: TextRange
): DeclarationNode(pos) {
    override fun toString(): String = "module $path"
}