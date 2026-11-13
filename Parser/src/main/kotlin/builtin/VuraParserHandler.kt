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
import builtin.rules.declarations.PropertyDeclRule
import builtin.rules.declarations.StructRule
import builtin.rules.declarations.TraitRule
import builtin.rules.declarations.VariableRule
import builtin.rules.statements.AssignmentRule
import builtin.rules.statements.ExpressionRule
import builtin.rules.statements.ForRule
import builtin.rules.statements.ReturnRule
import builtin.rules.statements.WhileRule

object VuraParserHandler {
    fun register(lexer: Parser) {
        for (rule in rules(lexer)) {
            lexer.registerRule(rule)
        }
    }

    val rules: (parser: Parser) -> List<ParserRule> = {
        listOf(
            /* DECLARATIONS */
            ImportRule(it),
            ModuleRule(it),
            StructRule(it),
            ClassRule(it),
            ObjectRule(it),
            TraitRule(it),
            EnumRule(it),
            ExtensionsRule(it),
            FunctionRule(it),
            PropertyDeclRule(it),
            VariableRule(it),

            // The last choice as it would otherwise catch on any identifier
            ConstructorRule(it),

            /* STATEMENTS */
            AssignmentRule(it),
            ForRule(it),
            WhileRule(it),
            ReturnRule(it),
            VariableRule(it),
            ExpressionRule(it),
        )
    }
}