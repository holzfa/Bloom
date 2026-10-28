package builtin

import Parser
import ParserRule

object KlarParserHandler {
    fun register(lexer: Parser) {
        for (rule in rules(lexer)) {
            lexer.registerRule(rule)
        }
    }

    val rules: (parser: Parser) -> List<ParserRule<*>> = {
        listOf<ParserRule<*>>(
            ImportRule(it),
            ModuleStatementRule(it),
        )
    }
}