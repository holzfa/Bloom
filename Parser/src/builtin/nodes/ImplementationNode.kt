package builtin.nodes

import AstNode
import ExpressionNode
import TextRange
import TypeNode

data class ImplementationNode(
    val type: TypeNode,
    val parameters: List<ExpressionNode>?,
    override val pos: TextRange
): AstNode(pos)