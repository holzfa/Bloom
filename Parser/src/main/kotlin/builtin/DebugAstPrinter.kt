package builtin

import AstNode
import AstVisitor
import FileNode
import builtin.nodes.declarations.ClassDeclaration
import builtin.nodes.declarations.EnumDeclaration
import builtin.nodes.declarations.EnumMember
import builtin.nodes.declarations.ImportDeclaration
import builtin.nodes.declarations.ModuleDeclaration
import builtin.nodes.declarations.ObjectDeclaration
import builtin.nodes.declarations.StructDeclaration
import builtin.nodes.declarations.TraitDeclaration

object DebugAstPrinter: AstVisitor<String>() {
    init {
        register<FileNode> { handleFile(it) }
        register<ImportDeclaration> { handleImport(it) }
        register<ModuleDeclaration> { handleModule(it) }
        register<StructDeclaration> { handleStruct(it) }
        register<ClassDeclaration> { handleClass(it) }
        register<ObjectDeclaration> { handleObject(it) }
        register<TraitDeclaration> { handleTrait(it) }
        register<EnumDeclaration> { handleEnum(it) }
        register<EnumMember> { handleEnumMember(it) }
    }

    override fun handleDefault(node: AstNode): String {
        return "${node.javaClass.simpleName} - ${node.pos}"
    }

    private fun handleFile(node: FileNode): String = node.nodes.joinToString(separator = "\n") { visit(it) }
    private fun handleImport(node: ImportDeclaration): String = "import ${node.path}"
    private fun handleModule(node: ModuleDeclaration): String = "module ${node.path}\n" + node.declarations.joinToString(separator = "\n") { visit(it) }
    private fun handleStruct(node: StructDeclaration): String = "Struct: ${node.name}"
    private fun handleClass(node: ClassDeclaration): String = "Class: ${node.name}"
    private fun handleObject(node: ObjectDeclaration): String = "Object: ${node.name}"
    private fun handleTrait(node: TraitDeclaration): String = "Trait: ${node.name}"
    private fun handleEnum(node: EnumDeclaration): String = "Enum: ${node.name}" + (node.members?.joinToString { visit(it) } ?: "")
    private fun handleEnumMember(node: EnumMember): String = "${node.name}"
}