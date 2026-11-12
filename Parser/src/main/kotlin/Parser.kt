    class Parser(bag: MessageBag = MessageBag()) : MessageCollector<TextRange>(bag, "Parser") {
        private val rules = mutableListOf<ParserRule>()

        /** Registers a rule that is used to parse an ASTNode */
        fun registerRule(rule: ParserRule) {
            rules.add(rule)
        }

        /** Parses a pre-lexed file and returns its AST tree */
        fun parse(tokens: List<Token>, filePath: String): FileNode {
            val reader = ParserReader(tokens, filePath, bag)
            val fileRange = reader.range(Vec2(1,1), tokens.lastOrNull()?.pos?.endVec ?: Vec2(1,1))

            if (rules.isEmpty()) {
                doError("No parser rules were registered.", fileRange)
                return ErrorFile(fileRange)
            }

            val nodes = requestDeclarationsUntil(reader) { false }

            return FileNode(nodes, fileRange)
        }

        /** Requests a declaration node to be parsed and returns it if parsing was successful */
        fun requestDeclaration(reader: ParserReader): DeclarationNode? {
            for (rule in rules) {
                reader.mark()
                val node = rule.tryDeclaration(reader)
                if (node != null) {
                    return node
                }
                reader.reset()
            }

            return null
        }

        /** Requests a statement node to be parsed and returns it if parsing was successful */
        fun requestStatement(reader: ParserReader): StatementNode? {
            for (rule in rules) {
                reader.mark()
                val node = rule.tryStatement(reader)
                if (node != null) {
                    return node
                }
                reader.reset()
            }

            return null
        }

        /** Requests an expression node to be parsed and returns it if parsing was successful */
        fun requestExpression(reader: ParserReader): ExpressionNode? {
            for (rule in rules) {
                reader.mark()
                val node = rule.tryExpression(reader)
                if (node != null) {
                    return node
                }
                reader.reset()
            }

            return null
        }


        /** Requests declaration nodes to be parsed until the condition is true *or* the reader has reached its end */
        fun requestDeclarationsUntil(reader: ParserReader, condition: (reader: ParserReader) -> Boolean): List<DeclarationNode> {
            val nodes = mutableListOf<DeclarationNode>()

            while (!reader.isAtEnd && !condition(reader)) {
                val startPos = reader.currentPos
                val match = requestDeclaration(reader)

                if (match == null) {
                    val badToken = reader.advance()
                    val range = reader.range(badToken.pos)
                    doError("Unknown token '$badToken'", range)
                } else {
                    if (reader.currentPos == startPos) {
                        val badToken = reader.advance()
                        error("Parser stuck: declaration consumed no tokens ${reader.range(badToken.pos)}")
                    } else {
                        nodes.add(match)
                    }
                }
            }

            return nodes
        }

        /** Requests statement nodes to be parsed until the condition is true *or* the reader has reached its end */
        fun requestStatementsUntil(reader: ParserReader, condition: (reader: ParserReader) -> Boolean): List<StatementNode> {
            val nodes = mutableListOf<StatementNode>()

            while (!reader.isAtEnd && !condition(reader)) {
                val startPos = reader.currentPos
                val match = requestStatement(reader)

                if (match == null) {
                    val badToken = reader.advance()
                    val range = reader.range(badToken.pos)
                    doError("Unknown token '$badToken'", range)
                } else {
                    if (reader.currentPos == startPos) {
                        val position = reader.advance().pos
                        error("Parser stuck: statement consumed no tokens ${reader.range(position)}")
                    } else {
                        nodes.add(match)
                    }
                }
            }

            return nodes
        }
    }