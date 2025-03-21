package com.arc49.airflow.database.supabase.auth

import com.arc49.airflow.domain.supabase.UserLogin
import io.github.jan.supabase.SupabaseClient
import io.github.jan.supabase.auth.auth
import io.github.jan.supabase.auth.providers.Google
import io.github.jan.supabase.auth.providers.builtin.IDToken
import io.github.jan.supabase.auth.user.UserInfo
import io.github.jan.supabase.postgrest.postgrest
import kotlinx.coroutines.flow.Flow

class AuthImpl(private val client: SupabaseClient) : AuthApi {
    private val auth = client.auth
    private val table = client.postgrest["airflow_users"]
    override suspend fun signInWithGoogle(googleIdToken: String) {
        auth.signInWith(IDToken) {
            idToken = googleIdToken
            provider = Google
            //nonce = rawNonce
        }
    }

    override suspend fun retreiveOrUpsertUser(user: UserLogin): UserLogin {
        val existingUser = table.select().decodeList<UserLogin>()

        return if (existingUser.isEmpty()) {
            println("Upserting User Login: $user")
            table.upsert(user)
            user
        } else {
            println("Existing Account: ${existingUser.first()}")
            existingUser.first()
        }
    }

    override suspend fun upsertUser(user: UserLogin): Flow<UserLogin> {
        TODO("Not yet implemented")
    }

    override suspend fun retreiveUser(user: UserLogin): Flow<UserLogin> {
        TODO("Not yet implemented")
    }

    override suspend fun retreiveCurrentSession(): UserInfo? {
        println("Retreiving Current Session")
        return auth.currentUserOrNull()
    }
}