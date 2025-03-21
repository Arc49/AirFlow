package com.arc49.airflow.database.supabase.auth

import com.arc49.airflow.domain.supabase.UserLogin
import io.github.jan.supabase.auth.user.UserInfo
import kotlinx.coroutines.flow.Flow

interface AuthApi {
    suspend fun signInWithGoogle(googleIdToken: String)

    suspend fun retreiveOrUpsertUser(user: UserLogin): UserLogin

    suspend fun upsertUser(user: UserLogin): Flow<UserLogin>

    suspend fun retreiveUser(user: UserLogin): Flow<UserLogin>

    suspend fun retreiveCurrentSession(): UserInfo?
}