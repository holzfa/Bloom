package builtIn

import Lexer
import LexerReader
import LexerRule
import TextRange
import Token
import Vec2

internal class IdentifierOrKeywordRule(override val lexer: Lexer) : LexerRule {
    override fun tryMatch(reader: LexerReader): Token? {
        if (!reader.peek().isLetter() || reader.peek() == '_') return null

        val start = reader.cursor
        val pos = Vec2(reader.line, reader.column)

        while (!reader.isAtEnd) {
            if (reader.peek().isLetterOrDigit() || reader.peek() == '_') reader.advance()
            else break
        }

        val text = reader.substring(start, reader.cursor)
        val type = if (keywords.containsKey(text)) keywords.getValue(text) else KlarTokenType.Identifier

        return Token(type, text, TextRange.fromStart(pos, reader.cursor - start))
    }
}