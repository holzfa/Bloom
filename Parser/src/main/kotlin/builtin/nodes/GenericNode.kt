package builtin.nodes

import AstNode
import TextRange
import TypeNode

data class GenericNode(
    val name: Identifier,
    val constraints: List<Constraint>,
    override val pos: TextRange
) : AstNode(pos)

interface Constraint
data class TypeConstraint(val type: TypeNode): Constraint
data class InstanceConstraint(val instanceType: InstanceType): Constraint
