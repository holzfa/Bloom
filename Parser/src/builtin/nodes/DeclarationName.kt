package builtin.nodes

import TextRange
import TypeNode

interface DeclarationName { val pos: TextRange? }

data class ScalarName(
    val name: String,
    override val pos: TextRange?
) : DeclarationName {
    override fun toString(): String = name
}

data class ExtensionName(
    val type: TypeNode,
    val extension: DeclarationName?,
    override val pos: TextRange?
) : DeclarationName {
    override fun toString(): String = type.toString()
}

data class OperatorName(
    val op: BinaryOperator,
    override val pos: TextRange?
) : DeclarationName {
    override fun toString(): String = op.toString()
}
