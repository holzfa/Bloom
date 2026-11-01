package builtin.nodes.declarations

import DeclarationNode
import TextRange
import builtin.nodes.Identifier

data class ModuleDeclaration(val path: Identifier, val declarations: List<DeclarationNode>, override val pos: TextRange): DeclarationNode(pos)