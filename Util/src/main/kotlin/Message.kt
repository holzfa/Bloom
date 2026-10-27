data class Message(val message: String, val textRange: TextRange, val stage: String, val severity: Severity) {
    override fun toString(): String {
        return "[$severity] $stage: '$message' - at $textRange"
    }
}

enum class Severity {
    Info,
    Warning,
    Error;

    override fun toString(): String {
        return when(this) {
            Info -> "INFO"
            Warning -> "WARN"
            Error -> "EROR"
        }
    }
}