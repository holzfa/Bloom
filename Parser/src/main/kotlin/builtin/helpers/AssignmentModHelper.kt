package builtin.helpers

import ParserReader
import builtin.nodes.AssignmentModifier
import builtin.tokentypes.SymbolTokenType

fun parseAssignmentMod(reader: ParserReader): AssignmentModifier? = when (reader.peek().type) {
    SymbolTokenType.Assign -> AssignmentModifier.Assign

    SymbolTokenType.AddAssign -> AssignmentModifier.PlusAssign
    SymbolTokenType.SubtractAssign -> AssignmentModifier.MinusAssign
    SymbolTokenType.MultiplyAssign -> AssignmentModifier.MultiplyAssign
    SymbolTokenType.DivideAssign -> AssignmentModifier.DivideAssign
    SymbolTokenType.ModuloAssign -> AssignmentModifier.ModuloAssign

    SymbolTokenType.AndAssign -> AssignmentModifier.AndAssign
    SymbolTokenType.OrAssign -> AssignmentModifier.OrAssign
    SymbolTokenType.XorAssign -> AssignmentModifier.XorAssign

    SymbolTokenType.LeftShiftAssign -> AssignmentModifier.LeftShiftAssign

    else -> {
        if (reader.hasSequence(SymbolTokenType.GreaterThan, SymbolTokenType.GreaterThan, SymbolTokenType.Assign)) {
            for (i in 1..3) reader.advance()
            AssignmentModifier.RightShiftAssign
        } else null
    }
}