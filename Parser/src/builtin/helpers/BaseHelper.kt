package builtin.helpers

import ExpressionNode
import Parser
import ParserReader
import builtin.nodes.ImplementationNode
import builtin.tokentypes.SymbolTokenType

fun parseBase(reader: ParserReader, parser: Parser): ImplementationNode? {
    val start = reader.currentPos

    val type = parseType(reader) ?: return null
    if (reader.hope(SymbolTokenType.ParenthesisOpen) != null) {
        val params = mutableListOf<ExpressionNode>()
        while (!reader.isAtEnd && reader.hope(SymbolTokenType.ParenthesisClose) == null) {
            val param = parser.requestExpression(reader)
            if (param != null) params.add(param)
        }
        return ImplementationNode(type, params, reader.range(start))
    } else {
        return ImplementationNode(type, null, reader.range(start))
    }
}

fun parseBases(reader: ParserReader, parser: Parser): List<ImplementationNode>? {
    if (reader.hope(SymbolTokenType.Colon) != null) {
        return parseList(reader) { parseBase(reader, parser) }
    }

    return null
}