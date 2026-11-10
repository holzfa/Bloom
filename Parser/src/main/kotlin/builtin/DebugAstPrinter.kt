package builtin

import AstNode
import AstVisitor
import FileNode
import builtin.nodes.Constraint
import builtin.nodes.GenericNode
import builtin.nodes.InstanceConstraint
import builtin.nodes.TypeConstraint
import builtin.nodes.declarations.ClassDeclaration
import builtin.nodes.declarations.EnumDeclaration
import builtin.nodes.declarations.EnumMember
import builtin.nodes.declarations.ExtensionsDeclaration
import builtin.nodes.declarations.ImportDeclaration
import builtin.nodes.declarations.ModuleDeclaration
import builtin.nodes.declarations.ObjectDeclaration
import builtin.nodes.declarations.StructDeclaration
import builtin.nodes.declarations.TraitDeclaration
import builtin.nodes.types.GenericType
import builtin.nodes.types.LambdaType
import builtin.nodes.types.ScalarType

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
        register<ExtensionsDeclaration> { handleExtensions(it) }

        register<LambdaType> { handleLambdaType(it) }
        register<ScalarType> { handleScalarType(it) }
        register<GenericType> { handleGenericType(it) }
        register<GenericNode> { handleGenericNode(it) }
    }

    override fun handleDefault(node: AstNode): String {
        return "${node.javaClass.simpleName} - ${node.pos}"
    }

    private fun handleFile(node: FileNode): String = node.nodes.joinToString(separator = "\n") { visit(it) }
    private fun handleImport(node: ImportDeclaration): String = if (node.selection == null) "import ${node.path}" else "from ${node.path} import ${node.selection.joinToString(separator = ", ")}"
    private fun handleModule(node: ModuleDeclaration): String = "module ${node.path}\n" + node.declarations.joinToString(separator = "\n") { visit(it) }
    private fun handleStruct(node: StructDeclaration): String = "Struct: '${node.name}' (${if (node.declarations != null) node.declarations.size.toString() else "None"})"
    private fun handleClass(node: ClassDeclaration): String = "Class: '${node.name}' (${if (node.declarations != null) node.declarations.size.toString() else "None"})"
    private fun handleObject(node: ObjectDeclaration): String = "Object: '${node.name}' (${if (node.declarations != null) node.declarations.size.toString() else "None"})"
    private fun handleTrait(node: TraitDeclaration): String = "Trait: '${node.name}' (${if (node.declarations != null) node.declarations.size.toString() else "None"})"
    private fun handleExtensions(node: ExtensionsDeclaration): String = "Extensions: ${visit(node.targetType)} (${if (node.declarations != null) node.declarations.size.toString() else "None"})"
    private fun handleEnum(node: EnumDeclaration): String = "Enum: ${node.name}" + (node.members?.joinToString { visit(it) } ?: "")
    private fun handleEnumMember(node: EnumMember): String = "${node.name}"


    private fun handleLambdaType(node: LambdaType): String = "Lambda -> ${node.returnType}"
    private fun handleScalarType(node: ScalarType): String = "Type: ${node.name}"
    private fun handleGenericType(node: GenericType): String = "Generic: ${node.name}<${node.genericTypes.joinToString(separator = ", ") { visit(it) }}>"
    private fun handleGenericNode(node: GenericNode): String = "${node.name}<${node.constraints.joinToString(separator = ", ") { handleConstraint(it) }}>"


    private fun handleConstraint(node: Constraint): String = if (node is TypeConstraint) handleTypeConstraint(node) else if (node is InstanceConstraint) handleInstanceConstraint(node) else "<error>"
    private fun handleTypeConstraint(node: TypeConstraint): String = visit(node.type)
    private fun handleInstanceConstraint(node: InstanceConstraint): String = node.instanceType.toString()
}