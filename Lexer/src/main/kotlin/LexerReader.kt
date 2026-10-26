class LexerReader(private val input: String) {
    var cursor = 0
        private set
    var line = 1
        private set
    var column = 1
        private set

    private var onNewline: (c: Char) -> Unit = { }

    val isAtEnd: Boolean get() { return cursor >= input.length }

    fun registerNewlineCallback(callback: (c: Char) -> Unit) {
        onNewline = callback
    }

    fun peek(offset: Int = 0): Char {
        val idx = cursor + offset
        return if (idx < input.length) input[idx] else '\u0000'
    }

    fun advance(): Char {
        val c = input[cursor++]
        if (c == '\n') {
            line++
            column = 1

            onNewline(c)
        } else {
            column++
        }
        return c
    }

    fun substring(start: Int, end: Int): String = input.substring(start, end)
}