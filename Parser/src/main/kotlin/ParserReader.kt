import builtin.tokentypes.SymbolTokenType
import builtin.tokentypes.VuraTokenType
import java.util.*

data class ParserReaderState(var cursor: Int, var rawCursor: Int): RollbackState

class ParserReader(private val rawTokens: List<Token>, val filePath: String, bag: MessageBag): MessageCollector<TextRange>(bag, "Parser"), Rollback<ParserReaderState> {
    var state = ParserReaderState(0, 0)

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
        return if (state.cursor > 0) {
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
    val isAtEnd: Boolean get() { return state.cursor >= tokens.size || isNext(DefaultTokenTypes.Eof) }

    /** Peeks to future tokens with the specified offset or 0 if unspecified */
    fun peek(offset: Int = 0): Token {
        val idx = state.cursor + offset
        return if (idx < tokens.size) tokens[idx] else tokens.lastOrNull() ?: Token.error
    }
    private fun peekRaw(offset: Int = 0): Token {
        val idx = state.rawCursor + offset
        return if (idx < rawTokens.size) rawTokens[idx] else rawTokens.lastOrNull() ?: Token.error
    }

    /** Returns the current token and *then* advances */
    fun advance(): Token {
        if (isAtEnd) return Token.error
        val token = tokens[state.cursor++]

        // Sync the raw state.cursor
        while (state.rawCursor < rawTokens.size && rawTokens[state.rawCursor] != token) {
            state.rawCursor++
        }
        if (state.rawCursor < rawTokens.size) { // Consume the matched token as well
            state.rawCursor++
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

    /** Checks for a sequence of token types at the current position, it does not mutate the readers state */
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

    override val states: Stack<ParserReaderState> = Stack()

    override fun save() {
        states.add(state.copy())
        bag.save()
    }

    override fun rollBack() {
        state = states.pop()
        bag.rollBack()
    }

    override fun commit() {
        states.pop()
        bag.commit()
    }
}

