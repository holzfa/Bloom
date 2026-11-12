package builtin.rules.declarations

import DeclarationNode
import Parser
import ParserReader
import ParserRule
import builtin.helpers.parseBases
import builtin.helpers.parseBlock
import builtin.helpers.parseGenericDeclaration
import builtin.helpers.parseIdentifier
import builtin.helpers.parseInstanceBody
import builtin.helpers.parseModifiers
import builtin.helpers.parseParametersDeclaration
import builtin.helpers.parseType
import builtin.nodes.Identifier
import builtin.nodes.declarations.ConstructorDeclaration
import builtin.nodes.declarations.FunctionDeclaration
import builtin.tokentypes.KeywordTokenType
import builtin.tokentypes.SymbolTokenType

class ConstructorRule(override val parser: Parser): ParserRule() {
    override fun tryDeclaration(reader: ParserReader): DeclarationNode? {
        val start = reader.currentPos
        val mods = parseModifiers(reader)

        val name = parseIdentifier(reader) ?: return null
        val parameters = parseParametersDeclaration(reader)

        val body = parseBlock(reader, parser)

        return ConstructorDeclaration(mods, name, parameters, body, reader.range(start))
    }
}