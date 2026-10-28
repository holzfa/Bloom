class LexerReader(private val input: String, bag: MessageBag): CompilerStage<TextRange>(bag, "Lexer.Rule") {
    var cursor = 0
        private set
    var line = 1
        private set
    var column = 1
        private set

    val isAtEnd: Boolean get() { return cursor >= input.length }

    fun peek(offset: Int = 0): Char {
        val idx = cursor + offset
        return if (idx < input.length) input[idx] else '\u0000'
    }

    fun advance(): Char {
        if (isAtEnd) return '\u0000'

        val c = input[cursor++]
        if (c == '\n') {
            line++
            column = 1
        } else {
            column++
        }
        return c
    }

    fun substring(start: Int, end: Int): String = input.substring(start, end)
}