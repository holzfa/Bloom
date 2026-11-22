package builtin.nodes.declarations

import DeclarationNode
import TextRange
import builtin.nodes.*

data class TraitDeclaration(
    val modifiers: Set<Modifier>,
    val name: Identifier,
    val generics: List<GenericNode>?,
    val parameters: List<Parameter>?,
    val bases: List<ImplementationNode>?,
    val declarations: List<DeclarationNode>?,
    override val pos: TextRange
): DeclarationNode(pos) {
    override fun toString(): String =
        "trait $name" + if (!generics.isNullOrEmpty())
            "<${generics.joinToString()}>"
        else ""
}