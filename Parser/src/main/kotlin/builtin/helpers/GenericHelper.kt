package builtin.helpers

import ErrorType
import ParserReader
import builtin.nodes.Constraint
import builtin.nodes.GenericNode
import builtin.nodes.InstanceConstraint
import builtin.nodes.TypeConstraint
import builtin.nodes.declarations.ImplementationNode
import builtin.tokentypes.SymbolTokenType
import builtin.tokentypes.VuraTokenType
import kotlin.time.measureTime

fun parseGenericDeclaration(reader: ParserReader): List<GenericNode>? {
    if (reader.hope(SymbolTokenType.LessThan) == null) return null

    val parseConstraint: () -> Constraint? = {
        val instance = parseInstanceType(reader)
        if (instance != null) {
            InstanceConstraint(instance)
        } else {
            val type = parseType(reader)
            if (type != null) TypeConstraint(type)
            else null
        }
    }

    val nodes = mutableListOf<GenericNode>()
    while (!reader.isAtEnd && reader.hope(SymbolTokenType.GreaterThan) == null) {
        val pos = reader.currentPos
        val name = parseIdentifier(reader) ?: break
        val constraints = mutableListOf<Constraint>()

        if (reader.hope(SymbolTokenType.ParenthesisOpen) == null) {
            while (!reader.isAtEnd && reader.hope(SymbolTokenType.ParenthesisClose) != null) {
                val const = parseConstraint()
                if (const != null) constraints.add(const)
            }
        } else {
            val const = parseConstraint()
            if (const != null) constraints.add(const)
        }
        nodes.add(GenericNode(name, constraints, reader.range(pos)))

        if (reader.peek(1).type == VuraTokenType.Identifier) {
            reader.expect(SymbolTokenType.Comma)
        } else {
            reader.hope(SymbolTokenType.Comma)
        }
    }

    return if (nodes.isEmpty()) null else nodes
}