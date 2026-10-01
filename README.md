# FoundationKit

[![Platform](https://img.shields.io/badge/platform-android%20%7C%20jvm-green.svg)](https://developer.android.com/android)
[![Kotlin](https://img.shields.io/badge/kotlin-2.x-blue.svg)](http://kotlinlang.org/)
[![License: MIT](https://img.shields.io/badge/License-MIT-yellow.svg)](https://opensource.org/licenses/MIT)

**The foundational bedrock of the mobile ecosystem.**

FoundationKit provides base abstractions, common interfaces, and essential utilities that all feature modules (AuthKit, MetricKit, etc.) depend on, ensuring consistency, efficiency, and zero leaky abstractions across the suite.

---

## 📦 Modules & Installation

FoundationKit is split into modules following strict purity boundaries:

```kotlin
dependencies {
    // 1. Android Library: Context, SharedPreferences, ViewModel, NetworkMonitor, Resources
    implementation("es.joshluq.kit:foundationkit:2.0.0")

    // 2. Pure JVM / Kotlin Core (for pure Kotlin domain/data modules without Android SDK)
    implementation("es.joshluq.kit:foundationkit-core:2.0.0")

    // 3. Testing Fixtures (for unit test sourcesets across all kits and apps)
    testImplementation("es.joshluq.kit:foundationkit-testing:2.0.0")
}
```

---

## 🚀 Key Features & Components

### 1. 🌐 Network Connectivity Monitoring (`NetworkMonitor`)
Observe device internet connectivity reactively with automatic cleanup.

```kotlin
// Obtain monitor from any Android Context
val networkMonitor: NetworkMonitor = context.networkMonitor()

// 1. Simple boolean state
lifecycleScope.launch {
    networkMonitor.isOnline.collect { isOnline ->
        if (isOnline) syncPendingData() else showOfflineBanner()
    }
}

// 2. Granular status (Available, Losing, Lost, Unavailable)
lifecycleScope.launch {
    networkMonitor.status.collect { status ->
        when (status) {
            NetworkStatus.Available -> hideBanner()
            NetworkStatus.Losing -> showWeakConnectionWarning()
            NetworkStatus.Lost, NetworkStatus.Unavailable -> showOfflineBanner()
        }
    }
}
```

> **Note:** The `:foundationkit` library automatically includes the `<uses-permission android:name="android.permission.ACCESS_NETWORK_STATE" />` manifest permission.

---

### 2. 🧪 Testing Utilities (`foundationkit-testing`)
Avoid rewriting coroutine test boilerplate across every module.

```kotlin
class MyViewModelTest {

    // Automatically replaces Dispatchers.Main with StandardTestDispatcher and resets it
    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    // Test dispatcher provider for deterministic coroutine execution
    private val testDispatcherProvider = TestDispatcherProvider()

    @Test
    fun `loadData updates state correctly`() = runTest {
        val viewModel = MyViewModel(dispatchers = testDispatcherProvider)
        viewModel.loadData()
        
        // Assert state deterministically with virtual time
        assertEquals(expectedState, viewModel.state.value)
    }
}
```

---

### 3. ⏱️ Flow Operators (`throttleFirst`)
Prevent rapid double-clicks, burst events, and duplicate network requests.

```kotlin
import es.joshluq.foundationkit.coroutines.throttleFirst

// Suppress subsequent emissions within a 1-second window
buttonClicksFlow
    .throttleFirst(windowDurationMillis = 1_000L)
    .onEach { event -> viewModel.sendEvent(event) }
    .launchIn(lifecycleScope)
```

---

### 4. 🛡️ Typed Infrastructure Errors (`AppError`)
Structured, non-business platform errors ready to plug into Clean Architecture and UI states.

```kotlin
sealed interface AppError {
    Network, NoInternet, Timeout, Storage, Serialization,
    Cryptographic, NotFound, PermissionDenied, Authentication,
    Validation, Unexpected
}
```

#### Integration with `TextProvider` and `ListState`:
```kotlin
val error: AppError = AppError.NoInternet()

// Map directly to localized/dynamic TextProvider
val textProvider: TextProvider = error.toTextProvider()

// Map directly to UI ListState.Error
val listState: ListState.Error = error.toListStateError()
```

---

### 5. 🏗️ DSL Initialization Pattern
The official standard for creating configurable managers and SDK entry points.

#### Flavor A: Context-Aware Manager (Android SDK)
```kotlin
val manager = MyManager.build(context) {
    apiKey = "XYZ-123"
    debugMode = true
}
```

#### Flavor B: Pure Logic Manager (JVM / Pure Kotlin)
```kotlin
val manager = PureManager.build {
    precision = 4
}
```

---

### 6. 🏛️ Clean Architecture & MVI Foundation

* **Use Cases**: Standardized `UseCase<I, O>` (one-shot `suspend`) and `FlowUseCase<I, O>` (reactive stream) with `UseCaseInput` and `UseCaseOutput`.
* **ScreenViewModel**: Unidirectional Data Flow (UDF) managing `UiState` (immutable for Compose Strong Skipping), `UiEvent`, and one-off `UiEffect`.

```kotlin
class ProfileViewModel(
    private val fetchProfile: FetchProfileUseCase
) : ScreenViewModel<ProfileState, ProfileEvent, ProfileEffect>() {

    override fun createInitialState(): ProfileState = ProfileState()

    override fun handleEvent(event: ProfileEvent) {
        when (event) {
            is ProfileEvent.Refresh -> reload()
        }
    }
}
```

---

### 7. 💬 Decoupled Text (`TextProvider`)
Represent strings in ViewModels without leaking `android.content.Context`.

```kotlin
// In ViewModel / Domain:
val title = TextProvider.Resource(R.string.welcome_message, "User")
val dynamic = TextProvider.Dynamic("Server response text")

// In UI:
val text = title.asString(context)

// In Jetpack Compose:
Text(text = title.asString())
```

---

### 8. 📊 Exhaustive Data Collections (`ListState`)
Represent the full lifecycle of data collections in UI.

```kotlin
val state: ListState<Item> = itemsResult.toListState()

when (state) {
    is ListState.Idle -> Unit
    is ListState.Loading -> CircularProgressIndicator()
    is ListState.Success -> ItemList(state.data)
    is ListState.Empty -> EmptyPlaceholder()
    is ListState.Error -> ErrorMessage(state.message.asString())
}
```

---

### 9. 💾 Storage Providers
Abstracted key-value and object storage with serialization support:
* `CacheStorageProvider`: In-memory thread-safe cache (`ConcurrentHashMap`).
* `SharedPreferencesStorageProvider`: Android `SharedPreferences` with custom `SerializerProvider`.

---

## 📱 Showcase App

Check out the `:showcase` module in this repository for working examples and integration patterns with Jetpack Compose.

---

## 📄 License
Licensed under the [MIT License](LICENSE).
