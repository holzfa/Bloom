import java.io.Serializable

data class TextPos(val line: Int, val col: Int, val length: Int): Serializable {
    override fun toString(): String {
        return "$line:$col($length)"
    }

    companion object {
        fun fromStart(start: Vec2, length: Int): TextPos {
            return TextPos(start.x, start.y, length)
        }
    }
}
