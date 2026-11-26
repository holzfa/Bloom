package builtin.nodes

import AstNode
import TextRange
import TypeNode

data class GenericNode(
    val name: Identifier,
    val constraints: List<Constraint>,
    override val pos: TextRange
) : AstNode(pos) {
    override fun toString(): String = "$name" + if (constraints.isNotEmpty())
        if (constraints.size > 1) constraints.joinToString(prefix = "(", postfix = ")")
        else constraints.firstOrNull().toString()
    else ""
}

interface Constraint
data class TypeConstraint(val type: TypeNode): Constraint {
    override fun toString(): String = "$type"
}
data class InstanceConstraint(val instanceType: InstanceType): Constraint {
    override fun toString(): String = "$instanceType"
}
