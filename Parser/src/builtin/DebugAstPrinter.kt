package builtin

import AstNode
import AstVisitor
import DeclarationNode
import ErrorDeclaration
import ErrorFile
import FileNode
import builtin.nodes.GenericNode
import builtin.nodes.ImplementationNode
import builtin.nodes.Modifier
import builtin.nodes.Parameter
import builtin.nodes.declarations.*

/** Quick debug dump: one line per declaration, nested declarations indented, everything else inlined */
object DebugAstPrinter: AstVisitor<String>() {
    init {
        register<FileNode> { it.nodes.joinToString("\n") { n -> visit(n) } }
        register<ErrorFile> { line(it, "ErrorFile") }
        register<ErrorDeclaration> { line(it, "ErrorDeclaration") }

        register<ImportDeclaration> { line(it, "$it") }
        register<ModuleDeclaration> { tree(line(it, "module ${it.path}"), it.declarations) }

        register<StructDeclaration> {
            tree(
                line(it, "${mods(it.modifiers)}struct ${it.name}${generics(it.generics)}${params(it.parameters)}${bases(it.bases)}${bodyNote(it.declarations)}"),
                it.declarations
            )
        }
        register<ClassDeclaration> {
            tree(
                line(it, "${mods(it.modifiers)}class ${it.name}${generics(it.generics)}${params(it.parameters)}${bases(it.bases)}${bodyNote(it.declarations)}"),
                it.declarations
            )
        }
        register<TraitDeclaration> {
            tree(
                line(it, "${mods(it.modifiers)}trait ${it.name}${generics(it.generics)}${params(it.parameters)}${bases(it.bases)}${bodyNote(it.declarations)}"),
                it.declarations
            )
        }
        register<ObjectDeclaration> {
            tree(
                line(it, "${mods(it.modifiers)}object ${it.name}${bases(it.bases)}${bodyNote(it.declarations)}"),
                it.declarations
            )
        }
        register<ComplementObjectDeclaration> {
            tree(line(it, "complement object${bodyNote(it.declarations)}"), it.declarations)
        }
        register<ExtensionsDeclaration> {
            tree(line(it, "extensions ${it.targetType}${bodyNote(it.declarations)}"), it.declarations)
        }
        register<EnumDeclaration> {
            val base = it.base?.let { b -> " : ${base(b)}" } ?: ""
            val head = line(it, "${mods(it.modifiers)}enum ${it.name}${params(it.parameters)}$base")
            head + it.members.orEmpty().joinToString("") { m -> "\n" + indent("$m") }
        }

        register<FunctionDeclaration> {
            val ret = it.returnType?.let { t -> " -> $t" } ?: ""
            line(it, "${mods(it.modifiers)}fun ${it.name}${generics(it.generics)}${params(it.parameters)}$ret${blockNote(it.body?.statements?.size)}")
        }
        register<ConstructorDeclaration> {
            line(it, "${mods(it.modifiers)}constructor${params(it.parameters)}${blockNote(it.body?.statements?.size)}")
        }
        register<PropertyDeclaration> {
            val type = it.type?.let { t -> ": $t" } ?: ""
            val flags = (if (it.getter != null) " [get]" else "") +
                    (if (it.setter != null) " [set]" else "") +
                    (if (it.initializer != null) " [init]" else "")
            line(it, "${mods(it.modifiers)}prop ${it.name}$type$flags")
        }
        register<VariableDeclaration> {
            val type = it.type?.let { t -> ": $t" } ?: ""
            val init = if (it.initializer != null) " [init]" else ""
            line(it, "${mods(it.modifiers)}${it.kind} ${it.name}$type$init")
        }
    }

    override fun handleDefault(node: AstNode): String {
        return "<unhandled ${node.javaClass.simpleName}> @${node.pos}"
    }

    private fun line(node: AstNode, text: String): String = "$text @${node.pos}"

    private fun indent(text: String): String = text.lines().joinToString("\n") { "  $it" }

    private fun tree(head: String, children: List<DeclarationNode>?): String =
        head + children.orEmpty().joinToString("") { "\n" + indent(visit(it)) }

    private fun mods(modifiers: Set<Modifier>): String =
        if (modifiers.isEmpty()) "" else modifiers.joinToString(" ") { it.name.lowercase() } + " "

    private fun generics(list: List<GenericNode>?): String =
        if (list.isNullOrEmpty()) "" else "<${list.joinToString()}>"

    private fun params(list: List<Parameter>?): String =
        if (list == null) "" else "(${list.joinToString()})"

    private fun base(node: ImplementationNode): String {
        val args = node.parameters
        return "${node.type}" + if (args != null) "(${args.size} args)" else ""
    }

    private fun bases(list: List<ImplementationNode>?): String =
        if (list.isNullOrEmpty()) "" else " : " + list.joinToString { base(it) }

    private fun bodyNote(declarations: List<DeclarationNode>?): String =
        if (declarations == null) " (no body)" else ""

    private fun blockNote(statements: Int?): String =
        if (statements == null) " (no body)" else " {$statements}"
}