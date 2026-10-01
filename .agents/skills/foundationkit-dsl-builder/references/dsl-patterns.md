# FoundationKit DSL Patterns Reference

This reference provides complete, copy-paste ready implementations of the two official initialization flavors.

---

## Flavor A: Context-Aware Manager (Android SDK)

Use this pattern when your manager interacts with Android components (`Context`, `SharedPreferences`, `Resources`, etc.).

```kotlin
package es.joshluq.samplekit

import android.content.Context
import es.joshluq.foundationkit.manager.ContextConfigBuilder
import es.joshluq.foundationkit.manager.ContextManagerFactory
import es.joshluq.foundationkit.manager.Manager
import es.joshluq.foundationkit.manager.ManagerBuilder
import es.joshluq.foundationkit.manager.ManagerConfig

/**
 * Immutable configuration for [SampleContextManager].
 *
 * @property apiKey Unique service API key.
 * @property debugMode Enables verbose diagnostics.
 * @property context Application context.
 */
class SampleContextConfig(
    val apiKey: String,
    val debugMode: Boolean,
    val context: Context
) : ManagerConfig

/**
 * DSL Builder for [SampleContextConfig].
 *
 * @param context The Android context (converted to application context automatically).
 */
class SampleContextConfigBuilder(override val context: Context) : ContextConfigBuilder<SampleContextConfig> {
    var apiKey: String = ""
    var debugMode: Boolean = false

    override fun build(): SampleContextConfig {
        require(apiKey.isNotBlank()) { "apiKey must not be blank" }
        return SampleContextConfig(
            apiKey = apiKey,
            debugMode = debugMode,
            context = context
        )
    }
}

/**
 * Manager coordinating sample context-aware operations.
 */
class SampleContextManager private constructor() : Manager<SampleContextConfig>() {

    companion object : ContextManagerFactory<SampleContextManager, SampleContextConfig, SampleContextConfigBuilder> {
        override val builder: ManagerBuilder<SampleContextConfig, SampleContextManager> = SampleContextManagerBuilder()
        override fun createBuilder(context: Context): SampleContextConfigBuilder = SampleContextConfigBuilder(context)
    }

    private class SampleContextManagerBuilder : ManagerBuilder<SampleContextConfig, SampleContextManager> {
        override fun build(config: SampleContextConfig): SampleContextManager {
            val manager = SampleContextManager()
            manager.config = config
            return manager
        }
    }

    fun execute() {
        check(isConfigInitialized()) { "Manager is not initialized" }
        // Business logic using config
    }
}
```

### Usage
```kotlin
val manager = SampleContextManager.build(context) {
    apiKey = "API-KEY-999"
    debugMode = true
}
manager.execute()
```

---

## Flavor B: Pure Logic Manager (JVM / Pure Kotlin)

Use this pattern for managers with pure domain logic, cryptography, math, parsers, or formatting.

```kotlin
package es.joshluq.samplekit

import es.joshluq.foundationkit.manager.ConfigBuilder
import es.joshluq.foundationkit.manager.Manager
import es.joshluq.foundationkit.manager.ManagerBuilder
import es.joshluq.foundationkit.manager.ManagerConfig
import es.joshluq.foundationkit.manager.ManagerFactory

/**
 * Immutable configuration for [SamplePureManager].
 *
 * @property precision Number of decimal places.
 * @property strictParsing Enables strict validation rules.
 */
class SamplePureConfig(
    val precision: Int,
    val strictParsing: Boolean
) : ManagerConfig

/**
 * DSL Builder for [SamplePureConfig].
 */
class SamplePureConfigBuilder : ConfigBuilder<SamplePureConfig> {
    var precision: Int = 2
    var strictParsing: Boolean = false

    override fun build(): SamplePureConfig {
        require(precision in 0..10) { "precision must be between 0 and 10" }
        return SamplePureConfig(
            precision = precision,
            strictParsing = strictParsing
        )
    }
}

/**
 * Manager coordinating pure logic calculations.
 */
class SamplePureManager private constructor() : Manager<SamplePureConfig>() {

    companion object : ManagerFactory<SamplePureManager, SamplePureConfig, SamplePureConfigBuilder> {
        override val builder: ManagerBuilder<SamplePureConfig, SamplePureManager> = SamplePureManagerBuilder()
        override fun createBuilder(): SamplePureConfigBuilder = SamplePureConfigBuilder()
    }

    private class SamplePureManagerBuilder : ManagerBuilder<SamplePureConfig, SamplePureManager> {
        override fun build(config: SamplePureConfig): SamplePureManager {
            val manager = SamplePureManager()
            manager.config = config
            return manager
        }
    }

    fun calculate(value: Double): Double {
        check(isConfigInitialized()) { "Manager is not initialized" }
        // Calculation logic
        return value
    }
}
```

### Usage
```kotlin
val manager = SamplePureManager.build {
    precision = 4
    strictParsing = true
}
val result = manager.calculate(12.345678)
```
