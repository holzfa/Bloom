package builtin.rules.statements

import DeclarationNode
import Parser
import ParserReader
import ParserRule
import StatementNode
import builtin.helpers.parseAssignmentMod
import builtin.helpers.parseBases
import builtin.helpers.parseGenericDeclaration
import builtin.helpers.parseIdentifier
import builtin.helpers.parseInstanceBody
import builtin.helpers.parseModifiers
import builtin.helpers.parseParametersDeclaration
import builtin.nodes.Identifier
import builtin.nodes.declarations.ClassDeclaration
import builtin.nodes.declarations.StructDeclaration
import builtin.nodes.statements.AssignmentStatement
import builtin.tokentypes.KeywordTokenType

class AssignmentRule(override val parser: Parser): ParserRule() {
    override fun tryStatement(reader: ParserReader): StatementNode? {
        val start = reader.currentPos

        val operand = parser.requestExpression(reader)
        val mod = parseAssignmentMod(reader) ?: return null
        val value = parser.requestExpression(reader)

        return AssignmentStatement(operand, mod, value, reader.range(start))
    }
}