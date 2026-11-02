package builtin.nodes

data class Identifier(val parts: List<String>) {
    companion object {
        val empty = Identifier(listOf())
    }
}