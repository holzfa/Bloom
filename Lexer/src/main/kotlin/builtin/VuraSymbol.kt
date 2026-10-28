package builtin

import Lexer
import LexerReader
import LexerRule
import TextPos
import Token
import Vec2
import builtin.tokentypes.symbols

internal class SymbolRule(override val lexer: Lexer) : LexerRule {
    override fun tryMatch(reader: LexerReader): Token? {
        // Longest match first, only the matched length is consumed
        for (length in 3 downTo 1) {
            val text = buildString {
                for (i in 0 until length) append(reader.peek(i))
            }

            val type = symbols[text] ?: continue

            val pos = Vec2(reader.line, reader.column)
            for (i in 1..length) reader.advance()

            return Token(type, text, TextPos.fromStart(pos, length))
        }

        return null
    }
}
