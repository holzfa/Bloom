class Lexer(bag: MessageBag = MessageBag()) : CompilerStage(bag, "Lexer") {
    private val rules = mutableListOf<LexerRule>()

    fun registerRule(rule: LexerRule) {
        rules.add(rule)
    }

    fun tokenize(source: String): List<Token> {
        val tokens = mutableListOf<Token>()
        val reader = LexerReader(source)

        while (!reader.isAtEnd) {
            var matched = false

            for (rule in rules) {
                val token = rule.tryMatch(reader)
                if (token != null) {
                    matched = true
                    tokens.add(token)
                    break
                }
            }

            if (!matched) {
                val range = TextRange(reader.line, reader.column, 1)
                val badChar = reader.advance()
                doError("Unknown character '$badChar'", range)
                tokens.add(Token(DefaultTokenTypes.Unknown, badChar.toString(), range))
            }
        }

        tokens.add(Token(DefaultTokenTypes.Eof, "", TextRange(reader.line, reader.column, 0)))
        return tokens
    }
}