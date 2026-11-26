package builtin.nodes.expressions

import ExpressionNode
import TextRange
import builtin.nodes.Block
import builtin.nodes.Identifier

data class CallExpression(
    val callee: ExpressionNode,
    val generics: List<Identifier>?,
    val parameters: List<ExpressionNode>?,
    val body: Block?,
    override val pos: TextRange
): ExpressionNode(pos)
