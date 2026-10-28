import java.io.Serializable

abstract class CompilerStage<TPos: Serializable>(val bag: MessageBag, private val name: String) {
    open fun doInfo(message: String, pos: TPos) {
        bag.add(Message(message, pos, name, Severity.Info))
    }

    open fun doWarn(message: String, pos: TPos) {
        bag.add(Message(message, pos, name, Severity.Warning))
    }

    open fun doError(message: String, pos: TPos) {
        bag.add(Message(message, pos, name, Severity.Error))
    }
}