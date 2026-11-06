package builtin

import Parser
import ParserRule
import builtin.rules.declarations.ClassRule
import builtin.rules.declarations.EnumRule
import builtin.rules.declarations.ImportRule
import builtin.rules.declarations.ModuleRule
import builtin.rules.declarations.ObjectRule
import builtin.rules.declarations.StructRule
import builtin.rules.declarations.TraitRule

object VuraParserHandler {
    fun register(lexer: Parser) {
        for (rule in rules(lexer)) {
            lexer.registerRule(rule)
        }
    }

    val rules: (parser: Parser) -> List<ParserRule> = {
        listOf(
            ImportRule(it),
            ModuleRule(it),
            StructRule(it),
            ClassRule(it),
            ObjectRule(it),
            TraitRule(it),
            EnumRule(it),
        )
    }
}