package builtin.helpers

import Parser
import ParserReader
import builtin.nodes.Block
import builtin.tokentypes.SymbolTokenType

fun parseBlock(reader: ParserReader, parser: Parser): Block? {
    val start = reader.currentPos

    val statements = if (reader.hope(SymbolTokenType.BraceOpen) != null) {
         parser.requestStatementsUntil(reader) { reader.hope(SymbolTokenType.BraceClose) != null }
    } else if (reader.hope(SymbolTokenType.Assign) != null) {
        listOf(parser.requestStatement(reader) ?: return null)
    } else {
        return null
    }

    return Block(statements, reader.range(start))
}