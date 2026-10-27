import java.io.Serializable

class MessageBag {
    val messages: List<Message<Serializable>>
        field: MutableList<Message<Serializable>> = mutableListOf()

    val anyErrors
        get() = messages.any { it.severity == Severity.Error }
    val anyWarnings
        get() = messages.any { it.severity == Severity.Warning }

    fun add(message: Message<Serializable>) {
        messages.add(message)
    }
}