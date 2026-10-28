import tree.AstNode
import tree.ErrorFile
import tree.FileNode

class Parser(bag: MessageBag = MessageBag()) : CompilerStage<TextRange>(bag, "Parser") {
    private val rules = mutableListOf<ParserRule<*>>()

    fun registerRule(rule: ParserRule<*>) {
        rules.add(rule)
    }

    fun parse(tokens: List<Token>, filePath: String): FileNode {
        val nodes = mutableListOf<AstNode>()
        val reader = ParserReader(tokens, filePath, bag)
        val fileRange = reader.range(Vec2(1,1), tokens.last().pos.endVec)

        if (!rules.any()) {
            doError("No parser rules were registered.", fileRange)
            return ErrorFile(fileRange)
        }

        while (!reader.isAtEnd) {
            var matched = false

            for (rule in rules) {
                reader.mark()
                val node = rule.tryMatch(reader)
                if (node != null) {
                    matched = true
                    nodes.add(node)
                    break
                }
                reader.reset()
            }

            if (!matched) {
                val badToken = reader.advance()
                val range = reader.range(badToken.pos)
                // doError("Unknown token '$badToken'", range)
            }
        }

        return FileNode(nodes, fileRange)
    }
}