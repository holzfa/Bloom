import java.io.Serializable

class Message<TPos: Serializable>(val message: String, val pos: TPos, val stage: String, val severity: Severity) {
    override fun toString(): String {
        return "[$severity] $stage: '$message' - at $pos"
    }
}

enum class Severity {
    Info,
    Warning,
    Error;

    override fun toString(): String {
        return when(this) {
            Info -> "INFO "
            Warning -> "WARN "
            Error -> "ERROR"
        }
    }
}