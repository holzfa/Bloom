package builtin.helpers

import ExpressionNode
import Parser
import ParserReader
import builtin.nodes.declarations.EnumMember
import builtin.tokentypes.SymbolTokenType

fun parseEnumMember(reader: ParserReader, parser: Parser): EnumMember? {
    val start = reader.currentPos
    val name = parseIdentifier(reader) ?: return null

    val vals = mutableListOf<ExpressionNode>()
    if (reader.hope(SymbolTokenType.ParenthesisOpen) != null) {
        while (!reader.isAtEnd && reader.hope(SymbolTokenType.ParenthesisClose) == null) {
            val expr = parser.requestExpression(reader)
            if (expr != null) vals.add(expr)
        }
    }

    var init: ExpressionNode? = null
    if (reader.hope(SymbolTokenType.Assign) != null) {
        init = parser.requestExpression(reader)
    }

    return EnumMember(name, if (vals.isEmpty()) null else vals, init, reader.range(start))
}