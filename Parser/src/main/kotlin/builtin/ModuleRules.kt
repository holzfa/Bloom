package builtin

import Parser
import ParserReader
import ParserRule
import builtin.nodes.ModuleStatement
import builtin.tokentypes.KeywordTokenType
import builtin.tokentypes.VuraTokenType

internal class ModuleStatementRule(override val parser: Parser): ParserRule<ModuleStatement> {
    override fun tryMatch(reader: ParserReader): ModuleStatement? {
        val startType = reader.peek().type
        if (startType != KeywordTokenType.Module) return null

        val start = reader.currentPos
        reader.advance()

        val pathToken = reader.expect(VuraTokenType.Identifier, "Expected a module path")
        reader.expectTerminator()
        return if (pathToken != null) {
            ModuleStatement(pathToken.value, reader.range(start))
        } else {
            null
        }
    }
}