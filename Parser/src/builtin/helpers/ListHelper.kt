package builtin.helpers

import ErrorType
import ExpressionNode
import Parser
import ParserReader
import TokenType
import builtin.nodes.Parameter
import builtin.tokentypes.SymbolTokenType
import builtin.tokentypes.VuraTokenType

fun <TItem>parseList(
    reader: ParserReader,
    separator: TokenType = SymbolTokenType.Comma,
    getItem: () -> TItem?
): List<TItem> {
    val elements = mutableListOf<TItem>()
    while (!reader.isAtEnd) {
        val param = getItem() ?: break
        elements.add(param)

        if (reader.hope(separator) == null) break
    }

    return elements
}

fun <TItem>parseContainedList(
    startType: TokenType,
    endType: TokenType,
    reader: ParserReader,
    separator: TokenType = SymbolTokenType.Comma,
    getItem: () -> TItem?
): List<TItem>? {
    val elements = mutableListOf<TItem>()
    reader.hope(startType) ?: return null
    while (!reader.isAtEnd && !reader.isNext(endType)) {
        val param = getItem() ?: break
        elements.add(param)

        if (reader.hope(separator) == null) break
    }
    reader.expect(endType)

    return elements
}

fun parseExpressionList(startType: TokenType, endType: TokenType, reader: ParserReader, parser: Parser): List<ExpressionNode>? {
    return parseContainedList(startType, endType, reader) {
        parser.requestExpression(reader)
    }
}

fun parseParameterList(reader: ParserReader): List<Parameter>? {
    return parseContainedList(SymbolTokenType.ParenthesisOpen, SymbolTokenType.ParenthesisClose, reader) {
        parseParameterDeclaration(reader)
    }
}

fun parseParameterDeclaration(reader: ParserReader): Parameter? {
    val start = reader.currentPos

    val varMod = parseVariableMod(reader)
    val name = parseIdentifier(reader) ?: return null
    reader.expect(SymbolTokenType.Colon)

    val typeStart = reader.currentPos
    val type = parseType(reader) ?: ErrorType(reader.range(typeStart))

    return Parameter(varMod, name, type, reader.range(start))
}