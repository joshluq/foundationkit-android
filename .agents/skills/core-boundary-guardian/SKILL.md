---
name: core-boundary-guardian
description: >-
  Audits, verifies, or reviews module boundaries, API designs, and dependency purity between :core and :library.
  Use this skill whenever proposing new public interfaces, moving classes between modules,
  adding third-party dependencies, or checking for leaky abstractions and KDoc completeness.
---

# Core Boundary Guardian

This skill enforces architectural boundaries and purity rules within **FoundationKit** to ensure it remains lightweight, robust, and extensible as the bedrock of the entire mobile suite.

## The Architectural Line: `:core` vs `:library`

```
┌─────────────────────────────────────────────────────────────┐
│                       :showcase                             │
│       Sample Android application (Consumer-Driven test)     │
└──────────────────────────────┬──────────────────────────────┘
                               │
┌──────────────────────────────▼──────────────────────────────┐
│                       :library                              │
│       Android-specific bindings & platform implementations  │
│  (Context, SharedPreferences, Android Resources, ViewModel) │
└──────────────────────────────┬──────────────────────────────┘
                               │
┌──────────────────────────────▼──────────────────────────────┐
│                        :core                                │
│       Zero Android dependencies (Pure Kotlin / JVM)         │
│  (Abstractions, Interfaces, Pure Logic, Coroutines, Flow)   │
└─────────────────────────────────────────────────────────────┘
```

---

## The Four Golden Rules

### 1. Pure Kotlin in `:core`
* **Rule**: No imports starting with `android.*` or `androidx.*` are allowed in `:core`.
* **Rationale**: Any kit that requires pure JVM execution (or Kotlin Multiplatform in the future) must be able to depend on `:core` without bringing Android runtime baggage.

### 2. Zero Leaky Abstractions
* **Rule**: Never expose third-party library types in the signature of public interfaces or classes.
* **Bad**:
  ```kotlin
  interface HttpNetworkProvider {
      fun execute(request: okhttp3.Request): okhttp3.Response // LEAK!
  }
  ```
* **Good**:
  ```kotlin
  interface NetworkProvider {
      suspend fun request(endpoint: String, headers: Map<String, String>): NetworkResponse
  }
  ```

### 3. Interface Over Open Class
* **Rule**: Favor `interface` over `open class`.
* **Rationale**: Multiple inheritance of interfaces avoids brittle base class hierarchies and enables clean mocking/decorating (e.g. `Logger` and `LogProvider`).

### 4. Comprehensive KDoc
* **Rule**: 100% of public declarations must be documented.
* Document `@param`, `@return`, and `@throws` for every public method.
* Document expected thread safety and coroutine behavior.

---

## Audit Checklist Workflow

When reviewing or adding code:
1. Consult the [Boundary Decision Checklist](./references/boundary-checklist.md).
2. Run automated static checks:
   * Verify imports in `:core` do not contain `android.*`.
3. Check for open classes that should be interfaces.
4. Verify KDoc on all modified or new public API elements.
