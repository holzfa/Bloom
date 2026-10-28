import java.io.Serializable

class MessageBag: Rollback {
    val messages: List<Message<Serializable>>
        field: MutableList<Message<Serializable>> = mutableListOf()

    val anyErrors
        get() = messages.any { it.severity == Severity.Error }
    val anyWarnings
        get() = messages.any { it.severity == Severity.Warning }

    fun add(message: Message<Serializable>) {
        messages.add(message)
    }


    private var marked = listOf<Message<Serializable>>()
    override fun mark() {
        marked = messages
    }
    override fun reset() {
        messages.run {
            clear()
            addAll(marked)
        }
    }
}