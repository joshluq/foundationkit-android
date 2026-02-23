# FoundationKit

[![Kotlin Version](https://img.shields.io/badge/kotlin-1.9.0-blue.svg)](http://kotlinlang.org/)
[![Platform](https://img.shields.io/badge/platform-android-green.svg)](https://developer.android.com/android)

**The fundamental pillar of the suite.** 

FoundationKit provides the base abstractions, common interfaces, and essential utilities that all modules in the compendium depend on, ensuring consistency and efficiency across the entire ecosystem. It is the "glue" and the "foundation" for kits like AuthKit, MetricKit, and others.

---

## 🚀 Features

- 🛠 **Core Abstractions**: Common interfaces for Clean Architecture (`UseCase`, `FlowUseCase`).
- 🏗 **MVI Foundation**: Base classes for State-Event-Effect architecture (`ScreenViewModel`).
- 🪵 **Loggerkit**: A flexible, decorator-based logging system.
- 🧵 **Concurrency & Coroutines**: Injectable `DispatcherProvider`, `launchSafe`, and lifecycle-aware `ScopeOwner`.
- 💬 **TextProvider**: Decoupled text management for ViewModels (Strings, Resources, Compose).
- 📊 **ListState**: Exhaustive state management for data collections (Idle, Loading, Success, Empty, Error).
- 🗺 **Mapping Utilities**: Generic `Mappable` and `Model` interfaces for data transformation.
- 📱 **Showcase App**: Integrated demonstration of all components.

---

## 📦 Installation

Add the dependency to your `build.gradle.kts`:

```kotlin
dependencies {
    implementation("es.joshluq.kit:foundationkit:1.1.0")
}
```

---

## 🛠 Components

### 1. Loggerkit
A robust logging utility that follows Dependency Inversion.

```kotlin
val logger = Loggerkit.Builder().build()
logger.i("Tag", "Hello FoundationKit!") 
```

### 2. Concurrency & Coroutines
Infrastructure for testable and safe asynchronous tasks.

```kotlin
// Injectable Dispatchers
class MyRepository @Inject constructor(private val dispatchers: DispatcherProvider) {
    suspend fun doWork() = withContext(dispatchers.io) { ... }
}

// Safe launching with automatic logging
viewModelScope.launchSafe(logger = logger, onError = { /* Handle error */ }) {
    // Suspend work
}

// Safe execution with Result
val result = safeRun(logger) { api.call() }
```

### 3. TextProvider
Handle strings in ViewModels without `Context` or `R.string`.

```kotlin
val text = TextProvider.Resource(R.string.welcome_message, "User")
// Resolve in UI
val string = text.asString(context) 
// Resolve in Compose
val string = text.asString() 
```

### 4. ListState
Standardized state for collections.

```kotlin
val state: ListState<User> = users.toListState()
when (state) {
    is ListState.Loading -> ShowLoader()
    is ListState.Success -> ShowList(state.data)
    is ListState.Error -> ShowError(state.message.asString())
    is ListState.Empty -> ShowEmpty()
}
```

### 5. ScreenViewModel (MVI)
Base class to implement Unidirectional Data Flow (UDF).

```kotlin
class MyViewModel : ScreenViewModel<MyState, MyEvent, MyEffect>() {
    override fun createInitialState() = MyState()
    override fun handleEvent(event: MyEvent) { ... }
}
```

---

## 📱 Showcase
The project includes a `:showcase` module where you can see all these components working together in a real Android app with Jetpack Compose.

---

## 📄 License
This project is licensed under the MIT License.
