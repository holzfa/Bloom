package io.holzfa.bloom

import Lexer
import MessageBag
import Parser
import builtin.VuraLexerHandler
import builtin.VuraParserHandler
import java.io.File

fun main() {
    val bag = MessageBag()

    val lexer = Lexer(bag)
    VuraLexerHandler.register(lexer)

    val parser = Parser(bag)
    VuraParserHandler.register(parser)

    val file = "/home/aengstlicher/Code/Kotlin/Bloom/Cli/src/main/kotlin/test.vu"
    val tokens = lexer.tokenize(File(file).readText())
    if (!bag.anyErrors) {
        val ast = parser.parse(tokens, file)
        for (node in ast.nodes) {
            println(node)
        }
    }

    for (m in bag.messages) {
        println(m)
    }
}