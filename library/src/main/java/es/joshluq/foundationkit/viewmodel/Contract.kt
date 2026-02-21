package es.joshluq.foundationkit.viewmodel

/**
 * Base interface for UI State.
 * Implementations should be data classes and consist of immutable properties to ensure
 * stability in Compose (enabling Strong Skipping Mode).
 */
interface UiState

/**
 * Base interface for UI Events (user actions).
 * Implementations should be data objects or data classes.
 */
interface UiEvent

/**
 * Base interface for UI Side Effects (navigation, toasts, etc.).
 * Implementations should be data objects or data classes.
 */
interface UiEffect
