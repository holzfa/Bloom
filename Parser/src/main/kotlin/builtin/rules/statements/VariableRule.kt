package builtin.rules.statements

import DeclarationNode
import Parser
import ParserReader
import ParserRule
import StatementNode
import builtin.helpers.parseIdentifier
import builtin.helpers.parseModifiers
import builtin.helpers.parseType
import builtin.helpers.parseVariableMod
import builtin.nodes.Identifier
import builtin.nodes.declarations.PropertyDeclaration
import builtin.nodes.declarations.VariableDeclaration
import builtin.nodes.statements.VariableStatement
import builtin.tokentypes.KeywordTokenType
import builtin.tokentypes.SymbolTokenType

class VariableRule(override val parser: Parser): ParserRule() {
    override fun tryStatement(reader: ParserReader): StatementNode? {
        val start = reader.currentPos
        val kind = parseVariableMod(reader) ?: return null

        val name = parseIdentifier(reader) ?: Identifier.empty
        val type = if (reader.hope(SymbolTokenType.Colon) != null) parseType(reader) else null
        val init = if (reader.hope(SymbolTokenType.Assign) != null) parser.requestExpression(reader) else null

        return VariableStatement(kind, name, type, init, reader.range(start))
    }
}