package builtin.helpers

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

    val parseConstraint: () -> Constraint = {
        val instance = parseInstanceType(reader)
        if (instance != null) {
            InstanceConstraint(instance)
        } else {
            val type = parseType(reader)
            TypeConstraint(type)
        }
    }

    val nodes = mutableListOf<GenericNode>()
    if (reader.hope(SymbolTokenType.GreaterThan) != null) {
        while (!reader.isAtEnd && reader.hope(SymbolTokenType.GreaterThan) == null) {
            val pos = reader.currentPos
            val name = parseIdentifier(reader) ?: break
            val constraints = mutableListOf<Constraint>()

            if (reader.hope(SymbolTokenType.ParenthesisOpen) != null) {
                while (!reader.isAtEnd && reader.hope(SymbolTokenType.ParenthesisClose) != null) {
                    constraints.add(parseConstraint())
                }
            } else {
                constraints.add(parseConstraint())
            }

            nodes.add(GenericNode(name, constraints, reader.range(pos)))
        }
    }

    return if (nodes.isEmpty()) null else nodes
}