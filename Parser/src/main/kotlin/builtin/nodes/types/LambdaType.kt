package builtin.nodes.types

import TextRange
import TypeNode
import builtin.nodes.Parameter

data class LambdaType(
    val parameters: List<TypeNode>?,
    val returnType: TypeNode,
    override val isNullable: Boolean,
    override val pos: TextRange
): TypeNode(isNullable, pos)