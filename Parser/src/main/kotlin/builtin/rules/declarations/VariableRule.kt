package builtin.rules.declarations

import DeclarationNode
import Parser
import ParserReader
import ParserRule
import builtin.helpers.parseIdentifier
import builtin.helpers.parseModifiers
import builtin.helpers.parseType
import builtin.helpers.parseVariableMod
import builtin.nodes.Identifier
import builtin.nodes.declarations.PropertyDeclaration
import builtin.nodes.declarations.VariableDeclaration
import builtin.tokentypes.KeywordTokenType
import builtin.tokentypes.SymbolTokenType

class VariableRule(override val parser: Parser): ParserRule() {
    override fun tryDeclaration(reader: ParserReader): DeclarationNode? {
        val start = reader.currentPos
        val mods = parseModifiers(reader)
        val kind = parseVariableMod(reader) ?: return null

        val name = parseIdentifier(reader) ?: Identifier.empty
        val type = if (reader.hope(SymbolTokenType.Colon) != null) parseType(reader) else null
        val init = if (reader.hope(SymbolTokenType.Assign) != null) parser.requestExpression(reader) else null

        return VariableDeclaration(mods, kind, name, type, init, reader.range(start))
    }
}