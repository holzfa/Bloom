package builtin.nodes.declarations

import AstNode
import ExpressionNode
import TextRange
import builtin.nodes.Identifier

data class ImplementationNode(
    val type: Identifier,
    val parameters: List<ExpressionNode>?,
    override val pos: TextRange
): AstNode(pos)