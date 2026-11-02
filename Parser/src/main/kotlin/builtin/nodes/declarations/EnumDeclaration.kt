package builtin.nodes.declarations

import DeclarationNode
import TextRange
import builtin.nodes.Identifier
import builtin.nodes.Modifier
import builtin.nodes.Parameter
import java.util.*

data class EnumDeclaration(
    val modifiers: Set<Modifier>,
    val name: Identifier,
    val parameters: List<Parameter>?,
    val base: ImplementationNode?,
    val members: List<EnumMember>?,
    override val pos: TextRange
): DeclarationNode(pos)