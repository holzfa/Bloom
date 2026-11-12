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
    override val pos: TextRange
): DeclarationNode(pos)