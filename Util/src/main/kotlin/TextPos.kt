import java.io.Serializable

data class TextPos(val line: Int, val col: Int, val length: Int): Serializable {
    override fun toString(): String {
        return "$line:$col($length)"
    }

    val vec: Vec2
        get() = Vec2(line, col)

    companion object {
        val unknown = TextPos(-1, -1, 0)

        fun fromStart(start: Vec2, length: Int): TextPos {
            return TextPos(start.x, start.y, length)
        }
    }
}
