import java.io.Serializable

class TextRange(val start: Vec2, val end: Vec2): Serializable {
    override fun toString(): String {
        return "${start.x}:${start.y} - ${end.x}:${end.y}"
    }

    companion object {
        fun create(startLine: Int, startCol: Int, endLine: Int, endCol: Int): TextRange {
            return TextRange(Vec2(startLine, startCol), Vec2(endLine, endCol))
        }

        fun from(start: Vec2, endLine: Int, endCol: Int): TextRange {
            return TextRange(start, Vec2(endLine, endCol))
        }

        fun to(end: Vec2, startLine: Int, startCol: Int): TextRange {
            return TextRange(Vec2(startLine, startCol), end)
        }
    }
}