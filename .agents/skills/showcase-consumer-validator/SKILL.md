---
name: showcase-consumer-validator
description: >-
  Validates FoundationKit API changes from the consumer perspective using the :showcase app module.
  Use this skill whenever verifying new features, checking developer ergonomics, or running
  compilation and unit test suites across modules.
---

# Showcase Consumer Validator

This skill enforces the **Consumer-Driven development pattern** required by [AGENTS.md](file:///c:/Users/josh_/AndroidStudioProjects/foundationkit-android/AGENTS.md). 

Every core abstraction or platform component created in FoundationKit must be validated by implementing a practical usage scenario in the `:showcase` sample module before being considered complete.

---

## Consumer-Driven Validation Workflow

### Step 1: Implement the Feature in `:showcase`
* Open or add a sample screen or integration in `:showcase/src/main/java`.
* Consume the new API directly as an external client would:
  ```kotlin
  // Example consumer validation
  val manager = MyManager.build(context) {
      apiKey = "showcase-test-key"
  }
  ```

### Step 2: Evaluate API Ergonomics
Ask the following questions during integration:
1. **Is the call site clean and intuitive?** Does it require unintuitive typecasts or boilerplate?
2. **Are default values sensible?** Can a developer get started with minimal configuration?
3. **Are failure messages helpful?** If a required property is missing, does the error point directly to the misconfigured property?
4. **Is IDE auto-completion fluid?** Are DSL lambdas and methods properly named?

### Step 3: Run Builds & Tests

Execute the test suites and build tasks:

```powershell
# Run unit tests across all modules
.\gradlew.bat test

# Or run tests for specific modules
.\gradlew.bat :foundationkit-core:test
.\gradlew.bat :foundationkit:testDebugUnitTest

# Verify showcase builds without compilation or packaging errors
.\gradlew.bat :showcase:assembleDebug
```

> [!NOTE]
> Ensure that `JAVA_HOME` points to JDK 21+ before executing Gradle tasks.
