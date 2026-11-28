package io.holzfa.bloom.debug

import androidx.compose.foundation.HorizontalScrollbar
import androidx.compose.foundation.ScrollbarStyle
import androidx.compose.foundation.VerticalScrollbar
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.rememberScrollbarAdapter
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.selection.LocalTextSelectionColors
import androidx.compose.foundation.text.selection.TextSelectionColors
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.drawscope.translate
import androidx.compose.ui.input.pointer.PointerEventPass
import androidx.compose.ui.input.pointer.PointerEventType
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.TextLayoutResult
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.input.OffsetMapping
import androidx.compose.ui.text.input.TransformedText
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.LineHeightStyle
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import io.holzfa.bloom.debug.parser.categoryColor
import kotlin.math.roundToInt

internal val LineHeight = 20.sp
internal val ContentPadding = 8.dp
private val TextPaddingX = 12.dp

// A fixed, centered line height keeps the gutter rows aligned with the text lines
internal val CodeStyle = TextStyle(
    color = IslandsDark.text,
    fontFamily = FontFamily.Monospace,
    fontSize = 14.sp,
    lineHeight = LineHeight,
    lineHeightStyle = LineHeightStyle(
        alignment = LineHeightStyle.Alignment.Center,
        trim = LineHeightStyle.Trim.None
    )
)

internal val DarkScrollbarStyle = ScrollbarStyle(
    minimalHeight = 16.dp,
    thickness = 8.dp,
    shape = RoundedCornerShape(4.dp),
    hoverDurationMillis = 300,
    unhoverColor = Color.White.copy(alpha = 0.16f),
    hoverColor = Color.White.copy(alpha = 0.40f)
)

@Composable
fun CodeEditor(editor: EditorState) {
    val density = LocalDensity.current
    val value = editor.value
    val analysis = remember(value.text) { SourceAnalysis(value.text) }

    // A result of an older text has stale offsets, it is only applied while the text is unchanged
    val view = editor.parseResult?.takeIf { it.source == value.text }?.view
    val hover = remember(view) { HoverState(view) }

    val hidden = remember(analysis, editor.folded) { hiddenRanges(analysis.regions, editor.folded) }
    val gutter = remember(analysis, hidden, editor.folded) { gutterLines(analysis, hidden, editor.folded) }

    // The hovered node is not part of the text transformation, a new transformation re-lays out the field and disturbs the scroll
    val displayed = remember(analysis, view) {
        view?.overlay(analysis.highlighted) ?: analysis.highlighted
    }
    val transformed = remember(displayed, hidden) { foldedText(displayed, hidden) }
    val transformation = remember(analysis, transformed) {
        VisualTransformation { text ->
            if (text.text == analysis.source) transformed else TransformedText(text, OffsetMapping.Identity)
        }
    }

    val layoutHolder = remember { arrayOfNulls<TextLayoutResult>(1) }
    val focusRequester = remember { FocusRequester() }
    val verticalScroll = rememberScrollState()
    val horizontalScroll = rememberScrollState()
    val selectionColors = TextSelectionColors(handleColor = IslandsDark.text, backgroundColor = IslandsDark.selection)

    val paddingX = with(density) { TextPaddingX.toPx() }
    val paddingY = with(density) { ContentPadding.toPx() }

    CompositionLocalProvider(LocalTextSelectionColors provides selectionColors) {
        BoxWithConstraints(Modifier.fillMaxSize()) {
            val viewportHeight = maxHeight
            val viewportPx = constraints.maxHeight

            // Unfolds what hides the target, selects it and scrolls it into the upper third of the view
            LaunchedEffect(editor.revealRequest) {
                val request = editor.revealRequest ?: return@LaunchedEffect
                val start = request.start.coerceIn(0, value.text.length)

                val blocking = analysis.regions.filter { it.open in editor.folded && it.open < start && start < it.close }
                editor.unfold(blocking.map { it.open })
                editor.select(request.start, request.end)

                val folded = editor.folded
                val lines = gutterLines(analysis, hiddenRanges(analysis.regions, folded), folded)
                val lineIndex = lineIndexOf(analysis.lineStarts, start)
                val row = lines.indexOfLast { it.number - 1 <= lineIndex }.coerceAtLeast(0)
                val target = row * with(density) { LineHeight.toPx() } - viewportPx / 3f

                focusRequester.requestFocus()
                verticalScroll.animateScrollTo(target.roundToInt().coerceAtLeast(0))
                editor.consumeReveal()
            }

            Row(Modifier.fillMaxSize().verticalScroll(verticalScroll)) {
                Gutter(
                    lines = gutter,
                    lineCount = analysis.lineStarts.size,
                    onToggle = editor::toggleFold,
                    modifier = Modifier.heightIn(min = viewportHeight)
                )

                BoxWithConstraints(Modifier.weight(1f)) {
                    val viewportWidth = maxWidth

                    // The horizontal scroll gives the field unbounded width, so lines are never wrapped
                    Box(Modifier.horizontalScroll(horizontalScroll)) {
                        // The hover state is read while drawing, so a hover change only redraws and never recomposes
                        Box(
                            Modifier.drawBehind {
                                val entry = hover.entry ?: return@drawBehind
                                val layout = layoutHolder[0] ?: return@drawBehind
                                if (!entry.isValid) return@drawBehind

                                val length = layout.layoutInput.text.length
                                val start = transformed.offsetMapping.originalToTransformed(entry.start).coerceIn(0, length)
                                val end = transformed.offsetMapping.originalToTransformed(entry.end).coerceIn(0, length)
                                if (start >= end) return@drawBehind

                                translate(paddingX, paddingY) {
                                    drawPath(
                                        layout.getPathForRange(start, end),
                                        categoryColor(entry.node).copy(alpha = 0.55f)
                                    )
                                }
                            }
                        ) {
                            BasicTextField(
                                value = value,
                                onValueChange = editor::edit,
                                modifier = Modifier
                                    .widthIn(min = viewportWidth)
                                    .heightIn(min = viewportHeight)
                                    .focusRequester(focusRequester)
                                    .pointerInput(transformed, hover, paddingX, paddingY) {
                                        awaitPointerEventScope {
                                            while (true) {
                                                val event = awaitPointerEvent(PointerEventPass.Initial)
                                                val position = event.changes.first().position

                                                when (event.type) {
                                                    PointerEventType.Move, PointerEventType.Enter -> {
                                                        hover.offset = originalOffsetAt(
                                                            layoutHolder[0],
                                                            transformed.offsetMapping,
                                                            position.x - paddingX,
                                                            position.y - paddingY
                                                        )
                                                        hover.pointer = IntOffset(position.x.roundToInt(), position.y.roundToInt())
                                                    }

                                                    PointerEventType.Exit -> hover.offset = null
                                                }
                                            }
                                        }
                                    },
                                textStyle = CodeStyle,
                                cursorBrush = SolidColor(IslandsDark.text),
                                visualTransformation = transformation,
                                onTextLayout = { layoutHolder[0] = it },
                                decorationBox = { innerTextField ->
                                    Box(Modifier.padding(horizontal = TextPaddingX, vertical = ContentPadding)) {
                                        if (value.text.isEmpty()) {
                                            Text(
                                                text = "Choose a vura file to debug...",
                                                style = CodeStyle.copy(color = IslandsDark.comment)
                                            )
                                        }
                                        innerTextField()
                                    }
                                }
                            )

                            HoverTooltip(hover)
                        }
                    }
                }
            }

            VerticalScrollbar(
                adapter = rememberScrollbarAdapter(verticalScroll),
                modifier = Modifier.align(Alignment.CenterEnd).fillMaxHeight(),
                style = DarkScrollbarStyle
            )
            HorizontalScrollbar(
                adapter = rememberScrollbarAdapter(horizontalScroll),
                modifier = Modifier.align(Alignment.BottomStart).fillMaxWidth(),
                style = DarkScrollbarStyle
            )
        }
    }
}

/** Position relative to the text (padding already removed) to the offset in the original text, null outside the text */
private fun originalOffsetAt(layout: TextLayoutResult?, mapping: OffsetMapping, x: Float, y: Float): Int? {
    if (layout == null || x < 0f || y < 0f || y >= layout.size.height) return null

    val line = layout.getLineForVerticalPosition(y)
    if (x > layout.getLineRight(line)) return null

    return mapping.transformedToOriginal(layout.getOffsetForPosition(Offset(x, y)))
}

@Composable
private fun Gutter(lines: List<GutterLine>, lineCount: Int, onToggle: (Int) -> Unit, modifier: Modifier) {
    val rowHeight = with(LocalDensity.current) { LineHeight.toDp() }
    val numberWidth = (lineCount.toString().length * 9).dp
    val numberStyle = CodeStyle.copy(color = IslandsDark.lineNumber, textAlign = TextAlign.End)

    Column(modifier.padding(start = 12.dp, top = ContentPadding, bottom = ContentPadding)) {
        for (line in lines) {
            val region = line.region

            Row(Modifier.height(rowHeight), verticalAlignment = Alignment.CenterVertically) {
                Text(text = line.number.toString(), style = numberStyle, modifier = Modifier.width(numberWidth))

                Box(
                    modifier = Modifier
                        .width(20.dp)
                        .fillMaxHeight()
                        .then(if (region != null) Modifier.clickable { onToggle(region.open) } else Modifier),
                    contentAlignment = Alignment.Center
                ) {
                    if (region != null) {
                        Text(
                            text = if (line.folded) "▸" else "▾",
                            color = IslandsDark.lineNumber,
                            fontSize = 12.sp
                        )
                    }
                }
            }
        }
    }
}
