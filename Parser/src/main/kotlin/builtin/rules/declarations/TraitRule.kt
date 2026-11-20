package builtin.rules.declarations

import DeclarationNode
import DeclarationRule
import Parser
import ParserReader
import builtin.helpers.*
import builtin.nodes.Identifier
import builtin.nodes.declarations.TraitDeclaration
import builtin.tokentypes.KeywordTokenType

class TraitRule(override val parser: Parser): DeclarationRule {
    override fun tryDeclaration(reader: ParserReader): DeclarationNode? {
        val start = reader.currentPos
        val mods = parseModifiers(reader)
        if (reader.hope(KeywordTokenType.Trait) == null) return null

        val name = parseIdentifier(reader) ?: Identifier.empty
        val generics = parseGenericDeclaration(reader)
        val parameters = parseParametersDeclaration(reader)
        val bases = parseBases(reader, parser)
        val body = parseInstanceBody(reader, parser)

        return TraitDeclaration(mods, name, generics, parameters, bases, body, reader.range(start))
    }
}