package builtin.nodes.statements

import ExpressionNode
import StatementNode
import TextRange
import TypeNode
import builtin.nodes.Identifier
import builtin.nodes.VariableModifier

data class VariableStatement(
    val mod: VariableModifier,
    val name: Identifier,
    val type: TypeNode,
    val initialValue: ExpressionNode,
    override val pos: TextRange
): StatementNode(pos)