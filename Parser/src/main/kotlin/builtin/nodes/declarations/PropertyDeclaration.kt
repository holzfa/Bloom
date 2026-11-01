package builtin.nodes.declarations

import DeclarationNode
import ExpressionNode
import TextRange
import builtin.nodes.Identifier
import builtin.nodes.Modifier
import builtin.nodes.PropertyAccessor
import java.util.*

data class PropertyDeclaration(
    val modifiers: EnumSet<Modifier>,
    val name: Identifier,
    val getter: PropertyAccessor?,
    val setter: PropertyAccessor?,
    val initializer: ExpressionNode?,
    override val pos: TextRange
): DeclarationNode(pos)