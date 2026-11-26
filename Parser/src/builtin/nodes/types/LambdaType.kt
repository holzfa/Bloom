package builtin.nodes.types

import TextRange
import TypeNode

data class LambdaType(
    val parameters: List<TypeNode>,
    val returnType: TypeNode,
    override val isNullable: Boolean,
    override val pos: TextRange
): TypeNode(isNullable, pos) {
    override fun toString(): String = "(${parameters.joinToString()}) -> $returnType"
}