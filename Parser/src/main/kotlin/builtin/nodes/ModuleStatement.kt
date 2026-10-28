package builtin.nodes

import TextRange
import tree.StatementNode

data class ModuleStatement(val path: String, override val pos: TextRange): StatementNode(pos)