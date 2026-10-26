package builtIn

import Lexer
import LexerReader
import LexerRule
import Token
import Vec2
import isPunctuation

internal class PunctuationRule(override val lexer: Lexer): LexerRule {
    override fun tryMatch(reader: LexerReader): Token? {
        val c = reader.peek()
        if (!c.isPunctuation()) return null

        val start = reader.cursor
        val pos = Vec2(reader.line, reader.column)

        reader.advance()

        val type = punctuation.getValue(c)
        return Token(type, c.toString(), TextRange.fromStart(pos, reader.cursor - start))
    }
}