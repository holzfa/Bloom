fun Char.isPunctuation(): Boolean {
    return punctuation.contains(this)
}

private val punctuation = hashSetOf(
    '.', ',', '(', ')', '{', '}', '[', ']', ';', ':'
)