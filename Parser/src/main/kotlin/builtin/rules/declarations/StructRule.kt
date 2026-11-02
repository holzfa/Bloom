package builtin.rules.declarations

import DeclarationNode
import Parser
import ParserReader
import ParserRule
import builtin.helpers.parseGenericDeclaration
import builtin.helpers.parseIdentifier
import builtin.helpers.parseModifiers
import builtin.helpers.parseParametersDeclaration
import builtin.nodes.Identifier
import builtin.nodes.declarations.StructDeclaration
import builtin.tokentypes.KeywordTokenType

class StructRule(override val parser: Parser): ParserRule() {
    override fun tryDeclaration(reader: ParserReader): DeclarationNode? {
        val mods = parseModifiers(reader)
        if (reader.hope(KeywordTokenType.Struct) == null) return null

        val name = parseIdentifier(reader) ?: Identifier.empty
        val generics = parseGenericDeclaration(reader)
        val parameters = parseParametersDeclaration(reader)
        val bases = parseBase

        return StructDeclaration(mods, name, generics, parameters)
    }
}