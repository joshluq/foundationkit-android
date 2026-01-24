package es.joshluq.foundationkit.showcase

import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Button
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import es.joshluq.foundationkit.log.Loggerkit
import es.joshluq.foundationkit.showcase.ui.theme.ShowcaseTheme
import es.joshluq.foundationkit.usecase.NoneInput
import es.joshluq.foundationkit.viewmodel.ScreenViewModel
import es.joshluq.foundationkit.viewmodel.UiEffect
import es.joshluq.foundationkit.viewmodel.UiEvent
import es.joshluq.foundationkit.viewmodel.UiState
import kotlinx.coroutines.launch

// --- MVI Components for Showcase ---

data class ShowcaseState(val counter: Int = 0) : UiState

sealed interface ShowcaseEvent : UiEvent {
    data object OnIncrementClick : ShowcaseEvent
    data object OnShowToastClick : ShowcaseEvent
}

sealed interface ShowcaseEffect : UiEffect {
    data class ShowToast(val message: String) : ShowcaseEffect
}

class ShowcaseViewModel : ScreenViewModel<ShowcaseState, ShowcaseEvent, ShowcaseEffect>() {
    override fun createInitialState(): ShowcaseState = ShowcaseState()

    override fun handleEvent(event: ShowcaseEvent) {
        when (event) {
            ShowcaseEvent.OnIncrementClick -> {
                updateState { it.copy(counter = it.counter + 1) }
            }
            ShowcaseEvent.OnShowToastClick -> {
                launchEffect(ShowcaseEffect.ShowToast("Counter is: ${state.value.counter}"))
            }
        }
    }
}

// --- UI Components ---

class MainActivity : ComponentActivity() {

    private val logger: Loggerkit = Loggerkit.Builder().build()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        logger.i("MainActivity", "onCreate called")

        setContent {
            ShowcaseTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    val viewModel: ShowcaseViewModel = viewModel()
                    val state by viewModel.state.collectAsState()
                    val context = LocalContext.current

                    LaunchedEffect(Unit) {
                        viewModel.effects.collect { effect ->
                            when (effect) {
                                is ShowcaseEffect.ShowToast -> {
                                    Toast.makeText(context, effect.message, Toast.LENGTH_SHORT).show()
                                }
                            }
                        }
                    }

                    LazyColumn(
                        modifier = Modifier
                            .padding(innerPadding)
                            .fillMaxSize()
                            .padding(16.dp),
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.spacedBy(16.dp)
                    ) {
                        item { LoggerDemo(logger = logger) }
                        item { HorizontalDivider(modifier = Modifier.padding(vertical = 8.dp)) }
                        item { MviDemo(state = state, onEvent = viewModel::sendEvent) }
                        item { HorizontalDivider(modifier = Modifier.padding(vertical = 8.dp)) }
                        item { UseCaseDemo() }
                    }
                }
            }
        }
    }
}

@Composable
fun LoggerDemo(logger: Loggerkit) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        Text(text = "Loggerkit Demo", style = MaterialTheme.typography.titleMedium)
        Button(onClick = { logger.d("Demo", "Debug Log clicked") }) {
            Text("Send Debug Log")
        }
    }
}

@Composable
fun MviDemo(state: ShowcaseState, onEvent: (ShowcaseEvent) -> Unit) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        Text(text = "ScreenViewModel (MVI) Demo", style = MaterialTheme.typography.titleMedium)
        Text(text = "Counter: ${state.counter}")
        
        Button(onClick = { onEvent(ShowcaseEvent.OnIncrementClick) }) {
            Text("Increment Counter")
        }
        
        Button(onClick = { onEvent(ShowcaseEvent.OnShowToastClick) }) {
            Text("Show Effect (Toast)")
        }
    }
}

@Composable
fun UseCaseDemo() {
    val scope = rememberCoroutineScope()
    var useCaseResult by remember { androidx.compose.runtime.mutableStateOf("No result yet") }
    val flowResults = remember { mutableStateListOf<Int>() }

    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        Text(text = "UseCase Demo", style = MaterialTheme.typography.titleMedium)
        
        // Standard UseCase Example
        Button(
            onClick = {
                scope.launch {
                    val useCase = GetGreetingUseCase()
                    useCase(GetGreetingInput("Josh")).onSuccess { 
                        useCaseResult = it.message 
                    }.onFailure {
                        useCaseResult = "Error: ${it.message}"
                    }
                }
            },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Run GetGreetingUseCase")
        }
        Text(text = useCaseResult)

        Spacer(modifier = Modifier.height(8.dp))

        // FlowUseCase Example
        Button(
            onClick = {
                scope.launch {
                    flowResults.clear()
                    val flowUseCase = GetCounterFlowUseCase()
                    flowUseCase(NoneInput).collect { result ->
                        flowResults.add(result.value)
                    }
                }
            },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Run GetCounterFlowUseCase (1 to 5)")
        }
        
        Text(text = "Flow results: ${flowResults.joinToString(", ")}")
    }
}
