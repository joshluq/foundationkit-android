---
name: clean-mvi-architect
description: >-
  Designs, implements, and tests Clean Architecture UseCases and MVI ViewModels in FoundationKit.
  Use this skill whenever creating UseCase, FlowUseCase, ScreenViewModel, UiState, UiEvent,
  or UiEffect implementations, or when testing unidirectional data flow with Coroutines and Flow.
---

# Clean MVI Architect

This skill provides step-by-step guidance for implementing Clean Architecture operations (`UseCase`, `FlowUseCase`) and Unidirectional Data Flow (MVI) using `ScreenViewModel`.

---

## 1. Clean Architecture (`UseCase` & `FlowUseCase`)

FoundationKit standardizes all business operations through two core interfaces:

### `UseCase<I : UseCaseInput, O : UseCaseOutput>`
* For one-shot suspendable operations.
* Must return `Result<O>`.
* Use `NoneInput` if no parameters are required.
* Use `NoneOutput` if no result value is returned.

### `FlowUseCase<I : UseCaseInput, O : UseCaseOutput>`
* For continuous reactive data streams.
* Must return a cold `Flow<O>`.

### Concurrency Standard
* Use Cases must execute on appropriate dispatchers.
* Use `CoroutineDispatchers` (`Dispatchers.IO` for disk/network, `Dispatchers.Default` for CPU-intensive computing).
* Never hardcode `Dispatchers.IO` directly; inject or pass the dispatcher abstraction to enable deterministic testing.

---

## 2. MVI Architecture (`ScreenViewModel`)

FoundationKit implements MVI via `ScreenViewModel<State : UiState, Event : UiEvent, Effect : UiEffect>`:

### Contracts (`Contract.kt`)
* **`UiState`**: Immutable `data class`. Must have default values for all properties. Compatible with Jetpack Compose Strong Skipping Mode.
* **`UiEvent`**: Sealed interface or data objects/classes representing user interactions.
* **`UiEffect`**: Sealed interface or data objects/classes representing one-off side effects (navigation, snackbars, toasts).

### Unidirectional Data Flow Loop

```
  ┌──────────┐      sendEvent(Event)      ┌──────────────────┐
  │          ├───────────────────────────►│                  │
  │    UI    │                            │ ScreenViewModel  │
  │ (Compose)│◄───────────────────────────┤                  │
  └──────────┘   state (StateFlow)        └──────────────────┘
        ▲        effects (Channel Flow)
        │
        └────────────────────────────────────────┘
```

For full templates and unit test examples, see [MVI Templates Reference](./references/mvi-templates.md).
