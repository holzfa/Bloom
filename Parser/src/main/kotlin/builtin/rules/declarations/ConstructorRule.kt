package builtin.rules.declarations

import DeclarationNode
import DeclarationRule
import Parser
import ParserReader
import builtin.helpers.parseBlock
import builtin.helpers.parseIdentifier
import builtin.helpers.parseModifiers
import builtin.helpers.parseParametersDeclaration
import builtin.nodes.declarations.ConstructorDeclaration

class ConstructorRule(override val parser: Parser): DeclarationRule {
    override fun tryDeclaration(reader: ParserReader): DeclarationNode? {
        val start = reader.currentPos
        val mods = parseModifiers(reader)

        val name = parseIdentifier(reader) ?: return null
        val parameters = parseParametersDeclaration(reader)

        val body = parseBlock(reader, parser)

        return ConstructorDeclaration(mods, name, parameters, body, reader.range(start))
    }
}