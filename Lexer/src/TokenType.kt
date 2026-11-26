interface TokenType {
    val name: String
}

enum class DefaultTokenTypes(val const: String? = null): TokenType {
    Error,
    Unknown,
    Eof
}