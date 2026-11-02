package builtin.nodes.declarations

import DeclarationNode
import TextRange
import builtin.nodes.*
import java.util.*

data class FunctionDeclaration(
    val modifiers: Set<Modifier>,
    val name: Identifier,
    val generics: List<GenericNode>?,
    val parameters: List<Parameter>,
    val body: Block?,
    override val pos: TextRange
): DeclarationNode(pos)