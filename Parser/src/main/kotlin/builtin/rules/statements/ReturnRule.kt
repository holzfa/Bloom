package builtin.rules.statements

import Parser
import ParserReader
import ParserRule
import StatementNode
import builtin.nodes.statements.AssignmentStatement
import builtin.nodes.statements.ReturnStatement
import builtin.tokentypes.KeywordTokenType

class ReturnRule(override val parser: Parser): ParserRule() {
    override fun tryStatement(reader: ParserReader): StatementNode? {
        val start = reader.currentPos

        if (reader.hope(KeywordTokenType.Return) == null) return null
        val expr = parser.requestExpression(reader)

        return ReturnStatement(expr, reader.range(start))
    }
}