data class Message(val message: String, val textRange: TextRange, val stage: String, val severity: Severity)

enum class Severity {
    Info,
    Warning,
    Error
}