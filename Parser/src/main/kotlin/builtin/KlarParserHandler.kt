package builtin

import Parser
import ParserRule
import tree.AstNode

object KlarParserHandler {
    fun register(lexer: Parser) {
        for (rule in rules(lexer)) {
            lexer.registerRule(rule)
        }
    }

    val rules: (lexer: Parser) -> List<ParserRule<AstNode>> = {
        listOf(

        )
    }
}