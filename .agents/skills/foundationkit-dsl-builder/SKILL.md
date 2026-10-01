---
name: foundationkit-dsl-builder
description: >-
  Builds, refactors, or reviews Managers and DSL Builders in FoundationKit and dependent SDKs.
  Use this skill whenever creating a new Manager, Config, ConfigBuilder, or when implementing
  DSL initialization patterns following Flavor A (Context-Aware) or Flavor B (Pure Logic).
---

# FoundationKit DSL Builder

This skill provides the official procedure and standards for implementing and maintaining the **DSL-first initialization pattern** across FoundationKit and all dependent kits (AuthKit, MetricKit, etc.).

## Core Principles

1. **Uniform Entry Point**: Every manager must be instantiable via `MyManager.build { ... }` or `MyManager.build(context) { ... }`.
2. **Strict Immutability**: The resulting `ManagerConfig` must be completely immutable (`val` properties only).
3. **Memory Safety**: Context-aware managers must always convert incoming `Context` to `context.toSafeContext()` (Application Context) to prevent memory leaks.
4. **Comprehensive KDoc**: All builders, configs, and factories must have standard KDoc documentation.

---

## Step-by-Step Implementation Workflow

### Step 1: Determine the Manager Flavor

Analyze the technical requirements of the manager:
- **Flavor A (Context-Aware)**: Requires access to Android system services, resources, `SharedPreferences`, storage, or application lifecycle.
- **Flavor B (Pure Logic)**: Operates strictly with pure Kotlin/Java logic (crypto, formatting, domain algorithms, in-memory processing).

See [DSL Patterns Reference](./references/dsl-patterns.md) for full code examples.

### Step 2: Define the Immutable Configuration (`ManagerConfig`)

* Inherit from `es.joshluq.foundationkit.manager.ManagerConfig`.
* All fields must be `val`.
* Provide sensible default values where possible or keep constructor parameters mandatory if essential for manager operation.

```kotlin
class FeatureConfig(
    val endpoint: String,
    val timeoutMillis: Long,
    val context: Context // Omit if Flavor B
) : ManagerConfig
```

### Step 3: Implement the DSL Builder

* **For Flavor A**: Implement `ContextConfigBuilder<FeatureConfig>`.
  ```kotlin
  class FeatureConfigBuilder(override val context: Context) : ContextConfigBuilder<FeatureConfig> {
      var endpoint: String = ""
      var timeoutMillis: Long = 10_000L

      override fun build(): FeatureConfig {
          require(endpoint.isNotBlank()) { "endpoint cannot be empty" }
          return FeatureConfig(endpoint, timeoutMillis, context)
      }
  }
  ```
* **For Flavor B**: Implement `ConfigBuilder<FeatureConfig>`.
  ```kotlin
  class PureConfigBuilder : ConfigBuilder<PureConfig> {
      var precision: Int = 2

      override fun build(): PureConfig {
          require(precision >= 0) { "precision must be non-negative" }
          return PureConfig(precision)
      }
  }
  ```

### Step 4: Implement Manager & Factory Companion Object

* Extend `Manager<FeatureConfig>()`.
* Declare `companion object` implementing:
  * `ContextManagerFactory<FeatureManager, FeatureConfig, FeatureConfigBuilder>` for Flavor A.
  * `ManagerFactory<FeatureManager, FeatureConfig, PureConfigBuilder>` for Flavor B.
* Provide an internal builder implementing `ManagerBuilder<FeatureConfig, FeatureManager>`.

```kotlin
class FeatureManager private constructor() : Manager<FeatureConfig>() {

    companion object : ContextManagerFactory<FeatureManager, FeatureConfig, FeatureConfigBuilder> {
        override val builder = FeatureManagerBuilder()
        override fun createBuilder(context: Context): FeatureConfigBuilder = FeatureConfigBuilder(context)
    }

    private class FeatureManagerBuilder : ManagerBuilder<FeatureConfig, FeatureManager> {
        override fun build(config: FeatureConfig): FeatureManager {
            val manager = FeatureManager()
            manager.config = config
            return manager
        }
    }
}
```

### Step 5: Verification & Unit Tests

Always generate unit tests confirming that:
1. Default properties are properly populated.
2. DSL configuration block correctly overrides default values.
3. Validation in `build()` throws an `IllegalArgumentException` or `IllegalStateException` on invalid inputs.
4. The factory returns a fully initialized instance where `isConfigInitialized()` is true.
