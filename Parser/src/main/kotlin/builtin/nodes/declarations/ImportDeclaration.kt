package builtin.nodes.declarations

import DeclarationNode
import TextRange
import builtin.nodes.Identifier

data class ImportDeclaration(
    val path: Identifier,
    val selection: List<Identifier>?,
    override val pos: TextRange
): DeclarationNode(pos) {
    override fun toString(): String =
        if (selection == null) "import $path"
        else "from $path import ${selection.joinToString()}"
}