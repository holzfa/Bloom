data class Token(
    val type: TokenType,
    val value: String,
    val pos: TextPos
) {
    override fun toString(): String {
        return if (value.any()) {
            "${type.name}: '${value.replace("\n", "\\n")}' - at $pos"
        } else {
            type.name
        }
    }

    companion object {
        val error = Token(DefaultTokenTypes.Error, "", TextPos.unknown)
    }
}