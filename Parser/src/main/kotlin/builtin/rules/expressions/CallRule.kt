package builtin.rules.expressions

import ExpressionNode
import Parser
import ParserReader
import ParserRule
import builtin.helpers.parseBlock
import builtin.helpers.parseIdentifier
import builtin.nodes.Identifier
import builtin.nodes.expressions.CallExpression
import builtin.tokentypes.SymbolTokenType
import builtin.tokentypes.VuraTokenType

class CallRule(override val parser: Parser): ParserRule() {
    override fun tryExpression(reader: ParserReader): ExpressionNode? {
        val start = reader.currentPos

        val callee = parser.requestExpression(reader) ?: return null

        val generics: List<Identifier>? = if (reader.hope(SymbolTokenType.LessThan) != null) {
            val gen = mutableListOf<Identifier>()
            
            while (!reader.isAtEnd && reader.peek().type == VuraTokenType.Identifier) {
                val ident = parseIdentifier(reader)
                if (ident != null) gen.add(ident)
                else break

                if (reader.peek(1).type == VuraTokenType.Identifier) reader.expect(SymbolTokenType.Comma)
                else reader.hope(SymbolTokenType.Comma)
            }

            gen
        } else null

        val params = parser.requestExpressionsUntil(reader) {
            reader.isNext(SymbolTokenType.Comma)
        }
        reader.hope(SymbolTokenType.Comma)

        val body = parseBlock(reader, parser)


        return CallExpression(callee, generics, params, body, reader.range(start))
    }
}