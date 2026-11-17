package builtin.rules.expressions

import ExpressionNode
import Parser
import ParserReader
import ParserRule
import builtin.helpers.parseAssignmentMod
import builtin.helpers.parseBlock
import builtin.helpers.parseIdentifier
import builtin.nodes.expressions.IdentifierExpression
import builtin.nodes.expressions.IfExpression
import builtin.tokentypes.KeywordTokenType
import builtin.tokentypes.SymbolTokenType

class IfRule(override val parser: Parser): ParserRule() {
    override fun tryExpression(reader: ParserReader): ExpressionNode? {
        val start = reader.currentPos

        reader.hope(KeywordTokenType.If) ?: return null
        reader.hope(SymbolTokenType.ParenthesisOpen) ?: return null
        val condition = parser.requestExpression(reader)
        reader.expect(SymbolTokenType.ParenthesisClose)

        val thenBranch = parseBlock(reader, parser)

        val elseBranch = if (reader.hope(KeywordTokenType.Else) != null) {
            parseBlock(reader, parser)
        } else null

        return IfExpression(condition, thenBranch, elseBranch, reader.range(start))
    }
}