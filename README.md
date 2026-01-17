# FoundationKit

[![Kotlin Version](https://img.shields.io/badge/kotlin-1.9.0-blue.svg)](http://kotlinlang.org/)
[![Platform](https://img.shields.io/badge/platform-android-green.svg)](https://developer.android.com/android)

**The fundamental pillar of the suite.** 

FoundationKit provides the base abstractions, common interfaces, and essential utilities that all modules in the compendium depend on, ensuring consistency and efficiency across the entire ecosystem. It is the "glue" and the "foundation" for kits like AuthKit, MetricKit, and others.

---

## 🚀 Features

- 🛠 **Core Abstractions**: Common interfaces for Clean Architecture (UseCases).
- 🏗 **MVI Foundation**: Base classes for State-Event-Effect architecture.
- 🪵 **Loggerkit**: A flexible, decorator-based logging system.
- 🧩 **Consistency**: Unified coding style and architectural patterns.
- 📱 **Showcase App**: Integrated demonstration of all components.

---

## 📦 Installation

Add the dependency to your `build.gradle.kts`:

```kotlin
dependencies {
    implementation("es.joshluq.kit:foundationkit:x.y.z")
}
```

---

## 🛠 Components

### 1. Loggerkit
A robust logging utility that follows Dependency Inversion.

```kotlin
// Initialization (Defaults: Loggerkit -> TAG, Emojis, Thread Info)
val logger = Loggerkit.Builder().build()

logger.i("Tag", "Hello FoundationKit!") 
// Output: Loggerkit -> Tag: ℹ️ [main] Hello FoundationKit!

// Custom Configuration
val customLogger = Loggerkit.Builder()
    .setProvider(LoggerDefaults.defaultLogProvider(
        minLogLevel = LogLevel.DEBUG,
        tagPrefix = "MyApp",
        showThread = false
    ))
    .build()
```

### 2. UseCase & FlowUseCase
Standardized interfaces for business logic execution.

```kotlin
// Standard UseCase (Suspend)
class MyUseCase : UseCase<MyInput, MyOutput> {
    override suspend fun invoke(input: MyInput): Result<MyOutput> { ... }
}

// Reactive UseCase (Flow)
class MyFlowUseCase : FlowUseCase<NoneInput, MyOutput> {
    override fun invoke(input: NoneInput): Flow<Result<MyOutput>> { ... }
}
```

### 3. ScreenViewModel (MVI)
Base class to implement Unidirectional Data Flow (UDF).

```kotlin
class MyViewModel : ScreenViewModel<MyState, MyEvent, MyEffect>() {
    override fun createInitialState() = MyState()

    override fun handleEvent(event: MyEvent) {
        when (event) {
            is MyEvent.Refresh -> updateState { it.copy(isLoading = true) }
        }
    }
}
```

---

## 📱 Showcase
The project includes a `:showcase` module where you can see all these components working together in a real Android app with Jetpack Compose.

---

## 🛠 Building & Testing

### Compile the library
```bash
./gradlew :library:assemble
```

### Run tests
```bash
./gradlew :library:test
```

---

## 📄 License
This project is licensed under the MIT License.
