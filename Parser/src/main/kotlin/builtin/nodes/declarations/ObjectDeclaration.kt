package builtin.nodes.declarations

import DeclarationNode
import TextRange
import builtin.nodes.Identifier
import builtin.nodes.Modifier
import java.util.*

data class ObjectDeclaration(
    val modifiers: Set<Modifier>,
    val name: Identifier,
    val bases: List<ImplementationNode>?,
    val declarations: List<DeclarationNode>?,
    override val pos: TextRange
): DeclarationNode(pos)