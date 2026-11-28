package builtin.helpers

import ParserReader
import builtin.nodes.DeclarationName
import builtin.nodes.ExtensionName
import builtin.nodes.OperatorName
import builtin.nodes.ScalarName
import builtin.tokentypes.SymbolTokenType
import builtin.tokentypes.VuraTokenType

fun parseDeclarationName(reader: ParserReader, allowExtension: Boolean = true): DeclarationName? {
    val start = reader.currentPos

    if (allowExtension) {
        reader.save()
        val type = parseType(reader)
        if (type != null && reader.hope(SymbolTokenType.Dot) != null) {
            // Extension
            reader.commit()
            val extension = parseDeclarationName(reader, false)
            return ExtensionName(type, extension, reader.range(start))
        }
        reader.rollBack()
    }

    if (reader.peek().type is SymbolTokenType) {
        // Operator
        val op = parseBinaryOperator(reader) ?: return null
        return OperatorName(op, reader.range(start))
    }

    // Scalar
    val name = reader.hope(VuraTokenType.Identifier)?.value ?: return null
    return ScalarName(name, reader.range(start))
}

val DeclarationName.isOperator: Boolean
    get() = if (this is ExtensionName) this.extension is OperatorName else this is OperatorName