package io.holzfa.bloom

import Lexer
import builtIn.KlarLexerHandler
import builtIn.KlarTokenType
import java.io.File

fun main() {
    val lexer = Lexer()
    KlarLexerHandler.register(lexer)

    val file = File("/home/aengstlicher/Code/Kotlin/Bloom/Cli/src/main/kotlin/test.klr").readText()
    val tokens = lexer.tokenize(file)

    for (token in tokens.filter {t -> t.type == DefaultTokenTypes.Unknown }) {
        println(token)
    }
}