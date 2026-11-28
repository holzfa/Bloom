package builtin.nodes

data class Identifier(val value: String) {
    override fun toString(): String = value

    companion object {
        val empty = Identifier("")
    }
}