package builtin.helpers

import DeclarationNode
import Parser
import ParserReader
import builtin.tokentypes.SymbolTokenType
import java.io.Reader

fun parseInstanceBody(reader: ParserReader, parser: Parser): List<DeclarationNode>? {
    return if (reader.hope(SymbolTokenType.BraceOpen) != null) {
        parser.requestDeclarationsUntil(reader) { reader.hope(SymbolTokenType.BraceClose) != null }
    } else {
        null
    }
}