import tree.AstNode

interface ParserRule<TNode: AstNode> {
    val parser: Parser

    fun tryMatch(reader: ParserReader): TNode?
}