package builtin

import Lexer
import LexerReader
import LexerRule
import TextPos
import Token
import TokenType
import Vec2
import builtin.tokentypes.LiteralTokenType

private val hexChars = charArrayOf('a', 'b', 'c', 'd', 'e', 'f', 'A', 'B', 'C', 'D', 'E', 'F')

internal class NumberRule(override val lexer: Lexer) : LexerRule {
    override fun tryMatch(reader: LexerReader): Token? {
        if (!reader.peek().isDigit()) return null // Fast early return that skips variable instantiation for speed

        val start = reader.cursor
        val pos = Vec2(reader.line, reader.column)

        var isFloat = false
        val isHex = reader.peek() == '0' && reader.peek(1) == 'x'
        val isBin = reader.peek() == '0' && reader.peek(1) == 'b'

        if (isHex || isBin) for (i in 1..2) reader.advance()

        while (!reader.isAtEnd) {
            val c = reader.peek()

            if (c == '_' && reader.peek(1).isDigit()) { // Handle '_' separation for big numbers
                reader.advance()
            } else if (c.isDigit() && !isHex && !isBin) { // Handle normal digits
                reader.advance()
            } else if (!isFloat && c == '.' && reader.peek(1).isDigit()) { // Handle the single '.' for float/double
                isFloat = true
                reader.advance()
            } else if (isHex && (hexChars.contains(c) || c.isDigit())) { // Handle hexadecimal
                reader.advance()
            } else if (isBin && c.isDigit() && c.digitToInt() >= 0 && c.digitToInt() < 2) { // Handle binary
                reader.advance()
            } else {
                break
            }
        }

        val endChar = reader.peek()
        val type: TokenType = when (endChar) {
            'd', 'D' -> {
                reader.advance()
                LiteralTokenType.Double
            }

            'l', 'L' -> {
                reader.advance()
                LiteralTokenType.Long
            }

            else -> {
                if (isFloat) LiteralTokenType.Float
                else LiteralTokenType.Int
            }
        }

        val end = when (type) {
            LiteralTokenType.Double, LiteralTokenType.Long -> reader.cursor - 1
            else -> reader.cursor
        }
        return Token(type, reader.substring(start, end), TextPos.fromStart(pos, reader.cursor - start))
    }
}

internal class StringOrCharRule(override val lexer: Lexer) : LexerRule {
    override fun tryMatch(reader: LexerReader): Token? {
        if (!arrayOf('"', '\'').contains(reader.peek())) return null
        val quoteChar = reader.peek()

        val isMultiline = quoteChar == '"' && reader.peek(1) == '"' && reader.peek(2) == '"'
        val quoteLength = if (isMultiline) 3 else 1
        val type = if (quoteChar == '\'') LiteralTokenType.Char else LiteralTokenType.String

        val start = reader.cursor
        val pos = Vec2(reader.line, reader.column)
        for (i in 1..quoteLength) reader.advance()

        while (!reader.isAtEnd) {
            val c = reader.peek()

            if (isMultiline) {
                if (c == '"' && reader.peek(1) == '"' && reader.peek(2) == '"') {
                    val text = reader.substring(start + 3, reader.cursor) // Get the content of the string without quotes
                    for (i in 1..quoteLength) reader.advance()
                    return Token(type, text, TextPos.fromStart(pos, reader.cursor - start))
                }
            } else {
                if (c == quoteChar) {
                    val text = reader.substring(start + 1, reader.cursor)
                    reader.advance()
                    return Token(type, text, TextPos.fromStart(pos, reader.cursor - start))
                }

                if (c == '\n') {
                    // The newline is left for the whitespace rule
                    val range = TextPos.fromStart(pos, reader.cursor - start)
                    lexer.doError("String/Char was cut off by end of line", range)
                    return Token(type, reader.substring(start + quoteLength, reader.cursor), range)
                }
            }

            if (c == '\\') {
                reader.advance()
                if (!reader.isAtEnd) reader.advance()
            } else {
                reader.advance()
            }
        }

        val range = TextPos.fromStart(pos, reader.cursor - start)
        lexer.doError("String/Char was cut off by end of file", range)
        return Token(type, reader.substring(start + quoteLength, reader.cursor), range)
    }
}