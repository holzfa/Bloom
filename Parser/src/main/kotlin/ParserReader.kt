import builtin.tokentypes.KlarTokenType
import builtin.tokentypes.SymbolTokenType

class ParserReader(private val rawTokens: List<Token>, val filePath: String, bag: MessageBag): CompilerStage<TextRange>(bag, "Parser.Rule"), Rollback {
    var cursor = 0
        private set

    var rawCursor = 0
        private set

    private val filteredTypes = listOf<TokenType>(
        KlarTokenType.Whitespace,
        KlarTokenType.Comment,
        KlarTokenType.Documentation
    )
    private val tokens: List<Token> = rawTokens.filter { !filteredTypes.contains(it.type) }

    val currentPos: Vec2
        get() = peek().pos.startVec
    val currentRange: TextRange
        get() = range(peek().pos)

    fun range(start: Vec2, end: Vec2): TextRange {
        return TextRange(start, end, filePath)
    }
    fun range(start: Vec2): TextRange {
        if (cursor > 0) {
            return TextRange(start, peek(-1).pos.endVec, filePath)
        } else {
            return TextRange(start, peek().pos.endVec, filePath)
        }
    }
    fun range(pos: TextPos): TextRange {
        return TextRange(pos.startVec, pos.endVec, filePath)
    }

    val isAtEnd: Boolean get() { return cursor >= tokens.size }

    fun peek(offset: Int = 0): Token {
        val idx = cursor + offset
        return if (idx < tokens.size) tokens[idx] else tokens.lastOrNull() ?: Token.error
    }
    private fun peekRaw(offset: Int = 0): Token {
        val idx = rawCursor + offset
        return if (idx < rawTokens.size) rawTokens[idx] else rawTokens.lastOrNull() ?: Token.error
    }

    fun advance(): Token {
        if (isAtEnd) return Token.error
        val token = tokens[cursor++]

        // Sync the raw cursor
        while (rawCursor < rawTokens.size && rawTokens[rawCursor] != token) {
            rawCursor++
        }
        if (rawCursor < rawTokens.size) { // Consume the matched token as well
            rawCursor++
        }

        return token
    }

    fun expect(type: TokenType, errorMessage: String): Token? {
        val token = peek()
        if (token.type != type) {
            doError(errorMessage, range(token.pos))
            return null
        } else {
            advance()
            return token
        }
    }
    fun expect(type: TokenType): Token? {
        return expect(type, "Expected '${type.name}'")
    }

    fun hope(type: TokenType): Token? {
        val token = peek()
        if (token.type != type) {
            return null
        } else {
            advance()
            return token
        }
    }

    fun isTerminator(): Boolean {
        var offset = 0
        var token = peekRaw(offset)
        while (!isAtEnd && token.type == KlarTokenType.Comment || token.type == KlarTokenType.Documentation) {
            token = peekRaw(offset++)
        }

        return token.type == KlarTokenType.Whitespace && token.value.contains('\n')
                || token.type == SymbolTokenType.Semicolon || isAtEnd
    }

    fun expectTerminator() {
        if (!isTerminator()) {
            doError("Expected statement termination", currentRange)
        }
    }


    private var marked: Int = 0
    private var rawMarked: Int = 0
    override fun mark() {
        marked = cursor
        rawMarked = rawCursor
        bag.mark()
    }
    override fun reset() {
        cursor = marked
        rawCursor = rawMarked
        bag.reset()
    }
}