package builtin.rules.statements

import Parser
import ParserReader
import ParserRule
import StatementNode
import builtin.helpers.parseAssignmentMod
import builtin.nodes.statements.AssignmentStatement

class AssignmentRule(override val parser: Parser): ParserRule() {
    override fun tryStatement(reader: ParserReader): StatementNode? {
        val start = reader.currentPos

        val operand = parser.requestExpression(reader)
        val mod = parseAssignmentMod(reader) ?: return null
        val value = parser.requestExpression(reader)

        return AssignmentStatement(operand, mod, value, reader.range(start))
    }
}