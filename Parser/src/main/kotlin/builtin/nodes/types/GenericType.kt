package builtin.nodes.types

import TextRange
import TypeNode
import builtin.nodes.Identifier

data class GenericType(
    val name: Identifier,
    val genericTypes: List<TypeNode>,
    override val isNullable: Boolean,
    override val pos: TextRange
): TypeNode(isNullable, pos)