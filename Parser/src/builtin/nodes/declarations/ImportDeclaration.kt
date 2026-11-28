package builtin.nodes.declarations

import DeclarationNode
import TextRange
import builtin.nodes.Identifier
import builtin.nodes.IdentifierPath

data class ImportDeclaration(
    val path: IdentifierPath,
    val selection: List<Identifier>?,
    override val pos: TextRange
): DeclarationNode(pos) {
    override fun toString(): String =
        if (selection == null) "import $path"
        else "from $path import ${selection.joinToString()}"
}