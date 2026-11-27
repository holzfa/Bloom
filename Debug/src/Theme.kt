package io.holzfa.bloom.debug

import androidx.compose.ui.graphics.Color

object IslandsDark {
    val frame = Color(0xFF26282C)
    val editorBackground = Color(0xFF191A1C)

    val text = Color(0xFFBCBEC4)
    val lineNumber = Color(0xFF4B5059)
    val selection = Color(0xFF214283)
    val error = Color(0xFFF75464)
    val warning = Color(0xFFE5B95C)

    val keyword = Color(0xFFCF8E6D)
    val string = Color(0xFF6AAB73)
    val number = Color(0xFF2AACB8)
    val comment = Color(0xFF7A7E85)
    val documentation = Color(0xFF5F826B)

    val accent = Color(0xFF3574F0)
    val button = Color(0xFF393B40)
    val foldPlaceholder = Color(0x33FFFFFF)

    // Base colors of the AST node regions, the alpha is applied per nesting depth
    val declarationTint = Color(0xFF3574F0)
    val statementTint = Color(0xFF6AAB73)
    val expressionTint = Color(0xFFE5B95C)
    val typeTint = Color(0xFFC77DBB)
    val otherTint = Color(0xFF8A8F98)

    val errorBackground = Color(0x80F75464)
}
