package es.joshluq.foundationkit.coroutines

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.cancel

/**
 * Interface that defines a component that owns a [CoroutineScope].
 *
 * Any component (Service, Manager, Repository) that launches long-running tasks
 * should implement this interface to ensure proper lifecycle management and resource cleanup.
 */
interface ScopeOwner {
    /**
     * The [CoroutineScope] associated with this component.
     */
    val scope: CoroutineScope

    /**
     * Cancels the [scope] and all its children.
     * This should be called when the component is being destroyed to prevent memory leaks.
     */
    fun cancelAll() {
        scope.cancel()
    }
}
