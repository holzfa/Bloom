package builtin.rules.declarations

import DeclarationNode
import DeclarationRule
import Parser
import ParserReader
import builtin.helpers.parseBlock
import builtin.helpers.parseModifiers
import builtin.helpers.parseParameterList
import builtin.nodes.declarations.ConstructorDeclaration
import builtin.tokentypes.KeywordTokenType

class ConstructorRule(override val parser: Parser): DeclarationRule {
    override fun tryDeclaration(reader: ParserReader): DeclarationNode? {
        val start = reader.currentPos
        val mods = parseModifiers(reader)

        reader.hope(KeywordTokenType.Constructor) ?: return null
        val parameters = parseParameterList(reader)

        val body = parseBlock(reader, parser)

        return ConstructorDeclaration(mods, parameters, body, reader.range(start))
    }
}