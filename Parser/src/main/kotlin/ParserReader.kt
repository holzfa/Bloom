import builtIn.tokentypes.KlarTokenType

class ParserReader(rawTokens: List<Token>, val filePath: String, bag: MessageBag): CompilerStage<TextRange>(bag, "Parser") {
    var cursor = 0
        private set

    private val tokens: List<Token> = rawTokens.filter { it.type != KlarTokenType.Whitespace }

    val currentPos: Vec2
        get() = peek().pos.startVec
    fun range(start: Vec2, end: Vec2): TextRange {
        return TextRange(start, end, filePath)
    }
    fun range(start: Vec2): TextRange {
        return TextRange(start, peek().pos.endVec, filePath)
    }
    fun range(pos: TextPos): TextRange {
        return TextRange(pos.startVec, pos.endVec, filePath)
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

    fun expect(type: TokenType, errorMessage: String): Token {
        val token = peek()
        if (token.type != type) {
            doError(errorMessage, range(token.pos))
            return Token.error
        } else {
            advance()
            return token
        }
    }
    fun expect(type: TokenType): Token {
        return expect(type, "Expected '${type.name}'")
    }

    fun hope(type: TokenType): Token {
        val token = peek()
        if (token.type != type) {
            return Token.error
        } else {
            advance()
            return token
        }
    }
}