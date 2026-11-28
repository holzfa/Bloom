package builtin.rules.expressions

import ExpressionNode
import Parser
import ParserReader
import PreExpressionRule
import builtin.helpers.parseBlock
import builtin.helpers.parseMatchMember
import builtin.nodes.Block
import builtin.nodes.expressions.MatchExpression
import builtin.nodes.expressions.MatchMember
import builtin.tokentypes.KeywordTokenType
import builtin.tokentypes.SymbolTokenType

class MatchRule(override val parser: Parser): PreExpressionRule {
    override fun tryExpression(reader: ParserReader): ExpressionNode? {
        val start = reader.currentPos

        reader.hope(KeywordTokenType.Match) ?: return null
        reader.hope(SymbolTokenType.ParenthesisOpen) ?: return null
        val target = parser.requestExpression(reader)
        reader.expect(SymbolTokenType.ParenthesisClose)

        reader.expect(SymbolTokenType.BraceOpen)
        val members = mutableListOf<MatchMember>()
        while (!reader.isAtEnd && !reader.isNext(SymbolTokenType.BraceClose, KeywordTokenType.Else)) {
            val member = parseMatchMember(reader, parser)
            if (member != null) members.add(member)
            else break
        }

        var default: Block? = null
        if (reader.hope(KeywordTokenType.Else) != null) {
            default = parseBlock(reader, parser)
        }
        reader.expect(SymbolTokenType.BraceClose)

        return MatchExpression(target, members, default, reader.range(start))
    }
}