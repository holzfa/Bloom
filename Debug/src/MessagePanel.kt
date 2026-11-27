package io.holzfa.bloom.debug

import Severity
import androidx.compose.foundation.VerticalScrollbar
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.rememberScrollbarAdapter
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

private val PanelStyle = CodeStyle.copy(fontSize = 13.sp)

internal fun severityColor(severity: Severity): Color = when (severity) {
    Severity.Error -> IslandsDark.error
    Severity.Warning -> IslandsDark.warning
    Severity.Info -> IslandsDark.comment
}

@Composable
fun MessagePanel(editor: EditorState, modifier: Modifier = Modifier) {
    val result = editor.parseResult
    val stale = result != null && result.source != editor.value.text

    Surface(modifier, shape = RoundedCornerShape(14.dp), color = IslandsDark.editorBackground) {
        Column(Modifier.fillMaxSize().padding(12.dp), verticalArrangement = Arrangement.spacedBy(6.dp)) {
            when {
                editor.isParsing -> Text("Parsing…", style = PanelStyle.copy(color = IslandsDark.comment))

                result == null -> Text(
                    "Press Parse (Ctrl+Enter) to run the lexer and the parser.",
                    style = PanelStyle.copy(color = IslandsDark.comment)
                )

                else -> {
                    val errors = result.messages.count { it.severity == Severity.Error }
                    val warnings = result.messages.count { it.severity == Severity.Warning }
                    val summary = "$errors errors, $warnings warnings, ${result.messages.size} messages" +
                            if (stale) " - outdated, the text changed since the last parse" else ""
                    Text(summary, style = PanelStyle.copy(color = IslandsDark.comment))

                    if (result.failure != null) {
                        Text("Parsing aborted: ${result.failure}", style = PanelStyle.copy(color = IslandsDark.error))
                    }

                    val listState = rememberLazyListState()
                    Box(Modifier.weight(1f).fillMaxWidth()) {
                        LazyColumn(Modifier.fillMaxSize(), state = listState) {
                            items(result.messages) { message ->
                                val region = if (stale) null else result.view.regionOf(message)

                                Row(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .clickable(enabled = region != null) {
                                            if (region != null) editor.reveal(region.start, region.end)
                                        }
                                        .padding(horizontal = 4.dp, vertical = 2.dp),
                                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Text(message.severity.toString(), style = PanelStyle.copy(color = severityColor(message.severity)))
                                    Text(
                                        text = "${message.stage}: ${message.message}",
                                        style = PanelStyle,
                                        modifier = Modifier.weight(1f)
                                    )
                                    Text(message.pos.toString(), style = PanelStyle.copy(color = IslandsDark.comment))
                                }
                            }
                        }

                        VerticalScrollbar(
                            adapter = rememberScrollbarAdapter(listState),
                            modifier = Modifier.align(Alignment.CenterEnd).fillMaxHeight(),
                            style = DarkScrollbarStyle
                        )
                    }
                }
            }
        }
    }
}
