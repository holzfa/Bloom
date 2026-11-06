package builtin.rules.declarations

import DeclarationNode
import Parser
import ParserReader
import ParserRule
import builtin.helpers.parseBase
import builtin.helpers.parseEnumMember
import builtin.helpers.parseIdentifier
import builtin.helpers.parseModifiers
import builtin.helpers.parseParametersDeclaration
import builtin.nodes.Identifier
import builtin.nodes.declarations.EnumDeclaration
import builtin.nodes.declarations.EnumMember
import builtin.tokentypes.KeywordTokenType
import builtin.tokentypes.SymbolTokenType
import builtin.tokentypes.VuraTokenType

class EnumRule(override val parser: Parser): ParserRule() {
    override fun tryDeclaration(reader: ParserReader): DeclarationNode? {
        val start = reader.currentPos
        val mods = parseModifiers(reader)
        if (reader.hope(KeywordTokenType.Enum) == null) return null

        val name = parseIdentifier(reader) ?: Identifier.empty
        val parameters = parseParametersDeclaration(reader)

        val base = if (reader.hope(SymbolTokenType.Colon) != null) parseBase(reader, parser)
                   else null

        val members = mutableListOf<EnumMember>()
        if (reader.hope(SymbolTokenType.BraceOpen) != null) {
            while (!reader.isAtEnd && reader.hope(SymbolTokenType.BraceClose) == null) {
                val member = parseEnumMember(reader, parser)
                if (member != null) members.add(member)

                if (reader.peek(1).type == VuraTokenType.Identifier) {
                    reader.expect(SymbolTokenType.Comma)
                } else {
                    reader.hope(SymbolTokenType.Comma)
                }
            }
        }

        return EnumDeclaration(mods, name, parameters, base, if (members.isEmpty()) null else members, reader.range(start))
    }
}