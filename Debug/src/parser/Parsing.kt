package io.holzfa.bloom.debug

import FileNode
import Lexer
import Message
import MessageBag
import Parser
import builtin.VuraLexerHandler
import builtin.rules.VuraParserHandler
import io.holzfa.bloom.debug.parser.ParseView
import java.io.Serializable

class ParseResult(
    val source: String,
    val root: FileNode?,
    val messages: List<Message<Serializable>>,
    /** Set when the lexer or parser threw, `root` is null in that case */
    val failure: String?
) {
    val view: ParseView by lazy(LazyThreadSafetyMode.NONE) { ParseView(this) }
}

fun runParse(source: String, filePath: String): ParseResult {
    val bag = MessageBag()

    return try {
        val lexer = Lexer(bag)
        VuraLexerHandler.register(lexer)

        val parser = Parser(bag)
        VuraParserHandler.register(parser)

        val root = parser.parse(lexer.tokenize(source), filePath)
        ParseResult(source, root, bag.state.messages.toList(), null)
    } catch (e: Throwable) {
        ParseResult(source, null, bag.state.messages.toList(), "${e::class.simpleName}: ${e.message}")
    }
}
