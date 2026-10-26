interface LexerRule {
    fun tryMatch(reader: LexerReader): Token?
}