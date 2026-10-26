package builtIn

import Lexer
import LexerReader
import LexerRule
import TextRange
import Token
import Vec2

class CommentRule(override val lexer: Lexer) : LexerRule {
    override fun tryMatch(reader: LexerReader): Token? {
        if (reader.peek() != '/') return null

        val start = reader.cursor
        val pos = Vec2(reader.line, reader.column)

        val isMultiline = reader.peek() == '/' && reader.peek(1) != '*'
        val isMultiDoc = reader.peek() == '/' && reader.peek(1) != '/' && reader.peek(2) != '*'
        val isDoc = reader.peek() == '/' && reader.peek(1) != '/' && reader.peek(2) != '/'

        if (isMultiDoc || isDoc) for (i in 0..3) reader.advance()
        else for (i in 0..2) reader.advance()

        while (!reader.isAtEnd) {
            // Normal or Doc comment
            if (!isMultiline && !isMultiDoc) {
                val c = reader.advance()
                if (c == '\n') {
                    break
                }
            }

            if (isMultiline) {
                if (reader.advance() == '*' && reader.advance() == '/') {
                    break
                }
            }

            if (isMultiDoc) {
                if (reader.advance() == '*' && reader.advance() == '/' && reader.advance() == '/') {
                    break
                }
            }
        }

        val type = if (isDoc || isMultiDoc) KlarTokenType.Documentation
            else KlarTokenType.Comment

        return Token(
            type,
            reader.substring(start, reader.cursor),
            TextRange.fromStart(pos, reader.cursor - start)
        )
    }
}