package builtin.nodes.expressions

import ExpressionNode
import TextRange
import builtin.nodes.Identifier

data class IdentifierExpression(
    val identifier: Identifier,
    override val pos: TextRange
): ExpressionNode(pos)
