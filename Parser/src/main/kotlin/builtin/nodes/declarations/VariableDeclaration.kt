package builtin.nodes.declarations

import DeclarationNode
import ExpressionNode
import TextRange
import builtin.nodes.Identifier
import builtin.nodes.Modifier
import builtin.nodes.VariableModifier
import java.util.*

data class VariableDeclaration(
    val modifiers: EnumSet<Modifier>,
    val type: VariableModifier,
    val name: Identifier,
    val initializer: ExpressionNode?,
    override val pos: TextRange
): DeclarationNode(pos)