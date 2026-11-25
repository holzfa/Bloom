package io.holzfa.bloom

import Lexer
import MessageBag
import Parser
import builtin.DebugAstPrinter
import builtin.VuraLexerHandler
import builtin.rules.VuraParserHandler
import java.io.File

fun main() {
    val bag = MessageBag()

    try {
        val lexer = Lexer(bag)
        VuraLexerHandler.register(lexer)

        val parser = Parser(bag)
        VuraParserHandler.register(parser)

        val file = "/home/fabio/IdeaProjects/Bloom/Cli/src/main/kotlin/test.vu"
        val tokens = lexer.tokenize(File(file).readText())
        val ast = parser.parse(tokens, file)
        print(DebugAstPrinter.visit(ast))
    } catch (e: Exception) {
        for (m in bag.state.messages) {
            println(m)
        }
        println("Error: ${e.message}")
    }
}