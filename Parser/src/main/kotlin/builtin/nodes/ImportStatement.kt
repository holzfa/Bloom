package builtin.nodes

import TextRange
import tree.StatementNode

data class ImportStatement(val path: String, val selection: List<String>?, override val pos: TextRange): StatementNode(pos)