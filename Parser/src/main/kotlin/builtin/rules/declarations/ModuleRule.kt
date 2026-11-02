package builtin.rules.declarations

import DeclarationNode
import Parser
import ParserReader
import ParserRule
import builtin.helpers.parseIdentifier
import builtin.nodes.declarations.ModuleDeclaration
import builtin.tokentypes.KeywordTokenType
import builtin.tokentypes.SymbolTokenType

internal class ModuleRule(override val parser: Parser): ParserRule() {
    override fun tryDeclaration(reader: ParserReader): ModuleDeclaration? {
        val startType = reader.peek().type
        if (startType != KeywordTokenType.Module) return null

        val start = reader.currentPos
        reader.advance()

        val path = parseIdentifier(reader) ?: return null

        val declarations = mutableListOf<DeclarationNode>()
        if (!reader.isNext(SymbolTokenType.BraceOpen)) {
            // Single module
            reader.expectTerminator()
            declarations.addAll(parser.requestDeclarationsUntil(reader) { true })
        } else {
            // Scoped module
            reader.expect(SymbolTokenType.BraceOpen)
            declarations.addAll(parser.requestDeclarationsUntil(reader) { !reader.isNext(SymbolTokenType.BraceClose) })
            reader.expect(SymbolTokenType.BraceClose)
        }
        return ModuleDeclaration(path, declarations, reader.range(start))
    }
}