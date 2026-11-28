package builtin.rules

import Parser
import ParserRule
import builtin.rules.declarations.*
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
            ComplementObjectRule(it),
            EnumRule(it),
            ExtensionsRule(it),
            FunctionRule(it),
            PropertyRule(it),
            VariableDeclarartionRule(it),

            // The last choice as it would otherwise catch on any identifier
            ConstructorRule(it),

            /* STATEMENTS */
            VariableStatementRule(it),
            AssignmentRule(it),
            ForRule(it),
            WhileRule(it),
            ReturnRule(it),
            ExpressionStatementRule(it),

            /* EXPRESSIONS */
            MemberAccessRule(it),
            LiteralRule(it),
            ReturnRule(it),
            IfRule(it),
            MatchRule(it),
            NumberOfRule(it),
            ArrayLiteralRule(it),
            IndexRule(it),
            RangeRule(it),
            CallRule(it),
            IdentifierRule(it),
            BoxedRule(it)
        )
    }
}