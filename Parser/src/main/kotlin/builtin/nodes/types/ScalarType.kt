package builtin.nodes.types

import TextRange
import TypeNode
import builtin.nodes.Identifier

data class ScalarType(
    val name: Identifier,
    override val isNullable: Boolean,
    override val pos: TextRange
): TypeNode(isNullable, pos)