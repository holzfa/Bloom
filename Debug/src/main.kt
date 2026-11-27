package io.holzfa.bloom.debug

import androidx.compose.runtime.remember
import androidx.compose.ui.unit.DpSize
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Window
import androidx.compose.ui.window.application
import androidx.compose.ui.window.rememberWindowState
import io.github.vinceglb.filekit.FileKit

fun main() {
    FileKit.init(appId = "io.holzfa.bloom.debug")

    application {
        val editor = remember { EditorState() }

        Window(
            onCloseRequest = ::exitApplication,
            title = editor.title,
            state = rememberWindowState(size = DpSize(1100.dp, 850.dp))
        ) {
            AstVisualizer(editor)
        }
    }
}