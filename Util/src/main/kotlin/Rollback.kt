import java.util.*

interface Rollback<TState: RollbackState> {
    val states: Stack<TState>

    /** Creates a restore point that can be restored using ´rollBack´ */
    fun save()

    /** Resets the restore point that was created using ´save´ */
    fun rollBack()

    /** Removes the restore point that was created using ´save´ when the operation was a success*/
    fun commit()
}

interface RollbackState