package builtin.rules.expressions

import ExpressionNode
import Parser
import ParserReader
import PreExpressionRule
import builtin.nodes.LiteralType
import builtin.nodes.expressions.LiteralExpression
import builtin.tokentypes.KeywordTokenType
import builtin.tokentypes.LiteralTokenType

private val keywordTypes = hashSetOf(
    KeywordTokenType.It,
    KeywordTokenType.Self,
    KeywordTokenType.Null,
    KeywordTokenType.True,
    KeywordTokenType.False
)

class LiteralRule(override val parser: Parser): PreExpressionRule {
    override fun tryExpression(reader: ParserReader): ExpressionNode? {
        val start = reader.currentPos

        val literal = reader.advance()
        return if (literal.type is LiteralTokenType) {
            LiteralExpression(LiteralType.fromLiteral(literal.type as LiteralTokenType), literal.value, reader.range(start))
        } else if (keywordTypes.contains(literal.type)) {
            LiteralExpression(LiteralType.fromKeyword(literal.type as KeywordTokenType)!!, literal.value, reader.range(start))
        }
        else null
    }
}