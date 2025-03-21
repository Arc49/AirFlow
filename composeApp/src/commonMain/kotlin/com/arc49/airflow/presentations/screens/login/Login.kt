package com.arc49.airflow.presentations.screens.login

import androidx.compose.animation.AnimatedContent
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.currentOrThrow
import com.arc49.airflow.components.Constants.Companion.SERVER_ID
import com.arc49.airflow.domain.supabase.Subscription
import com.arc49.airflow.domain.supabase.UserLogin
import com.arc49.airflow.root.RootComponents
import com.mmk.kmpauth.google.GoogleAuthCredentials
import com.mmk.kmpauth.google.GoogleAuthProvider
import com.mmk.kmpauth.google.GoogleButtonUiContainer
import io.github.alexzhirkevich.cupertino.adaptive.AdaptiveCircularProgressIndicator
import io.github.alexzhirkevich.cupertino.adaptive.AdaptiveSurface
import io.github.alexzhirkevich.cupertino.adaptive.ExperimentalAdaptiveApi
import io.github.jan.supabase.auth.status.SessionStatus
import kotlinx.coroutines.flow.collectLatest

class Login: Screen {
    @OptIn(ExperimentalAdaptiveApi::class)
    @Composable
    override fun Content() {
        val navigator = LocalNavigator.currentOrThrow
        val root = remember { RootComponents() }
        val viewModel = root.viewModel
        val userInfo by viewModel.sessionStatus.collectAsState()
        val userData by viewModel.userInfo.collectAsState()
        var loadData by remember { mutableStateOf(false) }
        var authReady by remember { mutableStateOf(false) }
        LaunchedEffect(Unit) {
            GoogleAuthProvider.create(credentials = GoogleAuthCredentials(serverId = SERVER_ID))
            authReady = true
        }
        //Check Device Info
        //val deviceInfoXState = DeviceInfoXState()

        AdaptiveSurface {
            AnimatedContent(userInfo) { info ->
                when (info) {
                    is SessionStatus.Authenticated -> {
                        println("Login Success Loading")
                        userData?.let {
                            println("Login Success: ${it.email} ${it.id}")
                            LaunchedEffect(it.id) {
                                try {
                                    viewModel.retreiveOrUpsertUser(
                                        UserLogin(
                                            userId = it.id,
                                            email = it.email ?: "",
                                            subscription = Subscription(
                                                isSubscribed = false,
                                                subscriptionCode = "",
                                                startSubscribed = "",
                                                endSubscribed = ""
                                            )
                                        )
                                    )
                                } catch (e: Exception) {
                                    println("Error occurred: ${e.message}")
                                } finally {
                                    viewModel.userLoginAccount.collectLatest { account ->
                                        account?.let {
                                            println("Finish Login: ${account.email}")
                                            navigator.popUntilRoot()
                                        }
                                    }
                                }
                            }
                        } ?: Column(modifier = Modifier.fillMaxSize().padding(16.dp),
                            horizontalAlignment = Alignment.CenterHorizontally,
                            verticalArrangement = Arrangement.Center) {
                            Column(horizontalAlignment = Alignment.CenterHorizontally, verticalArrangement = Arrangement.spacedBy(18.dp)) {
                                AdaptiveCircularProgressIndicator()
                                Text("User Login Success")
                            }
                        }
                    }
                    SessionStatus.Initializing -> {
                        Column(modifier = Modifier.fillMaxSize().padding(16.dp),
                            horizontalAlignment = Alignment.CenterHorizontally,
                            verticalArrangement = Arrangement.Center) {
                            Column(horizontalAlignment = Alignment.CenterHorizontally, verticalArrangement = Arrangement.spacedBy(18.dp)) {
                                AdaptiveCircularProgressIndicator()
                                Text("Loading...")
                            }
                        }
                    }
                    is SessionStatus.NotAuthenticated -> {
                        if (loadData) {
                            Column(modifier = Modifier.fillMaxSize().padding(16.dp),
                                horizontalAlignment = Alignment.CenterHorizontally,
                                verticalArrangement = Arrangement.Center) {
                                Column(horizontalAlignment = Alignment.CenterHorizontally, verticalArrangement = Arrangement.spacedBy(18.dp)) {
                                    AdaptiveCircularProgressIndicator()
                                    //Text("Loading...")
                                }
                            }
                        } else {
                            Box(
                                modifier = Modifier.fillMaxSize(),
                                contentAlignment = Alignment.Center
                            ) {
                                if (authReady) {
                                    GoogleButtonUiContainer(onGoogleSignInResult = { googleUser ->
                                        val token = googleUser?.idToken
                                        token?.let {
                                            viewModel.loginWithGoogle(it)
                                            loadData = true
                                        }
                                    }) {
                                        com.mmk.kmpauth.uihelper.google.GoogleSignInButton(
                                            onClick = {this.onClick()}
                                        )
                                    }
                                }
                            }
                        }
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
                }
            }
        }
    }
}