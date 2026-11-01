package builtin.nodes.declarations

import DeclarationNode
import TextRange
import builtin.nodes.Identifier

data class ImportDeclaration(val path: Identifier, val selection: List<Identifier>?, override val pos: TextRange): DeclarationNode(pos)