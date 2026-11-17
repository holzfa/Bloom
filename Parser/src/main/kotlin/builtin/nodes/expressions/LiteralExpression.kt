package builtin.nodes.expressions

import ExpressionNode
import TextRange
import builtin.tokentypes.LiteralTokenType

data class LiteralExpression(
    val type: LiteralTokenType,
    val textRepresentation: String,
    override val pos: TextRange
): ExpressionNode(pos)
