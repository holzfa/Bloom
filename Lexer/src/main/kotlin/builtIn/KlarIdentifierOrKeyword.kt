package builtIn

import Lexer
import LexerReader
import LexerRule
import Token
import Vec2

internal class IdentifierRule(override val lexer: Lexer): LexerRule {
    override fun tryMatch(reader: LexerReader): Token? {
        if (!reader.peek().isLetter()) return null

        val start = reader.cursor
        val pos = Vec2(reader.line, reader.column)

        while (!reader.isAtEnd) {
            if (reader.peek().isLetter()) reader.advance()
            else break
        }

        val text = reader.substring(start, reader.cursor)
        val type = if (keywords.containsKey(text)) keywords.getValue(text) else KlarTokenType.Identifier

        return Token(type, text, TextRange.fromStart(pos, reader.cursor - start))
    }
}