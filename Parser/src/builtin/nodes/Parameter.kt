package builtin.nodes

import AstNode
import TextRange
import TypeNode

data class Parameter(
    val kind: VariableModifier?,
    val name: Identifier,
    val type: TypeNode,
    override val pos: TextRange
): AstNode(pos) {
    override fun toString(): String {
        val result = StringBuilder()

        if (kind != null) result.append("$kind ")
        result.append(name)
        result.append(": $type")

        return result.toString()
    }
}