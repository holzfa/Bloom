package io.holzfa.bloom

import Lexer
import builtIn.KlarLexerHandler
import java.io.File
import kotlin.math.asin
import kotlin.system.exitProcess

fun main() {
    val lexer = Lexer()
    KlarLexerHandler.register(lexer)

    val file = File("/home/aengstlicher/Code/Kotlin/Bloom/Cli/src/main/kotlin/test.klr").readText()
    val test = lexer.tokenize(file)

    for (token in test) {
        println(token)
    }
}