package builtin.nodes.expressions

import ExpressionNode
import TextRange
import builtin.nodes.Block

data class MatchExpression(
    val target: ExpressionNode?,
    val members: List<MatchMember>,
    val default: Block?,
    override val pos: TextRange
): ExpressionNode(pos)

data class MatchMember(
    val pattern: ExpressionNode?,
    val body: Block?,
    override val pos: TextRange
): ExpressionNode(pos)