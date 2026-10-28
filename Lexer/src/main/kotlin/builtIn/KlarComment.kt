package builtIn

import Lexer
import LexerReader
import LexerRule
import TextPos
import Token
import Vec2
import builtIn.tokentypes.KlarTokenType

class CommentRule(override val lexer: Lexer) : LexerRule {
    override fun tryMatch(reader: LexerReader): Token? {
        if (reader.peek() != '/') return null

        val isMultiDoc = reader.peek(1) == '/' && reader.peek(2) == '*'
        val isDoc = reader.peek(1) == '/' && reader.peek(2) == '/'
        val isLine = reader.peek(1) == '/'
        val isMultiline = reader.peek(1) == '*'
        if (!isLine && !isMultiline) return null

        val start = reader.cursor
        val pos = Vec2(reader.line, reader.column)

        val prefixLength = if (isMultiDoc || isDoc) 3 else 2
        for (i in 1..prefixLength) reader.advance()

        if (isMultiline || isMultiDoc) {
            var closed = false
            while (!reader.isAtEnd) {
                if (isMultiDoc && reader.peek() == '*' && reader.peek(1) == '/' && reader.peek(2) == '/') {
                    for (i in 1..3) reader.advance()
                    closed = true
                    break
                }
                if (isMultiline && reader.peek() == '*' && reader.peek(1) == '/') {
                    for (i in 1..2) reader.advance()
                    closed = true
                    break
                }
                reader.advance()
            }

            if (!closed) {
                lexer.doError("Comment was cut off by end of file", TextPos.fromStart(pos, reader.cursor - start))
            }
        } else {
            // The newline is left for the whitespace rule
            while (!reader.isAtEnd && reader.peek() != '\n') reader.advance()
        }

        val type = if (isDoc || isMultiDoc) KlarTokenType.Documentation
        else KlarTokenType.Comment

        return Token(
            type,
            reader.substring(start, reader.cursor),
            TextPos.fromStart(pos, reader.cursor - start)
        )
    }
}