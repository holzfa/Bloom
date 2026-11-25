package builtin.rules.statements

import Parser
import ParserReader
import StatementNode
import StatementRule
import builtin.helpers.parseIdentifier
import builtin.helpers.parseType
import builtin.helpers.parseVariableMod
import builtin.nodes.Identifier
import builtin.nodes.statements.VariableStatement
import builtin.tokentypes.SymbolTokenType

class VariableStatementRule(override val parser: Parser): StatementRule {
    override fun tryStatement(reader: ParserReader): StatementNode? {
        val start = reader.currentPos
        val kind = parseVariableMod(reader) ?: return null

        val name = parseIdentifier(reader) ?: Identifier.empty
        val type = if (reader.hope(SymbolTokenType.Colon) != null) parseType(reader) else null
        val init = if (reader.hope(SymbolTokenType.Assign) != null) parser.requestExpression(reader) else null

        return VariableStatement(kind, name, type, init, reader.range(start))
    }
}