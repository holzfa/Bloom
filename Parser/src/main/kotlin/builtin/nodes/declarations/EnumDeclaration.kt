package builtin.nodes.declarations

import DeclarationNode
import TextRange
import builtin.nodes.Identifier
import builtin.nodes.ImplementationNode
import builtin.nodes.Modifier
import builtin.nodes.Parameter

data class EnumDeclaration(
    val modifiers: Set<Modifier>,
    val name: Identifier,
    val parameters: List<Parameter>?,
    val base: ImplementationNode?,
    val members: List<EnumMember>?,
    val declarations: List<DeclarationNode>?,
    override val pos: TextRange
): DeclarationNode(pos) {
    override fun toString(): String =
        "enum $name" + if (!parameters.isNullOrEmpty())
            "(${parameters.joinToString()})"
        else ""
}