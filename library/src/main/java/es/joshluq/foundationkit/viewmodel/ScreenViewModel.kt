package es.joshluq.foundationkit.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

/**
 * Base class for ViewModels that follow the MVI pattern.
 * @param State The type of the UI state.
 * @param Event The type of the UI events.
 * @param Effect The type of the side effects.
 */
abstract class ScreenViewModel<State : UiState, Event : UiEvent, Effect : UiEffect> : ViewModel() {
    private val _state = MutableStateFlow(createInitialState())

    /**
     * The current state of the screen.
     */
    val state: StateFlow<State> = _state.asStateFlow()

    /**
     * Creates the initial state for the screen.
     * @return The initial state.
     */
    protected abstract fun createInitialState(): State

    /**
     * Updates the current state.
     * @param function A function that takes the current state and returns the new state.
     */
    protected fun updateState(function: (State) -> State) {
        _state.update(function)
    }

    /**
     * Sends an event to the ViewModel.
     * @param event The event to send.
     */
    fun sendEvent(event: Event) {
        handleEvent(event)
    }

    /**
     * Handles incoming events from the UI.
     * @param event The event to handle.
     */
    protected abstract fun handleEvent(event: Event)

    private val effectChannel = Channel<Effect>()

    /**
     * A flow of side effects that should be handled by the UI.
     */
    val effects = effectChannel.receiveAsFlow()

    /**
     * Launches a side effect.
     * @param effect The effect to launch.
     */
    protected fun launchEffect(effect: Effect) {
        viewModelScope.launch {
            effectChannel.send(effect)
        }
    }
}
