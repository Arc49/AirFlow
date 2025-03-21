package com.arc49.airflow.presentations.tabs

import airflow.composeapp.generated.resources.Res
import airflow.composeapp.generated.resources.compose_multiplatform
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp
import com.arc49.airflow.Greeting
import io.github.alexzhirkevich.cupertino.adaptive.AdaptiveButton
import io.github.alexzhirkevich.cupertino.adaptive.AdaptiveCircularProgressIndicator
import io.github.alexzhirkevich.cupertino.adaptive.ExperimentalAdaptiveApi
import kotlinx.coroutines.delay
import org.jetbrains.compose.resources.painterResource
import kotlin.time.Duration.Companion.milliseconds

@OptIn(ExperimentalAdaptiveApi::class)
@Composable
internal fun HowToTab(modifier: Modifier) {
    var showContent by remember { mutableStateOf(false) }
    val greeting = remember { Greeting().greet() }
    var loading by remember { mutableStateOf(true) }

    LaunchedEffect(Unit) {
        delay(800.milliseconds)
        loading = false
    }

    Column (
        modifier = modifier.fillMaxWidth().padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        if (loading) {
            Column(modifier = modifier.fillMaxSize().padding(16.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center) {
                Column(horizontalAlignment = Alignment.CenterHorizontally, verticalArrangement = Arrangement.spacedBy(18.dp)) {
                    AdaptiveCircularProgressIndicator()
                    Text("Loading...")
                }
            }
        } else {
            var inputText by remember { mutableStateOf("") }
            Box(modifier = Modifier.imePadding()) {
                BasicTextField(
                    textStyle = TextStyle(color = Color.White),
                    value = inputText,
                    onValueChange = { inputText = it },
                    singleLine = true,
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(Color.DarkGray, shape = RoundedCornerShape(4.dp))
                        .padding(8.dp),
                    decorationBox = { innerTextField ->
                        if (inputText.isEmpty()) {
                            Text(
                                text = "Type your request here...",
                                color = Color.White
                            )
                        }
                        innerTextField()
                    }
                )
            }
            AdaptiveButton(onClick = { showContent = !showContent }) {
                Text("Click me!")
            }
            AnimatedVisibility(showContent) {
                Column(
                    Modifier.fillMaxWidth(),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Image(painterResource(Res.drawable.compose_multiplatform), null)
                    Text("Compose: $greeting")
                }
            }
        }
    }
}