import java.io.Serializable

class TextRange(val start: Vec2, val end: Vec2, val filePath: String): Serializable {
    override fun toString(): String {
        return "${start.x}:${start.y}-${end.x}:${end.y}"
    }

    companion object {
        val unknown = TextRange(Vec2(-1, -1), Vec2(-1, -1), "")

        fun create(startLine: Int, startCol: Int, endLine: Int, endCol: Int, filePath: String): TextRange {
            return TextRange(Vec2(startLine, startCol), Vec2(endLine, endCol), filePath)
        }

        fun from(start: Vec2, endLine: Int, endCol: Int, filePath: String): TextRange {
            return TextRange(start, Vec2(endLine, endCol), filePath)
        }

        fun to(end: Vec2, startLine: Int, startCol: Int, filePath: String): TextRange {
            return TextRange(Vec2(startLine, startCol), end, filePath)
        }
    }
}