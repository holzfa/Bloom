package builtin.rules.declarations

import DeclarationNode
import DeclarationRule
import Parser
import ParserReader
import builtin.helpers.*
import builtin.nodes.Identifier
import builtin.nodes.declarations.StructDeclaration
import builtin.tokentypes.KeywordTokenType

class StructRule(override val parser: Parser): DeclarationRule {
    override fun tryDeclaration(reader: ParserReader): DeclarationNode? {
        val start = reader.currentPos
        val mods = parseModifiers(reader)
        if (reader.hope(KeywordTokenType.Struct) == null) return null

        val name = parseIdentifier(reader) ?: Identifier.empty
        val generics = parseGenericDeclaration(reader)
        val parameters = parseParameterList(reader)
        val bases = parseBases(reader, parser)
        val body = parseInstanceBody(reader, parser)

        return StructDeclaration(mods, name, generics, parameters, bases, body, reader.range(start))
    }
}