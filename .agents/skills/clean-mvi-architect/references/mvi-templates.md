# Clean & MVI Architecture Templates

## 1. Clean Architecture Template

```kotlin
package es.joshluq.samplekit.domain

import es.joshluq.foundationkit.usecase.UseCase
import es.joshluq.foundationkit.usecase.UseCaseInput
import es.joshluq.foundationkit.usecase.UseCaseOutput
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext

data class FetchUserProfileInput(val userId: String) : UseCaseInput

data class FetchUserProfileOutput(val name: String, val email: String) : UseCaseOutput

class FetchUserProfileUseCase(
    private val repository: UserRepository,
    private val dispatcher: CoroutineDispatcher
) : UseCase<FetchUserProfileInput, FetchUserProfileOutput> {

    override suspend fun invoke(input: FetchUserProfileInput): Result<FetchUserProfileOutput> =
        withContext(dispatcher) {
            runCatching {
                val user = repository.getUser(input.userId)
                FetchUserProfileOutput(name = user.name, email = user.email)
            }
        }
}
```

---

## 2. MVI ScreenViewModel Template

```kotlin
package es.joshluq.samplekit.presentation

import androidx.lifecycle.viewModelScope
import es.joshluq.foundationkit.viewmodel.ScreenViewModel
import es.joshluq.foundationkit.viewmodel.UiEffect
import es.joshluq.foundationkit.viewmodel.UiEvent
import es.joshluq.foundationkit.viewmodel.UiState
import kotlinx.coroutines.launch

// State
data class ProfileState(
    val isLoading: Boolean = false,
    val userName: String = "",
    val error: String? = null
) : UiState

// Events
sealed interface ProfileEvent : UiEvent {
    data object RefreshClicked : ProfileEvent
    data class SubmitName(val newName: String) : ProfileEvent
}

// Effects
sealed interface ProfileEffect : UiEffect {
    data class ShowToast(val message: String) : ProfileEffect
    data object NavigateToHome : ProfileEffect
}

// ViewModel
class ProfileViewModel(
    private val fetchProfileUseCase: FetchUserProfileUseCase
) : ScreenViewModel<ProfileState, ProfileEvent, ProfileEffect>() {

    override fun createInitialState(): ProfileState = ProfileState()

    override fun handleEvent(event: ProfileEvent) {
        when (event) {
            is ProfileEvent.RefreshClicked -> loadProfile()
            is ProfileEvent.SubmitName -> updateProfileName(event.newName)
        }
    }

    private fun loadProfile() {
        viewModelScope.launch {
            updateState { copy(isLoading = true, error = null) }
            fetchProfileUseCase(FetchUserProfileInput("current-user"))
                .onSuccess { output ->
                    updateState { copy(isLoading = false, userName = output.name) }
                }
                .onFailure { throwable ->
                    updateState { copy(isLoading = false, error = throwable.message) }
                    launchEffect(ProfileEffect.ShowToast("Failed to load profile"))
                }
        }
    }

    private fun updateProfileName(name: String) {
        updateState { copy(userName = name) }
        launchEffect(ProfileEffect.NavigateToHome)
    }
}
```
