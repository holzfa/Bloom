package builtin.helpers

import Parser
import ParserReader
import builtin.nodes.expressions.MatchMember

fun parseMatchMember(reader: ParserReader, parser: Parser): MatchMember? {
    val start = reader.currentPos

    val pattern = parser.requestExpression(reader) ?: return null
    val body = parseBlock(reader, parser)

    return MatchMember(pattern, body, reader.range(start))
}