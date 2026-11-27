package io.holzfa.bloom.debug

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.text.TextRange
import androidx.compose.ui.text.input.TextFieldValue
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.File
import java.io.IOException

/** A request to jump to a region of the text, a new instance is created for every request */
class RevealRequest(val start: Int, val end: Int)

class EditorState {
    var value by mutableStateOf(TextFieldValue(""))
        private set
    var file by mutableStateOf<File?>(null)
        private set
    /** Offsets of the opening brackets of the folded regions */
    var folded by mutableStateOf<Set<Int>>(emptySet())
        private set
    var message by mutableStateOf<String?>(null)
        private set

    var parseResult by mutableStateOf<ParseResult?>(null)
        private set
    var isParsing by mutableStateOf(false)
        private set
    var revealRequest by mutableStateOf<RevealRequest?>(null)
        private set

    private var savedText by mutableStateOf("")

    val isDirty: Boolean get() = value.text != savedText

    val title: String
        get() = (file?.name ?: "Untitled") + (if (isDirty) " ●" else "") + " - Vura Ast Debugger"

    fun edit(new: TextFieldValue) {
        if (new.text != value.text) folded = shiftFolds(value.text, new.text, folded)
        value = new
    }

    fun load(target: File) {
        try {
            val text = target.readText()
            value = TextFieldValue(text)
            savedText = text
            file = target
            folded = emptySet()
            parseResult = null
            message = null
        } catch (e: IOException) {
            message = "Could not open ${target.name}: ${e.message}"
        }
    }

    /** Saves to the given file or to the current one, returns false if there is no target or writing failed */
    fun save(target: File? = file): Boolean {
        if (target == null) return false

        return try {
            target.writeText(value.text)
            file = target
            savedText = value.text
            message = null
            true
        } catch (e: IOException) {
            message = "Could not save ${target.name}: ${e.message}"
            false
        }
    }

    suspend fun parse() {
        if (isParsing) return
        isParsing = true

        try {
            val source = value.text
            val path = file?.path ?: "untitled.vu"
            parseResult = withContext(Dispatchers.Default) { runParse(source, path) }
        } finally {
            isParsing = false
        }
    }

    fun toggleFold(open: Int) {
        folded = if (open in folded) folded - open else folded + open
    }

    fun unfold(opens: Collection<Int>) {
        if (opens.isNotEmpty()) folded = folded - opens.toSet()
    }

    fun select(start: Int, end: Int) {
        val length = value.text.length
        value = value.copy(selection = TextRange(start.coerceIn(0, length), end.coerceIn(0, length)))
    }

    fun reveal(start: Int, end: Int) {
        revealRequest = RevealRequest(start, end)
    }

    fun consumeReveal() {
        revealRequest = null
    }
}
