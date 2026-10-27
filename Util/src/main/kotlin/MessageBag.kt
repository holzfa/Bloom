import java.io.Serializable

class MessageBag {
    val messages: List<Message<Serializable>>
        field: MutableList<Message<Serializable>> = mutableListOf()

    fun add(message: Message<Serializable>) {
        messages.add(message)
    }
}