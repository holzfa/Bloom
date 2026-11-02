package builtin.helpers

import ParserReader
import builtin.nodes.VariableModifier
import builtin.tokentypes.KeywordTokenType

fun parseVariableMod(reader: ParserReader): VariableModifier? {
    if (reader.hope(KeywordTokenType.Const) != null) return VariableModifier.Const
    if (reader.hope(KeywordTokenType.Val) != null) return VariableModifier.Val
    if (reader.hope(KeywordTokenType.Var) != null) return VariableModifier.Var

    return null
}