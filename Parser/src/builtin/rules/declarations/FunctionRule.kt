package builtin.rules.declarations

import DeclarationNode
import DeclarationRule
import Parser
import ParserReader
import builtin.helpers.*
import builtin.nodes.Identifier
import builtin.nodes.declarations.FunctionDeclaration
import builtin.tokentypes.KeywordTokenType
import builtin.tokentypes.SymbolTokenType

class FunctionRule(override val parser: Parser): DeclarationRule {
    override fun tryDeclaration(reader: ParserReader): DeclarationNode? {
        val start = reader.currentPos
        val mods = parseModifiers(reader)
        reader.hope(KeywordTokenType.Function) ?: return null

        val name = parseDeclarationName(reader)
        val generics = parseGenericDeclaration(reader)

        val parameters = parseParameterList(reader)
        val returnType = if (reader.hope(SymbolTokenType.Arrow) != null) parseType(reader) else null

        val body = parseBlock(reader, parser)

        return FunctionDeclaration(mods, name, generics, parameters, returnType, body, reader.range(start))
    }
}