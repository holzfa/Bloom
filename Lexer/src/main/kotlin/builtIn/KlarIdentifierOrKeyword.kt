package builtIn

import Lexer
import LexerReader
import LexerRule
import TextPos
import Token
import Vec2
import builtIn.tokentypes.KlarTokenType
import builtIn.tokentypes.keywords

internal class IdentifierOrKeywordRule(override val lexer: Lexer) : LexerRule {
    override fun tryMatch(reader: LexerReader): Token? {
        val first = reader.peek()
        if (!first.isLetter() && first != '_') return null

        // A lone '_' is the Underscore symbol
        val next = reader.peek(1)
        if (first == '_' && !next.isLetterOrDigit() && next != '_') return null

        val start = reader.cursor
        val pos = Vec2(reader.line, reader.column)

        while (!reader.isAtEnd) {
            if (reader.peek().isLetterOrDigit() || reader.peek() == '_') reader.advance()
            else break
        }

        val text = reader.substring(start, reader.cursor)
        val type = if (keywords.containsKey(text)) keywords.getValue(text) else KlarTokenType.Identifier

        return Token(type, text, TextPos.fromStart(pos, reader.cursor - start))
    }
}