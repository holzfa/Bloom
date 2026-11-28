package io.holzfa.bloom.debug.parser

import AstNode
import DeclarationNode
import ExpressionNode
import Message
import Severity
import StatementNode
import TextPos
import TextRange
import TypeNode
import Vec2
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import io.holzfa.bloom.debug.IslandsDark
import io.holzfa.bloom.debug.NodeInfo
import io.holzfa.bloom.debug.NodeInspector
import io.holzfa.bloom.debug.ParseResult
import io.holzfa.bloom.debug.lineStartsOf
import io.holzfa.bloom.debug.offsetOf
import java.io.Serializable

class NodeEntry(
    val node: AstNode,
    val info: NodeInfo,
    val start: Int,
    val end: Int,
    val depth: Int,
    val parent: NodeEntry?
) {
    val name: String get() = node::class.simpleName ?: "Node"
    val isValid: Boolean get() = start in 0..<end

    /** From the outermost node down to this one */
    val chain: List<NodeEntry> get() = generateSequence(this) { it.parent }.toList().reversed()
}

class MessageRegion(val message: Message<Serializable>, val start: Int, val end: Int)

/** Everything the editor needs to know about one parse result, all offsets refer to `result.source` */
class ParseView(result: ParseResult) {
    val entries: List<NodeEntry>
    val messages: List<MessageRegion>
    private val byMessage: Map<Message<Serializable>, MessageRegion>

    init {
        val source = result.source
        val lineStarts = lineStartsOf(source)

        val nodes = ArrayList<NodeEntry>()
        result.root?.let { root ->
            for (child in NodeInspector.visit(root).children) walk(child, null, 0, nodes, lineStarts, source.length)
        }
        entries = nodes

        messages = result.messages.mapNotNull { message ->
            regionOf(message.pos, lineStarts, source.length)?.let { (start, end) -> MessageRegion(message, start, end) }
        }
        byMessage = messages.associateBy { it.message }
    }

    fun regionOf(message: Message<Serializable>): MessageRegion? = byMessage[message]

    /** The innermost node that contains the offset */
    fun nodeAt(offset: Int): NodeEntry? = entries
        .filter { it.isValid && offset >= it.start && offset < it.end }
        .maxWithOrNull(compareBy<NodeEntry> { it.depth }.thenBy { it.start })

    fun messagesAt(offset: Int): List<MessageRegion> = messages.filter { offset >= it.start && offset < it.end }

    /** Node tints (outer to inner), errors last so they win over everything. The hovered node is drawn by the editor */
    fun overlay(base: AnnotatedString): AnnotatedString = buildAnnotatedString {
        append(base)

        for (entry in entries) {
            if (!entry.isValid) continue
            val color = categoryColor(entry.node).copy(alpha = 0.14f + 0.06f * (entry.depth % 3))
            addStyle(SpanStyle(background = color), entry.start, minOf(entry.end, base.length))
        }

        for (region in messages) {
            if (region.message.severity != Severity.Error) continue
            addStyle(SpanStyle(background = IslandsDark.errorBackground), region.start, minOf(region.end, base.length))
        }
    }
}

internal fun categoryColor(node: AstNode): Color = when (node) {
    is DeclarationNode -> IslandsDark.declarationTint
    is StatementNode -> IslandsDark.statementTint
    is ExpressionNode -> IslandsDark.expressionTint
    is TypeNode -> IslandsDark.typeTint
    else -> IslandsDark.otherTint
}

private fun walk(
    node: AstNode,
    parent: NodeEntry?,
    depth: Int,
    out: MutableList<NodeEntry>,
    lineStarts: IntArray,
    length: Int
) {
    val info = NodeInspector.visit(node)
    val start = vecOffset(lineStarts, node.pos.start)
    val end = vecOffset(lineStarts, node.pos.end).let { if (it < 0) it else minOf(it, length) }

    val entry = NodeEntry(node, info, start, end, depth, parent)
    out.add(entry)

    for (child in info.children) walk(child, entry, depth + 1, out, lineStarts, length)
}

private fun vecOffset(lineStarts: IntArray, vec: Vec2): Int {
    val lineStart = lineStarts.getOrNull(vec.x - 1) ?: return -1
    return lineStart + vec.y - 1
}

/** Message positions are either a TextPos (lexer) or a TextRange (parser), ranges can be empty or inverted */
private fun regionOf(pos: Serializable, lineStarts: IntArray, length: Int): Pair<Int, Int>? {
    var start: Int
    var end: Int

    when (pos) {
        is TextPos -> {
            start = offsetOf(lineStarts, pos)
            end = start + pos.length
        }
        is TextRange -> {
            start = vecOffset(lineStarts, pos.start)
            end = vecOffset(lineStarts, pos.end)
        }
        else -> return null
    }

    if (start < 0 || end < 0) return null
    if (end < start) {
        val swap = start
        start = end
        end = swap
    }
    if (end == start) end = start + 1
    if (start >= length) {
        start = length - 1
        end = length
    }
    end = minOf(end, length)

    return if (start in 0 until end) start to end else null
}
