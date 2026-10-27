class TokenReader(private val tokens: List<Token>, val filePath: String) {
    var cursor = 0
        private set

    val currentPos: Vec2
        get() = peek().pos.vec
    val nextPos: Vec2
        get() = peek(1).pos.vec

    fun range(start: Vec2, end: Vec2): TextRange {
        return TextRange(start, end, filePath)
    }

    val isAtEnd: Boolean get() { return cursor >= tokens.size }

    fun peek(offset: Int = 0): Token {
        val idx = cursor + offset
        return if (idx < tokens.size) tokens[idx] else tokens.lastOrNull() ?: Token.error
    }

    fun advance(): Token {
        if (isAtEnd) return Token.error
        return tokens[cursor++]
    }
}