import java.io.Serializable
import java.util.*

data class MessageBagState(val messages: MutableList<Message<Serializable>>): RollbackState

class MessageBag: Rollback<MessageBagState> {
    var state = MessageBagState(mutableListOf())

    val anyErrors
        get() = state.messages.any { it.severity == Severity.Error }
    val anyWarnings
        get() = state.messages.any { it.severity == Severity.Warning }

    fun add(message: Message<Serializable>) {
        state.messages.add(message)
    }

    override val states: Stack<MessageBagState> = Stack()
    override fun save() {
        states.add(state)
    }
    override fun rollBack() {
        state = states.pop()
    }
}