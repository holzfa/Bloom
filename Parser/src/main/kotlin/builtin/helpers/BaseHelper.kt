package builtin.helpers

import ExpressionNode
import Parser
import ParserReader
import builtin.nodes.Identifier
import builtin.nodes.Parameter
import builtin.nodes.declarations.ImplementationNode
import builtin.tokentypes.SymbolTokenType

fun parseBase(reader: ParserReader, parser: Parser): ImplementationNode? {
    val start = reader.currentPos

    val name = parseIdentifier(reader) ?: return null
    if (reader.hope(SymbolTokenType.ParenthesisOpen) != null) {
        val params = mutableListOf<ExpressionNode>()
        while (!reader.isAtEnd && reader.hope(SymbolTokenType.ParenthesisClose) == null) {
            val param = parser.requestExpression(reader)
            if (param != null) params.add(param)
        }
        return ImplementationNode(name, params, reader.range(start))
    } else {
        return ImplementationNode(name, null, reader.range(start))
    }
}

fun parseBases(reader: ParserReader, parser: Parser): List<ImplementationNode>? {
    val start = reader.currentPos

    val impls = mutableListOf<ImplementationNode>()
    while (!reader.isAtEnd && reader.peek().type != SymbolTokenType.BraceOpen) {
        // ToDo: I have no clue how to do this...
    }

    return if (impls.isEmpty()) null else impls
}