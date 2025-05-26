package com.arc49.airflow.presentations.screens

import airflow.composeapp.generated.resources.Res
import airflow.composeapp.generated.resources.daily
import airflow.composeapp.generated.resources.idea
import airflow.composeapp.generated.resources.scan
import androidx.compose.animation.AnimatedContent
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Icon
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.withStyle
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.currentOrThrow
import com.arc49.airflow.presentations.tabs.HowToTab
import com.arc49.airflow.presentations.tabs.RoutineTab
import io.github.alexzhirkevich.cupertino.adaptive.AdaptiveAlertDialog
import io.github.alexzhirkevich.cupertino.adaptive.AdaptiveNavigationBar
import io.github.alexzhirkevich.cupertino.adaptive.AdaptiveNavigationBarItem
import io.github.alexzhirkevich.cupertino.adaptive.AdaptiveScaffold
import io.github.alexzhirkevich.cupertino.adaptive.AdaptiveTopAppBar
import io.github.alexzhirkevich.cupertino.adaptive.ExperimentalAdaptiveApi
import io.github.alexzhirkevich.cupertino.default
import org.jetbrains.compose.resources.painterResource

class Main: Screen {
    private var activeTab by mutableStateOf(Tabs.Routine)

    @OptIn(ExperimentalAdaptiveApi::class)
    @Composable
    override fun Content() {
        val navigator = LocalNavigator.currentOrThrow
        var showDialog by remember { mutableStateOf(false) }
        val snackbarHostState = remember { SnackbarHostState() }

        if (showDialog) {
            AdaptiveAlertDialog(
                title = { Text("Dialog Popup") },
                message = { Text("Contact info could be here!") },
                buttons = {
                    default(onClick = { showDialog = false }) { Text("OK") }
                },
                onDismissRequest = { showDialog = false }
            )
        }

        AdaptiveScaffold(
            snackbarHost = { SnackbarHost(snackbarHostState) },
            topBar = {
                AdaptiveTopAppBar(
                    title = {
                        Text(
                            buildAnnotatedString {
                                withStyle(style = SpanStyle(fontWeight = FontWeight.Bold)) {
                                    append("Air")
                                }
                                withStyle(style = SpanStyle(fontWeight = FontWeight.Medium)) {
                                    append("Flow")
                                }
                            }
                        )
                    }
                )
            },
            bottomBar = {
                bottomBar()
            }
        ) {
            AnimatedContent(targetState = activeTab) { selectedTab ->
                when (selectedTab) {
                    Tabs.Routine -> RoutineTab(Modifier.padding(it))
                    Tabs.Settings -> HowToTab(Modifier.padding(it))
                    Tabs.Scan -> {}
                }
            }
        }
    }

    @Composable
    @OptIn(ExperimentalAdaptiveApi::class)
    private fun bottomBar() {
        AdaptiveNavigationBar {
            AdaptiveNavigationBarItem(
                selected = activeTab == Tabs.Scan,
                icon = { Icon(painterResource(Res.drawable.scan), contentDescription = null) },
                label = { Text("scan") },
                onClick = { activeTab = Tabs.Scan }
            )
            AdaptiveNavigationBarItem(
                selected = activeTab == Tabs.Routine,
                icon = { Icon(painterResource(Res.drawable.daily), contentDescription = null) },
                label = { Text("routine") },
                onClick = { activeTab = Tabs.Routine }
            )
            AdaptiveNavigationBarItem(
                selected = activeTab == Tabs.Settings,
                icon = { Icon(painterResource(Res.drawable.idea), contentDescription = null) },
                label = { Text("how to") },
                onClick = { activeTab = Tabs.Settings }
            )
        }
    }

    enum class Tabs {
        Scan, Routine, Settings
    }
}