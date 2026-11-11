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
import builtin.nodes.declarations.FunctionDeclaration
import builtin.tokentypes.KeywordTokenType
import builtin.tokentypes.SymbolTokenType

class FunctionRule(override val parser: Parser): ParserRule() {
    override fun tryDeclaration(reader: ParserReader): DeclarationNode? {
        val start = reader.currentPos
        val mods = parseModifiers(reader)
        reader.hope(KeywordTokenType.Function) ?: return null

        val name = parseIdentifier(reader) ?: Identifier.empty
        val generics = parseGenericDeclaration(reader)
        val parameters = parseParametersDeclaration(reader)

        val returnType = if (reader.hope(SymbolTokenType.Arrow) != null) parseType(reader) else null

        val body = parseBlock(reader, parser)

        return FunctionDeclaration(mods, name, generics, parameters, returnType, body, reader.range(start))
    }
}