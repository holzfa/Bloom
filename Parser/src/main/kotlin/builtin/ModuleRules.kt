package builtin

import Parser
import ParserReader
import ParserRule
import builtIn.tokentypes.KeywordTokenType
import builtIn.tokentypes.KlarTokenType
import builtin.nodes.ModuleStatement

internal class ModuleStatementRule(override val parser: Parser): ParserRule<ModuleStatement> {
    override fun tryMatch(reader: ParserReader): ModuleStatement? {
        val startType = reader.peek().type
        if (startType != KeywordTokenType.Module) return null

        val start = reader.currentPos
        reader.advance()

        val pathToken = reader.expect(KlarTokenType.Identifier, "Expected a path for the module")
        reader.expectTerminator()
        return if (pathToken != Token.error) {
            ModuleStatement(pathToken.value, reader.range(start))
        } else {
            null
        }
    }
}