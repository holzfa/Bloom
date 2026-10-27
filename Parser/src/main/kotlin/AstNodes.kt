package tree

import TextRange

abstract class AstNode(open val pos: TextRange)
open class FileNode(val nodes: List<AstNode>, pos: TextRange): AstNode(pos)

abstract class DeclarationNode(pos: TextRange): AstNode(pos)
abstract class StatementNode(pos: TextRange): AstNode(pos)
abstract class ExpressionNode(pos: TextRange): AstNode(pos)
abstract class TypeNode(pos: TextRange): AstNode(pos)

data class ErrorDeclaration(override val pos: TextRange): DeclarationNode(pos)
data class ErrorStatement(override val pos: TextRange): StatementNode(pos)
data class ErrorExpression(override val pos: TextRange): ExpressionNode(pos)
data class ErrorType(override val pos: TextRange): TypeNode(pos)
data class ErrorFile(override val pos: TextRange): FileNode(listOf(), pos)


