package es.joshluq.foundationkit.showcase

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import es.joshluq.foundationkit.log.Loggerkit

import es.joshluq.foundationkit.showcase.ui.theme.ShowcaseTheme

class MainActivity : ComponentActivity() {

    // Example of Loggerkit initialization (could be via DI)
    private val logger: Loggerkit = Loggerkit.Builder().build()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        logger.i("MainActivity", "onCreate called")

        setContent {
            ShowcaseTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    LoggerDemo(
                        logger = logger,
                        modifier = Modifier.padding(innerPadding)
                    )
                }
            }
        }
    }
}

@Composable
fun LoggerDemo(logger: Loggerkit, modifier: Modifier = Modifier) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(16.dp, Alignment.CenterVertically)
    ) {
        Text(text = "Loggerkit Showcase", modifier = Modifier.padding(bottom = 16.dp))

        Button(
            onClick = { logger.d("Demo", "This is a DEBUG log 🔍") },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Send Debug Log")
        }

        Button(
            onClick = { logger.i("Demo", "This is an INFO log ℹ️") },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Send Info Log")
        }

        Button(
            onClick = { logger.w("Demo", "This is a WARNING log ⚠️") },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Send Warning Log")
        }

        Button(
            onClick = {
                logger.e("Demo", "This is an ERROR log 🚨", Throwable("Sample Error"))
            },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Send Error Log")
        }
    }
}
