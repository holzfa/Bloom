data class TextRange(val line: Int, val col: Int, val length: Int) {
    override fun toString(): String {
        return "$line:$col($length)"
    }

    companion object {
        fun fromStart(start: Vec2, length: Int): TextRange {
            return TextRange(start.x, start.y, length)
        }
    }
}
