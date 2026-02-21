package es.joshluq.foundationkit.viewmodel

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class ScreenViewModelTest {

    private val testDispatcher = UnconfinedTestDispatcher()

    @Before
    fun setup() {
        Dispatchers.setMain(testDispatcher)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    // Concrete implementation for testing
    private data class TestState(val count: Int = 0) : UiState
    private sealed interface TestEvent : UiEvent {
        data object Increment : TestEvent
        data class TriggerEffect(val message: String) : TestEvent
    }
    private sealed interface TestEffect : UiEffect {
        data class ShowMessage(val message: String) : TestEffect
    }

    private class TestViewModel : ScreenViewModel<TestState, TestEvent, TestEffect>() {
        override fun createInitialState(): TestState = TestState()

        override fun handleEvent(event: TestEvent) {
            when (event) {
                TestEvent.Increment -> updateState { copy(count = count + 1) }
                is TestEvent.TriggerEffect -> launchEffect(TestEffect.ShowMessage(event.message))
            }
        }
    }

    @Test
    fun `initial state should be correct`() {
        val viewModel = TestViewModel()
        assertEquals(TestState(), viewModel.state.value)
    }

    @Test
    fun `updateState should modify state correctly`() {
        val viewModel = TestViewModel()
        viewModel.sendEvent(TestEvent.Increment)
        assertEquals(1, viewModel.state.value.count)
    }

    @Test
    fun `launchEffect should emit effect`() = runTest {
        val viewModel = TestViewModel()
        val expectedMessage = "Hello Test"
        
        viewModel.sendEvent(TestEvent.TriggerEffect(expectedMessage))
        
        val effect = viewModel.effects.first()
        assertTrue(effect is TestEffect.ShowMessage)
        assertEquals(expectedMessage, (effect as TestEffect.ShowMessage).message)
    }
}

// Extension for convenience if assertTrue is not imported correctly by the env
private fun assertTrue(condition: Boolean) {
    assert(condition)
}
