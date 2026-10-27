package io.holzfa.bloom

import Lexer
import MessageBag
import Parser
import builtIn.KlarLexerHandler
import builtIn.KlarTokenType
import builtin.KlarParserHandler
import java.io.File

fun main() {
    val bag = MessageBag()

    val lexer = Lexer(bag)
    KlarLexerHandler.register(lexer)

    val parser = Parser(bag)
    KlarParserHandler.register(parser)

    val file = "/home/aengstlicher/Code/Kotlin/Bloom/Cli/src/main/kotlin/test.kl"
    val tokens = lexer.tokenize(File(file).readText())
    if (!bag.anyErrors){
        val ast = parser.parse(tokens, file)
    }


    for (m in bag.messages) {
        println(m)
    }
}