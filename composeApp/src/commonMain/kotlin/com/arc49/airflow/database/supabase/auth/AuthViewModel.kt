package com.arc49.airflow.database.supabase.auth

import cafe.adriel.voyager.core.model.ScreenModel
import cafe.adriel.voyager.core.model.screenModelScope
import co.touchlab.kermit.Logger
import com.arc49.airflow.domain.supabase.UserLogin
import io.github.jan.supabase.SupabaseClient
import io.github.jan.supabase.auth.auth
import io.github.jan.supabase.auth.user.UserInfo
import io.github.jan.supabase.realtime.RealtimeChannel
import io.github.jan.supabase.realtime.channel
import io.github.jan.supabase.realtime.realtime
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.IO
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch

class AuthViewModel(val supabaseClient: SupabaseClient, private val authApi: AuthApi): ScreenModel {
    val alert = MutableStateFlow<String?>(null)
    val userLoginAccount = MutableStateFlow<UserLogin?>(null)
    val _userInfo: MutableStateFlow<UserInfo?> = MutableStateFlow(null)
    val userInfo: MutableStateFlow<UserInfo?> = _userInfo
    val sessionStatus = supabaseClient.auth.sessionStatus
    private var userChannel: RealtimeChannel = supabaseClient.realtime.channel("public:airflow_users")
//    init {
//        screenModelScope.launch {
//            retreiveCurrentSession()
//            supabaseClient.auth.sessionStatus.collectLatest { status ->
//                when (status) {
//                    is SessionStatus.Authenticated -> {
//                        println("Load Authenticated")
//                        // User is authenticated
//                        _userInfo.value = status.session.user
//                        // retreiveAllUserLogin(status.session.user!!.id)
//                    }
//                    is SessionStatus.NotAuthenticated -> {
//                        // User is not authenticated
//                        println("Not Authenticated")
//                        _userInfo.value = null
//                    }
//                    is SessionStatus.RefreshFailure -> {
//                        // Handle refresh failure
//                        println("Session refresh failed: ${status.cause}")
//                    }
//                    SessionStatus.Initializing -> {
//                        // Handle initializing state if necessary
//                        println("Session is initializing")
//                    }
//                }
//            }
//        }
//    }

    fun loginWithGoogle(googleIdToken: String) {
        userChannel.apply {
            screenModelScope.launch(Dispatchers.IO) {
                kotlin.runCatching {
                    authApi.signInWithGoogle(googleIdToken)
                }
            }
        }
    }
    fun retreiveOrUpsertUser(user: UserLogin) {
        userChannel.apply {
            screenModelScope.launch(Dispatchers.IO) {
                kotlin.runCatching {
                    authApi.retreiveOrUpsertUser(user)
                }.onSuccess { userLogin ->
                    userLoginAccount.value = userLogin
                }.onFailure {
                    Logger.e(it) { "Error while retrieving message with id ${user.userId}" }
                    alert.value = "There was an error retrieving the message: ${it.message}"
                }
            }
        }
    }
    fun retreiveCurrentSession() {
        userChannel.apply {
            screenModelScope.launch(Dispatchers.IO) {
                kotlin.runCatching {
                    authApi.retreiveCurrentSession()
                }.onSuccess { userInfo ->
                    _userInfo.value = userInfo
                }
            }
        }
    }
}