interface ParserRule {
    val parser: Parser
}


interface DeclarationRule: ParserRule {
    fun tryDeclaration(reader: ParserReader): DeclarationNode?
}

interface StatementRule: ParserRule {
    fun tryStatement(reader: ParserReader): StatementNode?
}

interface PreExpressionRule: ParserRule {
    fun tryExpression(reader: ParserReader): ExpressionNode?
}

interface PostExpressionRule: ParserRule {
    fun tryExpression(reader: ParserReader, expression: ExpressionNode): ExpressionNode?
}