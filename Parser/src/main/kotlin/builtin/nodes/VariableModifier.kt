package builtin.nodes

enum class VariableModifier {
    Const,
    Val,
    Var;

    override fun toString(): String = this.name.lowercase()
}