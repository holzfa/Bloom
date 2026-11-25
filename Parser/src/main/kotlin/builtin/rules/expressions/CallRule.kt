package builtin.rules.expressions

import ExpressionNode
import Parser
import ParserReader
import PostExpressionRule
import builtin.helpers.parseBlock
import builtin.helpers.parseIdentifier
import builtin.nodes.Identifier
import builtin.nodes.expressions.CallExpression
import builtin.tokentypes.SymbolTokenType

class CallRule(override val parser: Parser): PostExpressionRule {
    override fun tryExpression(reader: ParserReader, expression: ExpressionNode): ExpressionNode? {
        val start = expression.pos.start

        val generics = if (reader.isNext(SymbolTokenType.LessThan)) {
            parseGenerics(reader) ?: return null
        } else null

        val params = mutableListOf<ExpressionNode>()

        reader.hope(SymbolTokenType.ParenthesisOpen) ?: return null
        while (!reader.isAtEnd && !reader.isNext(SymbolTokenType.ParenthesisClose)) {
            val param = parser.requestExpression(reader) ?: break
            params.add(param)

            if (reader.hope(SymbolTokenType.Comma) == null) break
        }
        reader.expect(SymbolTokenType.ParenthesisClose)

        // Trailing lambda, only a braced block is allowed here
        val body = parseBlock(reader, parser, blockOnly = true)

        return CallExpression(expression, generics, params, body, reader.range(start))
    }

    private fun parseGenerics(reader: ParserReader): List<Identifier>? {
        reader.hope(SymbolTokenType.LessThan) ?: return null

        val generics = mutableListOf<Identifier>()
        do {
            generics.add(parseIdentifier(reader) ?: return null)
        } while (reader.hope(SymbolTokenType.Comma) != null)

        reader.hope(SymbolTokenType.GreaterThan) ?: return null
        return generics
    }
}