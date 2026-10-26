abstract class CompilerStage(val bag: MessageBag, val name: String) {
    open fun doInfo(message: String, pos: TextRange) {
        bag.add(Message(message, pos, name, Severity.Info))
    }

    open fun doWarn(message: String, pos: TextRange) {
        bag.add(Message(message, pos, name, Severity.Warning))
    }

    open fun doError(message: String, pos: TextRange) {
        bag.add(Message(message, pos, name, Severity.Error))
    }
}