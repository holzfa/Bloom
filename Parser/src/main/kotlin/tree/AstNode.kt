package tree

import TextRange

abstract class AstNode(val pos: TextRange)

abstract class DeclarationNode(pos: TextRange): AstNode(pos)
abstract class StatementNode(pos: TextRange): AstNode(pos)
abstract class ExpressionNode(pos: TextRange): AstNode(pos)