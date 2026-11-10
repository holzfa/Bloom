package builtin.rules.declarations

import DeclarationNode
import ErrorType
import Parser
import ParserReader
import ParserRule
import builtin.helpers.parseInstanceBody
import builtin.helpers.parseType
import builtin.nodes.declarations.ExtensionsDeclaration
import builtin.tokentypes.KeywordTokenType

class ExtensionsRule(override val parser: Parser): ParserRule() {
    override fun tryDeclaration(reader: ParserReader): DeclarationNode? {
        val start = reader.currentPos
        if (reader.hope(KeywordTokenType.Extensions) == null) return null

        val type = parseType(reader) ?: ErrorType(reader.range(start))
        val body = parseInstanceBody(reader, parser)

        return ExtensionsDeclaration(type, body, reader.range(start))
    }
}