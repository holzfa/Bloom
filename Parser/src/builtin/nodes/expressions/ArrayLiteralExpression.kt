package builtin.nodes.expressions

import ExpressionNode
import TextRange

data class ArrayLiteralExpression(
    val elements: List<ExpressionNode>,
    override val pos: TextRange
): ExpressionNode(pos)