import builtin.tokentypes.SymbolTokenType
import builtin.tokentypes.VuraTokenType

class ParserReader(private val rawTokens: List<Token>, val filePath: String, bag: MessageBag): MessageCollector<TextRange>(bag, "Parser"), Rollback {
    var cursor = 0
        private set

    private var rawCursor = 0

    private val filteredTypes = listOf<TokenType>(
        VuraTokenType.Whitespace,
        VuraTokenType.Comment,
        VuraTokenType.Documentation
    )

    private val tokens: List<Token> = rawTokens.filter { !filteredTypes.contains(it.type) }

    /** Gets the current tokens start position */
    val currentPos: Vec2
        get() = peek().pos.startVec

    /** Creates a range from the specified start and end positions */
    fun range(start: Vec2, end: Vec2): TextRange {
        return TextRange(start, end, filePath)
    }

    /** Creates a range from the specified start and the end of the current token */
    fun range(start: Vec2): TextRange {
        return if (cursor > 0) {
            TextRange(start, peek(-1).pos.endVec, filePath)
        } else {
            TextRange(start, peek().pos.endVec, filePath)
        }
    }

    /** Creates a range from a single tokens position */
    fun range(pos: TextPos): TextRange {
        return TextRange(pos.startVec, pos.endVec, filePath)
    }

    /** Checks if the next token is Eof or if are no more tokens left */
    val isAtEnd: Boolean get() { return cursor >= tokens.size || isNext(DefaultTokenTypes.Eof) }

    /** Peeks to future tokens with the specified offset or 0 if unspecified */
    fun peek(offset: Int = 0): Token {
        val idx = cursor + offset
        return if (idx < tokens.size) tokens[idx] else tokens.lastOrNull() ?: Token.error
    }
    private fun peekRaw(offset: Int = 0): Token {
        val idx = rawCursor + offset
        return if (idx < rawTokens.size) rawTokens[idx] else rawTokens.lastOrNull() ?: Token.error
    }

    /** Returns the current token and *then* advances */
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

    /** Expects the current token to be of the specified type and consumes it, otherwise it errors out with the specified message. */
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

    /** Expects the current token to be of the specified type and consumes it, otherwise it errors out. */
    fun expect(type: TokenType): Token? {
        return expect(type, "Expected '${type.name}'")
    }

    /** If the current token is of the specified type, it gets consumed and returned */
    fun hope(type: TokenType): Token? {
        return if (peek().type != type) {
            null
        } else {
            advance()
        }
    }

    /** Skips the current token and errors out an error */
    fun skip() {
        val token = advance()
        doError("Skipped token: ${token.type}", range(token.pos))
    }

    /** Checks for a sequence of token types at the current position */
    fun hasSequence(vararg types: TokenType): Boolean {
        for (idx in types.indices) {
            if (peek(idx).type != types[idx]) return false
        }; return true
    }

    /** Checks if the next token is of the given type */
    fun isNext(type: TokenType, offset: Int = 0): Boolean = peek(offset).type == type

    /** Checks if the next token is a statement terminator('\n', ';') */
    fun isTerminator(): Boolean {
        hasSequence()
        var offset = 0
        var token = peekRaw(offset)
        while (!isAtEnd && token.type == VuraTokenType.Comment || token.type == VuraTokenType.Documentation) {
            token = peekRaw(offset++)
        }

        return token.type == VuraTokenType.Whitespace && token.value.contains('\n')
                || token.type == SymbolTokenType.Semicolon || isAtEnd
    }

    /** Checks for a statement terminator and errors out if none is found */
    fun expectTerminator() {
        if (!isTerminator()) {
            doError("Expected statement termination", range(currentPos))
        }
    }


    private var marked: Int = 0
    private var rawMarked: Int = 0

    /** Creates a restore point that can be restored using ´reset´ */
    override fun mark() {
        marked = cursor
        rawMarked = rawCursor
        bag.mark()
    }

    /** Resets the restore point that was created using ´mark´ */
    override fun reset() {
        cursor = marked
        rawCursor = rawMarked
        bag.reset()
    }
}