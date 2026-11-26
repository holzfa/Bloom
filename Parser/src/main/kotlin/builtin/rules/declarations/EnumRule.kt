package builtin.rules.declarations

import DeclarationNode
import DeclarationRule
import Parser
import ParserReader
import builtin.helpers.*
import builtin.nodes.Identifier
import builtin.nodes.declarations.EnumDeclaration
import builtin.tokentypes.KeywordTokenType
import builtin.tokentypes.SymbolTokenType

class EnumRule(override val parser: Parser): DeclarationRule {
    override fun tryDeclaration(reader: ParserReader): DeclarationNode? {
        val start = reader.currentPos
        val mods = parseModifiers(reader)
        if (reader.hope(KeywordTokenType.Enum) == null) return null

        val name = parseIdentifier(reader) ?: Identifier.empty
        val parameters = parseParameterList(reader)

        val base = if (reader.hope(SymbolTokenType.Colon) != null) parseBase(reader, parser)
                   else null

        val members = parseList(SymbolTokenType.SquareOpen, SymbolTokenType.SquareClose, reader) {
            parseEnumMember(reader, parser)
        }
        val declarations = parseInstanceBody(reader, parser)

        return EnumDeclaration(mods, name, parameters, base, members, declarations, reader.range(start))
    }
}