import java.util.*

interface Rollback<TState: RollbackState> {
    val states: Stack<TState>

    fun save()
    fun rollBack()
}

interface RollbackState