import tree.AstNode
import tree.ErrorFile
import tree.FileNode

class Parser(bag: MessageBag = MessageBag()) : CompilerStage<TextRange>(bag, "Parser") {
    private val rules = mutableListOf<ParserRule<AstNode>>()

    fun registerRule(rule: ParserRule<AstNode>) {
        rules.add(rule)
    }

    fun parse(tokens: List<Token>, filePath: String): FileNode {
        val nodes = mutableListOf<AstNode>()
        val reader = TokenReader(tokens, filePath)
        val fileRange = reader.range(Vec2(1,1), tokens.last().pos.vec)

        if (!rules.any()) {
            doError("No parser rules were registered.", fileRange)
            return ErrorFile(fileRange)
        }

        while (!reader.isAtEnd) {
            var matched = false

            for (rule in rules) {
                val node = rule.tryMatch(reader)
                if (node != null) {
                    matched = true
                    nodes.add(node)
                    break
                }
            }

            if (!matched) {
                val range = reader.range(reader.currentPos, reader.nextPos)
                val badToken = reader.advance()
                doError("Unknown token '$badToken'", range)
            }
        }

        return FileNode(nodes, fileRange)
    }
}