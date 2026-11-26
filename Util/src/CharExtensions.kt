fun Char.isPunctuation(): Boolean {
    return punctuation.contains(this)
}

fun Char.isOp(): Boolean {
    return operators.contains(this)
}

private val punctuation = hashSetOf(
    '.', ',',
    '_',
    '(', ')', '{', '}', '[', ']',
    ';', ':'
)

private val operators = hashSetOf(
    '+', '-', '*', '/', '%',
    '&', '|', '^', '~',
    '!', '?',
    '=', '<', '>',
    '@', '#', '$',
    '.'
)