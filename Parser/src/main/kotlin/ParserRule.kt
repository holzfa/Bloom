abstract class ParserRule {
    protected abstract val parser: Parser

    open fun tryDeclaration(reader: ParserReader): DeclarationNode? = null
    open fun tryStatement(reader: ParserReader): StatementNode? = null
    open fun tryExpression(reader: ParserReader): ExpressionNode? = null
}