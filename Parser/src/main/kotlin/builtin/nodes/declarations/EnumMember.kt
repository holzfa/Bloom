package builtin.nodes.declarations

import AstNode
import ExpressionNode
import TextRange
import builtin.nodes.Identifier

data class EnumMember(
    val name: Identifier,
    val values: List<ExpressionNode>?,
    val initializer: ExpressionNode?,
    override val pos: TextRange
): AstNode(pos)