package io.holzfa.bloom

import Lexer
import MessageBag
import Parser
import builtIn.KlarLexerHandler
import builtIn.KlarTokenType
import java.io.File

fun main() {
    val bag = MessageBag()
    val lexer = Lexer(bag)
    val parser = Parser(bag)
    KlarLexerHandler.register(lexer)

    val file = File("/home/aengstlicher/Code/Kotlin/Bloom/Cli/src/main/kotlin/test.klr").readText()
    val tokens = lexer.tokenize(file)

    for (m in bag.messages) {
        println(m)
    }
}