package builtin.rules.declarations

import DeclarationNode
import DeclarationRule
import Parser
import ParserReader
import builtin.helpers.parseInstanceBody
import builtin.nodes.declarations.ComplementObjectDeclaration
import builtin.tokentypes.KeywordTokenType

class ComplementObjectRule(override val parser: Parser): DeclarationRule {
    override fun tryDeclaration(reader: ParserReader): DeclarationNode? {
        val start = reader.currentPos
        if (reader.hope(KeywordTokenType.Object) == null) return null

        // We cannot let it continue with a missing body
        val body = parseInstanceBody(reader, parser) ?: return null

        return ComplementObjectDeclaration(body, reader.range(start))
    }
}