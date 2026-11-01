package builtin.nodes.declarations

import AstNode
import ExpressionNode
import TextRange

data class EnumMember(
    val name: String,
    val values: List<ExpressionNode>?,
    val initializer: ExpressionNode?,
    override val pos: TextRange
): AstNode(pos)