package builtIn

import Lexer
import LexerReader
import LexerRule
import TextRange
import Token
import TokenType
import Vec2
import builtIn.tokentypes.doubleOps
import builtIn.tokentypes.singleOps
import builtIn.tokentypes.tripleOps
import isOp
import isPunctuation

internal class OperatorRule(override val lexer: Lexer) : LexerRule {
    override fun tryMatch(reader: LexerReader): Token? {
        var c = reader.peek()
        if (!c.isOp()) return null

        reader.advance()

        val start = reader.cursor
        val pos = Vec2(reader.line, reader.column)

        val chars = mutableListOf<Char>()
        while (!reader.isAtEnd) {
            if (c.isOp() && chars.size <= 3) {
                chars.add(c)
                c = reader.advance()
            } else {
                break
            }
        }

        for (i in 0..chars.size) reader.advance()

        val type: TokenType
        val text: String
        when (chars.size) {
            3 -> {
                type = tripleOps.getValue(Triple(chars[0], chars[1], chars[2]))
                text = chars.joinToString()
            }
            2 -> {
                type = doubleOps.getValue(Pair(chars[0], chars[1]))
                text = chars.joinToString()
            }
            else -> {
                type = singleOps.getValue(chars[0])
                text = chars[0].toString()
            }
        }

        return Token(type, text, TextRange.fromStart(pos, reader.cursor - start))
    }
}