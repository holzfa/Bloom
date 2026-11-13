package builtin.rules.declarations

import DeclarationNode
import Parser
import ParserReader
import ParserRule
import TokenType
import builtin.helpers.parseBlock
import builtin.helpers.parseIdentifier
import builtin.helpers.parseModifiers
import builtin.helpers.parseType
import builtin.nodes.Identifier
import builtin.nodes.PropertyAccessor
import builtin.nodes.declarations.PropertyDeclaration
import builtin.tokentypes.KeywordTokenType
import builtin.tokentypes.SymbolTokenType

class PropertyDeclRule(override val parser: Parser): ParserRule() {
    override fun tryDeclaration(reader: ParserReader): DeclarationNode? {
        val start = reader.currentPos
        val mods = parseModifiers(reader)
        reader.hope(KeywordTokenType.Property) ?: return null

        val name = parseIdentifier(reader) ?: Identifier.empty
        val type = if (reader.hope(SymbolTokenType.Colon) != null) parseType(reader) else null

        val accessors = parseAccessors(reader)
        val init = if (reader.hope(SymbolTokenType.Assign) != null) parser.requestExpression(reader) else null

        return PropertyDeclaration(mods, name, type, accessors.first, accessors.second, init, reader.range(start))
    }

    private fun parseAccessors(reader: ParserReader): Pair<PropertyAccessor?, PropertyAccessor?> {
        if (reader.hope(SymbolTokenType.BraceOpen) != null) {
            val getAccessor: (TokenType) -> PropertyAccessor? = {
                val accessorStart = reader.currentPos

                if (reader.hope(it) != null) {
                    val body = if (reader.hope(SymbolTokenType.Arrow) != null) parseBlock(reader, parser) else null

                    PropertyAccessor(body, reader.range(accessorStart))
                } else null
            }

            val getter = getAccessor(KeywordTokenType.Get)
            val setter = getAccessor(KeywordTokenType.Set)

            reader.expect(SymbolTokenType.BraceClose)

            return Pair(getter, setter)
        } else {
            return Pair(null, null)
        }
    }
}