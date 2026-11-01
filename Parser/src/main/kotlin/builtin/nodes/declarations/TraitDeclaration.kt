package builtin.nodes.declarations

import DeclarationNode
import TextRange
import builtin.nodes.GenericNode
import builtin.nodes.Identifier
import builtin.nodes.Modifier
import builtin.nodes.Parameter
import java.util.*

data class TraitDeclaration(
    val modifiers: EnumSet<Modifier>,
    val name: Identifier,
    val generics: List<GenericNode>?,
    val parameters: List<Parameter>?,
    val bases: List<ImplementationNode>?,
    val declarations: List<DeclarationNode>?,
    override val pos: TextRange
): DeclarationNode(pos)