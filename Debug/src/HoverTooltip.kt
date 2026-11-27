package io.holzfa.bloom.debug

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.IntRect
import androidx.compose.ui.unit.IntSize
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Popup
import androidx.compose.ui.window.PopupPositionProvider

private val TooltipStyle = CodeStyle.copy(fontSize = 12.sp, lineHeight = 16.sp)
private const val MaxValueLength = 240

/** The offset is in original text coordinates, the pointer is relative to the text field */
class HoverState(private val view: ParseView?) {
    var offset by mutableStateOf<Int?>(null)
    var pointer by mutableStateOf(IntOffset.Zero)

    // Derived, so only a change of the node (not every mouse move) recomposes the editor
    val entry: NodeEntry? by derivedStateOf { offset?.let { view?.nodeAt(it) } }
    val messages: List<MessageRegion> by derivedStateOf { offset?.let { view?.messagesAt(it) }.orEmpty() }
}

@Composable
fun HoverTooltip(hover: HoverState) {
    val entry = hover.entry
    val messages = hover.messages
    if (hover.offset == null || (entry == null && messages.isEmpty())) return

    val positionProvider = remember(hover.pointer) { TooltipPosition(hover.pointer) }

    Popup(popupPositionProvider = positionProvider) {
        Surface(shape = RoundedCornerShape(10.dp), color = IslandsDark.frame, shadowElevation = 8.dp) {
            Column(
                modifier = Modifier.padding(12.dp).widthIn(max = 520.dp),
                verticalArrangement = Arrangement.spacedBy(6.dp)
            ) {
                if (entry != null) {
                    Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                        Text(entry.name, style = TooltipStyle.copy(fontWeight = FontWeight.Bold))
                        Text(entry.node.pos.toString(), style = TooltipStyle.copy(color = IslandsDark.comment))
                    }

                    for ((name, value) in entry.info.fields) {
                        val shown = if (value.length > MaxValueLength) value.take(MaxValueLength) + "…" else value
                        Text(
                            text = buildAnnotatedString {
                                withStyle(SpanStyle(color = IslandsDark.comment)) { append("$name: ") }
                                append(shown)
                            },
                            style = TooltipStyle
                        )
                    }

                    val parents = entry.chain.dropLast(1)
                    if (parents.isNotEmpty()) {
                        Text(
                            text = "in " + parents.joinToString(" › ") { it.name },
                            style = TooltipStyle.copy(color = IslandsDark.comment)
                        )
                    }
                }

                for (region in messages) {
                    val message = region.message
                    Text(
                        text = "${message.severity} ${message.stage}: ${message.message}",
                        style = TooltipStyle.copy(color = severityColor(message.severity))
                    )
                }
            }
        }
    }
}

/** Below and right of the pointer, moved so it stays inside the window */
private class TooltipPosition(private val pointer: IntOffset) : PopupPositionProvider {
    override fun calculatePosition(
        anchorBounds: IntRect,
        windowSize: IntSize,
        layoutDirection: LayoutDirection,
        popupContentSize: IntSize
    ): IntOffset {
        var x = anchorBounds.left + pointer.x + 16
        var y = anchorBounds.top + pointer.y + 20

        if (x + popupContentSize.width > windowSize.width) x = windowSize.width - popupContentSize.width
        if (y + popupContentSize.height > windowSize.height) y = anchorBounds.top + pointer.y - popupContentSize.height - 12

        return IntOffset(x.coerceAtLeast(0), y.coerceAtLeast(0))
    }
}
