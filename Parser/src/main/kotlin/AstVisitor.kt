package tree

import kotlin.collections.mutableMapOf
import kotlin.reflect.KClass

/**
 * This will only match the exact class and no super classes
 * @param TReturn The type that is returned by the visit.
 */
abstract class AstVisitor<TReturn> {
    @PublishedApi internal val handlers: MutableMap<KClass<out AstNode>, (node: AstNode) -> TReturn> = mutableMapOf()

    inline fun <reified T : AstNode> register(noinline handler: (node: T) -> TReturn) {
        @Suppress("UNCHECKED_CAST")
        handlers[T::class] = handler as (AstNode) -> TReturn
    }

    fun visit(node: AstNode): TReturn {
        return handlers[node::class]?.invoke(node) ?: handleDefault(node)
    }

    abstract fun handleDefault(node: AstNode): TReturn
}