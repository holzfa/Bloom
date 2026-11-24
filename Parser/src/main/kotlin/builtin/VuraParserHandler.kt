package builtin

import Parser
import ParserRule
import builtin.rules.declarations.*
import builtin.rules.declarations.VariableRule
import builtin.rules.expressions.*
import builtin.rules.statements.*

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
            PropertyRule(it),
            VariableRule(it),

            // The last choice as it would otherwise catch on any identifier
            ConstructorRule(it),

            /* STATEMENTS */
            AssignmentRule(it),
            ForRule(it),
            WhileRule(it),
            ReturnRule(it),
            VariableRule(it),
            ExpressionStatementRule(it),

            /* EXPRESSIONS */
            LiteralRule(it),
            ReturnRule(it),
            IfRule(it),
            NumberOfRule(it),
            IndexRule(it),
            RangeRule(it),
            CallRule(it),
            IdentifierRule(it),
        )
    }
}