package builtin.rules.declarations

import DeclarationNode
import Parser
import ParserReader
import ParserRule
import builtin.helpers.parseBases
import builtin.helpers.parseGenericDeclaration
import builtin.helpers.parseIdentifier
import builtin.helpers.parseInstanceBody
import builtin.helpers.parseModifiers
import builtin.helpers.parseParametersDeclaration
import builtin.nodes.Identifier
import builtin.nodes.declarations.ObjectDeclaration
import builtin.nodes.declarations.StructDeclaration
import builtin.tokentypes.KeywordTokenType

class ObjectRule(override val parser: Parser): ParserRule() {
    override fun tryDeclaration(reader: ParserReader): DeclarationNode? {
        val start = reader.currentPos
        val mods = parseModifiers(reader)
        if (reader.hope(KeywordTokenType.Object) == null) return null

        val name = parseIdentifier(reader) ?: Identifier.empty
        val bases = parseBases(reader, parser)
        val body = parseInstanceBody(reader, parser)

        return ObjectDeclaration(mods, name, bases, body, reader.range(start))
    }
}