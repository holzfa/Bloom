package builtin.nodes

data class Identifier(val parts: List<String>) {
    override fun toString(): String {
        return parts.joinToString(separator = ".")
    }
    companion object {
        val empty = Identifier(listOf())
    }
}