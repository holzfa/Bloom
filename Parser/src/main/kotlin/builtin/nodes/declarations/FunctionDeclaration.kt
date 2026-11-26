package builtin.nodes.declarations

import DeclarationNode
import TextRange
import TypeNode
import builtin.nodes.*

data class FunctionDeclaration(
    val modifiers: Set<Modifier>,
    val name: Identifier,
    val generics: List<GenericNode>?,
    val extensionName: Identifier?,
    val parameters: List<Parameter>?,
    val returnType: TypeNode?,
    val body: Block?,
    override val pos: TextRange
): DeclarationNode(pos) {
    override fun toString(): String =
        "fun $name" + (if (!generics.isNullOrEmpty())
            "<${generics.joinToString()}>" else "") + if (parameters != null)
            "(${parameters.joinToString()})" else ""
}