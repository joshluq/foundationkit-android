# Module Boundary & Placement Decision Matrix

Use this checklist to decide where a new component belongs.

---

## 1. Where Does It Belong?

| Question | If YES | If NO |
| :--- | :--- | :--- |
| Does the class require `android.content.Context` or Android SDK classes? | Place in **`:library`** | Consider **`:core`** |
| Is it a pure interface, contract, data mapping model, or coroutine dispatcher utility? | Place in **`:core`** | Place in **`:library`** |
| Is it specific to a single domain/feature (e.g. Firebase Auth, Room DB, Retrofit HTTP, Biometrics)? | **Do NOT put in FoundationKit**. Put in dedicated Kit (e.g., `AuthKit`, `StorageKit`, `NetworkKit`). | Belongs in FoundationKit if it's a cross-cutting foundational abstraction. |
| Is it an implementation of an abstraction using Android components (e.g. `SharedPreferencesStorage`, `AndroidTextProvider`)? | Place in **`:library`** | - |

---

## 2. API Design Checklist

- [ ] **Package convention**: Package begins with `es.joshluq.foundationkit.*`
- [ ] **Visibility modifiers**: Internal implementation details are marked `internal` or `private`.
- [ ] **Clean contract**: All domain abstractions are defined as `interface`, not `open class`.
- [ ] **No leaky third-party dependencies**: Public functions return domain models or standard Kotlin types, never library-specific wrapper objects.
- [ ] **Concurrency safety**: Functions performing I/O or heavy work are marked `suspend` or return a cold `Flow`.
- [ ] **KDoc**: Class-level KDoc explaining purpose, plus parameter/return descriptions on public members.
