package builtin

import Lexer
import LexerReader
import LexerRule
import TextPos
import Token
import Vec2
import builtin.tokentypes.VuraTokenType

class WhitespaceRule(override val lexer: Lexer) : LexerRule {
    override fun tryMatch(reader: LexerReader): Token? {
        if (!reader.peek().isWhitespace()) return null
        val start = reader.cursor
        val pos = Vec2(reader.line, reader.column)

        while (!reader.isAtEnd) {
            if (reader.peek().isWhitespace()) reader.advance()
            else break
        }

        return Token(
            VuraTokenType.Whitespace,
            reader.substring(start, reader.cursor),
            TextPos.fromStart(pos, reader.cursor - start)
        )
    }
}