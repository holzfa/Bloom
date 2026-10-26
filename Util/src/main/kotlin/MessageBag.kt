class MessageBag {
    val messages: List<Message>
        field: MutableList<Message> = mutableListOf<Message>()

    fun add(message: Message) {
        messages.add(message)
    }
}