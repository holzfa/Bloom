package builtIn

import LexerReader
import LexerRule
import Token

internal class NumberRule: LexerRule {
    override fun tryMatch(reader: LexerReader): Token? {
        if (!reader.peek().isDigit()) return null // Fast early return that skips variable instantiation for speed

        val chars = mutableListOf<Char>()

        var isLong = false
        var isFloat = false
        var isDouble = false

        while (!reader.isAtEnd && acceptedNumberChars.contains(reader.peek())) {
            val c = reader.advance()
        }
    }
}