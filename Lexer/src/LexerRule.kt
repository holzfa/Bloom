interface LexerRule {
    val lexer: Lexer

    fun tryMatch(reader: LexerReader): Token?
}