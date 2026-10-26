package builtIn

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
            PunctuationRule(it),
            NumberRule(it),
            StringOrCharRule(it),
            IdentifierOrKeywordRule(it),
        )
    }
}