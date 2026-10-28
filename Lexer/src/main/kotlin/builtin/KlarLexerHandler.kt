package builtin

import Lexer
import LexerRule

object KlarLexerHandler {
    fun register(lexer: Lexer) {
        for (rule in rules(lexer)) {
            lexer.registerRule(rule)
        }
    }

    val rules: (lexer: Lexer) -> List<LexerRule> = {
        listOf(
            WhitespaceRule(it),
            CommentRule(it),
            IdentifierOrKeywordRule(it),
            SymbolRule(it),
            NumberRule(it),
            StringOrCharRule(it),
        )
    }
}