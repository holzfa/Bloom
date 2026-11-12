package builtin

import Parser
import ParserRule
import builtin.rules.declarations.ClassRule
import builtin.rules.declarations.ConstructorRule
import builtin.rules.declarations.EnumRule
import builtin.rules.declarations.ExtensionsRule
import builtin.rules.declarations.FunctionRule
import builtin.rules.declarations.ImportRule
import builtin.rules.declarations.ModuleRule
import builtin.rules.declarations.ObjectRule
import builtin.rules.declarations.PropertyRule
import builtin.rules.declarations.StructRule
import builtin.rules.declarations.TraitRule
import builtin.rules.declarations.VariableRule

object VuraParserHandler {
    fun register(lexer: Parser) {
        for (rule in rules(lexer)) {
            lexer.registerRule(rule)
        }
    }

    val rules: (parser: Parser) -> List<ParserRule> = {
        listOf(
            // These all use keywords so the order does not matter
            ImportRule(it),
            ModuleRule(it),
            StructRule(it),
            ClassRule(it),
            ObjectRule(it),
            TraitRule(it),
            EnumRule(it),
            ExtensionsRule(it),
            FunctionRule(it),
            PropertyRule(it),
            VariableRule(it),

            // The last choice as it would otherwise catch on any identifier
            ConstructorRule(it),
        )
    }
}