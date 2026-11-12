package builtin.nodes.declarations

import DeclarationNode
import ExpressionNode
import TextRange
import TypeNode
import builtin.nodes.Identifier
import builtin.nodes.Modifier
import builtin.nodes.VariableModifier
import java.util.*

data class VariableDeclaration(
    val modifiers: Set<Modifier>,
    val kind: VariableModifier,
    val name: Identifier,
    val type: TypeNode?,
    val initializer: ExpressionNode?,
    override val pos: TextRange
): DeclarationNode(pos)