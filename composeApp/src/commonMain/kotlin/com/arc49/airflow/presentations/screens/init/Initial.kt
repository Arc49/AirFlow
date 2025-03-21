package com.arc49.airflow.presentations.screens.init

import androidx.compose.animation.AnimatedContent
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import cafe.adriel.voyager.navigator.Navigator
import cafe.adriel.voyager.transitions.SlideTransition
import com.arc49.airflow.presentations.screens.Main
import com.arc49.airflow.presentations.screens.login.Login
import com.arc49.airflow.root.RootComponents
import io.github.alexzhirkevich.cupertino.adaptive.AdaptiveCircularProgressIndicator
import io.github.alexzhirkevich.cupertino.adaptive.AdaptiveSurface
import io.github.alexzhirkevich.cupertino.adaptive.ExperimentalAdaptiveApi
import io.github.jan.supabase.auth.status.SessionStatus

@OptIn(ExperimentalAdaptiveApi::class)
@Composable
fun Initial() {
    val root = remember { RootComponents() }
    val viewModel = root.viewModel
    val userInfo by viewModel.sessionStatus.collectAsState()

    AdaptiveSurface {
        AnimatedContent(userInfo) { info ->
            when(info) {
                is SessionStatus.Initializing -> {
                    Column(modifier = Modifier.fillMaxSize().padding(16.dp),
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.Center) {
                        Column(horizontalAlignment = Alignment.CenterHorizontally, verticalArrangement = Arrangement.spacedBy(18.dp)) {
                            AdaptiveCircularProgressIndicator()
                            Text("Loading...")
                        }
                    }
                }
                is SessionStatus.NotAuthenticated -> Navigator(Login()) {
                    SlideTransition(it)
                }
                is SessionStatus.RefreshFailure -> {
                    Column(modifier = Modifier.fillMaxSize().padding(16.dp),
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.Center) {
                        Column(horizontalAlignment = Alignment.CenterHorizontally, verticalArrangement = Arrangement.spacedBy(18.dp)) {
                            AdaptiveCircularProgressIndicator()
                            Text("Loading...")
                        }
                    }
                }
                is SessionStatus.Authenticated -> {
                    println("Authenticated")
                    Navigator(Main()) {
                        SlideTransition(it)
                    }
                }
            }
        }
    }
}