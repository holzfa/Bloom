package io.holzfa.bloom.debug

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3ExpressiveApi
import androidx.compose.material3.FilledTonalButton
import androidx.compose.material3.MaterialExpressiveTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.darkColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.key.Key
import androidx.compose.ui.input.key.KeyEventType
import androidx.compose.ui.input.key.isCtrlPressed
import androidx.compose.ui.input.key.isShiftPressed
import androidx.compose.ui.input.key.key
import androidx.compose.ui.input.key.onPreviewKeyEvent
import androidx.compose.ui.input.key.type
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3ExpressiveApi::class)
@Composable
fun AstVisualizer(editor: EditorState) {
    val scope = rememberCoroutineScope()

    val colors = darkColorScheme(
        primary = IslandsDark.accent,
        background = IslandsDark.frame,
        surface = IslandsDark.frame,
        onBackground = IslandsDark.text,
        onSurface = IslandsDark.text,
        secondaryContainer = IslandsDark.button,
        onSecondaryContainer = IslandsDark.text
    )

    fun open() {
        scope.launch { FileDialogs.open()?.let { editor.load(it) } }
    }

    fun saveAs() {
        scope.launch { FileDialogs.save(editor.file?.nameWithoutExtension ?: "untitled")?.let { editor.save(it) } }
    }

    fun save() {
        if (editor.file != null) editor.save() else saveAs()
    }

    fun parse() {
        scope.launch { editor.parse() }
    }

    MaterialExpressiveTheme(colorScheme = colors) {
        Surface(
            modifier = Modifier
                .fillMaxSize()
                .onPreviewKeyEvent { event ->
                    if (event.type == KeyEventType.KeyDown && event.isCtrlPressed) {
                        when (event.key) {
                            Key.O -> { open(); true }
                            Key.S -> { if (event.isShiftPressed) saveAs() else save(); true }
                            Key.Enter -> { parse(); true }
                            else -> false
                        }
                    } else {
                        false
                    }
                },
            color = IslandsDark.frame
        ) {
            Column(Modifier.fillMaxSize().padding(8.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
                Toolbar(editor, onOpen = ::open, onSave = ::save, onSaveAs = ::saveAs, onParse = ::parse)

                Surface(
                    modifier = Modifier.weight(1f).fillMaxWidth(),
                    shape = RoundedCornerShape(14.dp),
                    color = IslandsDark.editorBackground
                ) {
                    CodeEditor(editor)
                }

                MessagePanel(editor, Modifier.fillMaxWidth().height(180.dp))
            }
        }
    }
}

@Composable
private fun Toolbar(
    editor: EditorState,
    onOpen: () -> Unit,
    onSave: () -> Unit,
    onSaveAs: () -> Unit,
    onParse: () -> Unit
) {
    Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(8.dp)) {
        FilledTonalButton(onClick = onOpen) { Text("Open") }
        FilledTonalButton(onClick = onSave) { Text("Save") }
        TextButton(onClick = onSaveAs) { Text("Save as…") }
        Button(onClick = onParse, enabled = !editor.isParsing) { Text("Parse") }

        Spacer(Modifier.weight(1f))

        val message = editor.message
        Text(
            text = message ?: editor.file?.absolutePath ?: "No file",
            color = if (message != null) IslandsDark.error else IslandsDark.comment,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis
        )
    }
}