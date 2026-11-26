package builtin.nodes.declarations

import DeclarationNode
import TextRange

data class ComplementObjectDeclaration(
    val declarations: List<DeclarationNode>?,
    override val pos: TextRange
): DeclarationNode(pos) {
    override fun toString(): String = "complement object"
}