package builtin.nodes

import DeclarationNode
import TextRange

data class ModuleDeclaration(val path: Identifier, val declarations: List<DeclarationNode>, override val pos: TextRange): DeclarationNode(pos)