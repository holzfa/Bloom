package builtin.nodes.declarations

import DeclarationNode
import TextRange
import builtin.nodes.Block
import builtin.nodes.Identifier
import builtin.nodes.Modifier
import builtin.nodes.Parameter

data class ConstructorDeclaration(
    val modifiers: Set<Modifier>,
    val name: Identifier,
    val parameters: List<Parameter>?,
    val body: Block?,
    override val pos: TextRange
): DeclarationNode(pos)