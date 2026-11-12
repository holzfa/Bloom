package builtin.nodes

import AstNode
import ExpressionNode
import TextRange

data class ImplementationNode(
    val type: Identifier,
    val parameters: List<ExpressionNode>?,
    override val pos: TextRange
): AstNode(pos)