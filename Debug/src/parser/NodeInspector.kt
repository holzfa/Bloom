package io.holzfa.bloom.debug

import AstNode
import AstVisitor
import ErrorDeclaration
import ErrorExpression
import ErrorFile
import ErrorStatement
import ErrorType
import FileNode
import builtin.nodes.Block
import builtin.nodes.GenericNode
import builtin.nodes.ImplementationNode
import builtin.nodes.InstanceConstraint
import builtin.nodes.Parameter
import builtin.nodes.PropertyAccessor
import builtin.nodes.TypeConstraint
import builtin.nodes.declarations.*
import builtin.nodes.expressions.*
import builtin.nodes.statements.*
import builtin.nodes.types.GenericType
import builtin.nodes.types.LambdaType
import builtin.nodes.types.ScalarType

/** `fields` is what the hover shows, `children` are the nested nodes that are walked */
class NodeInfo(val fields: List<Pair<String, String>>, val children: List<AstNode>)

private class InfoBuilder {
    val fields = ArrayList<Pair<String, String>>()
    val children = ArrayList<AstNode>()

    fun value(name: String, value: Any?) {
        fields.add(name to (value?.toString() ?: "null"))
    }

    fun node(name: String, node: AstNode?) {
        if (node != null) children.add(node)
        fields.add(name to describe(node))
    }

    fun nodes(name: String, nodes: List<AstNode>?) {
        if (nodes != null) children.addAll(nodes)
        fields.add(name to when {
            nodes == null -> "null"
            nodes.isEmpty() -> "[]"
            else -> nodes.joinToString("\n") { describe(it) }
        })
    }

    private fun describe(node: AstNode?): String =
        if (node == null) "null" else "${node::class.simpleName} ${node.pos}"
}

private fun info(block: InfoBuilder.() -> Unit): NodeInfo {
    val builder = InfoBuilder()
    builder.block()
    return NodeInfo(builder.fields, builder.children)
}

object NodeInspector : AstVisitor<NodeInfo>() {
    init {
        register<FileNode> { info { nodes("declarations", it.nodes) } }
        register<ErrorFile> { info { } }
        register<ErrorDeclaration> { info { } }
        register<ErrorStatement> { info { } }
        register<ErrorExpression> { info { } }
        register<ErrorType> { info { } }

        register<Block> { info { nodes("statements", it.statements) } }
        register<GenericNode> {
            info {
                value("name", it.name)
                for (constraint in it.constraints) {
                    when (constraint) {
                        is TypeConstraint -> node("constraint", constraint.type)
                        is InstanceConstraint -> value("constraint", constraint.instanceType)
                        else -> value("constraint", constraint)
                    }
                }
            }
        }
        register<ImplementationNode> {
            info {
                value("type", it.type)
                nodes("parameters", it.parameters)
            }
        }
        register<Parameter> {
            info {
                value("kind", it.kind)
                value("name", it.name)
                node("type", it.type)
            }
        }
        register<PropertyAccessor> { info { node("body", it.body) } }

        register<ModuleDeclaration> {
            info {
                value("path", it.path)
                nodes("declarations", it.declarations)
            }
        }
        register<ImportDeclaration> {
            info {
                value("path", it.path)
                value("selection", it.selection)
            }
        }
        register<StructDeclaration> {
            info {
                value("modifiers", it.modifiers)
                value("name", it.name)
                nodes("generics", it.generics)
                nodes("parameters", it.parameters)
                nodes("bases", it.bases)
                nodes("declarations", it.declarations)
            }
        }
        register<ClassDeclaration> {
            info {
                value("modifiers", it.modifiers)
                value("name", it.name)
                nodes("generics", it.generics)
                nodes("parameters", it.parameters)
                nodes("bases", it.bases)
                nodes("declarations", it.declarations)
            }
        }
        register<TraitDeclaration> {
            info {
                value("modifiers", it.modifiers)
                value("name", it.name)
                nodes("generics", it.generics)
                nodes("parameters", it.parameters)
                nodes("bases", it.bases)
                nodes("declarations", it.declarations)
            }
        }
        register<ObjectDeclaration> {
            info {
                value("modifiers", it.modifiers)
                value("name", it.name)
                nodes("bases", it.bases)
                nodes("declarations", it.declarations)
            }
        }
        register<ExtensionsDeclaration> {
            info {
                node("targetType", it.targetType)
                nodes("declarations", it.declarations)
            }
        }
        register<EnumDeclaration> {
            info {
                value("modifiers", it.modifiers)
                value("name", it.name)
                nodes("parameters", it.parameters)
                node("base", it.base)
                nodes("members", it.members)
            }
        }
        register<EnumMember> {
            info {
                value("name", it.name)
                nodes("values", it.values)
                node("initializer", it.initializer)
            }
        }
        register<FunctionDeclaration> {
            info {
                value("modifiers", it.modifiers)
                value("name", it.name)
                nodes("generics", it.generics)
                nodes("parameters", it.parameters)
                node("returnType", it.returnType)
                node("body", it.body)
            }
        }
        register<ConstructorDeclaration> {
            info {
                value("modifiers", it.modifiers)
                nodes("parameters", it.parameters)
                node("body", it.body)
            }
        }
        register<PropertyDeclaration> {
            info {
                value("modifiers", it.modifiers)
                value("name", it.name)
                node("type", it.type)
                node("getter", it.getter)
                node("setter", it.setter)
                node("initializer", it.initializer)
            }
        }
        register<VariableDeclaration> {
            info {
                value("modifiers", it.modifiers)
                value("kind", it.kind)
                value("name", it.name)
                node("type", it.type)
                node("initializer", it.initializer)
            }
        }

        register<AssignmentStatement> {
            info {
                node("location", it.location)
                value("mod", it.mod)
                node("value", it.value)
            }
        }
        register<ExpressionStatement> { info { node("expr", it.expr) } }
        register<ForStatement> {
            info {
                value("itemName", it.itemName)
                node("collection", it.collection)
                node("body", it.body)
            }
        }
        register<ReturnStatement> { info { node("value", it.value) } }
        register<VariableStatement> {
            info {
                value("mod", it.mod)
                value("name", it.name)
                node("type", it.type)
                node("initialValue", it.initialValue)
            }
        }
        register<WhileStatement> {
            info {
                node("condition", it.condition)
                node("body", it.body)
            }
        }

        register<CallExpression> {
            info {
                node("callee", it.callee)
                value("generics", it.generics)
                nodes("parameters", it.parameters)
                node("body", it.body)
            }
        }
        register<IdentifierExpression> { info { value("identifier", it.identifier) } }
        register<IfExpression> {
            info {
                node("condition", it.condition)
                node("thenBranch", it.thenBranch)
                node("elseBranch", it.elseBranch)
            }
        }
        register<IndexExpression> {
            info {
                node("callee", it.callee)
                node("index", it.index)
            }
        }
        register<LiteralExpression> {
            info {
                value("type", it.type)
                value("text", it.textRepresentation)
            }
        }
        register<NumberOfExpression> { info { node("callee", it.callee) } }
        register<RangeExpression> {
            info {
                node("from", it.from)
                node("to", it.to)
            }
        }

        register<ScalarType> {
            info {
                value("name", it.name)
                value("isNullable", it.isNullable)
            }
        }
        register<GenericType> {
            info {
                value("name", it.name)
                nodes("genericTypes", it.genericTypes)
                value("isNullable", it.isNullable)
            }
        }
        register<LambdaType> {
            info {
                nodes("parameters", it.parameters)
                node("returnType", it.returnType)
                value("isNullable", it.isNullable)
            }
        }
    }

    override fun handleDefault(node: AstNode): NodeInfo = info { value("note", "no inspector registered") }
}
