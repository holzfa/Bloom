package builtin.nodes

import DeclarationNode
import TextRange

data class ImportDeclaration(val path: Identifier, val selection: List<Identifier>?, override val pos: TextRange): DeclarationNode(pos)