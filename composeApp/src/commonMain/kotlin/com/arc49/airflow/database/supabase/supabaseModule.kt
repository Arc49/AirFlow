package com.arc49.airflow.database.supabase

import com.arc49.airflow.components.Constants.Companion.supabaseKey
import com.arc49.airflow.database.supabase.auth.AuthApi
import com.arc49.airflow.database.supabase.auth.AuthImpl
import com.arc49.airflow.database.supabase.auth.AuthViewModel
import io.github.jan.supabase.auth.Auth
import io.github.jan.supabase.auth.FlowType
import io.github.jan.supabase.createSupabaseClient
import io.github.jan.supabase.postgrest.Postgrest
import io.github.jan.supabase.realtime.Realtime
import io.github.jan.supabase.storage.Storage
import org.koin.dsl.module

val supabaseModule = module {
    single {
        supabseClientUser
    }
    single<AuthApi> { AuthImpl(get()) }
    factory { AuthViewModel(get(), get()) }
}

val supabseClientUser = createSupabaseClient(
    supabaseUrl = "https://vkmmwevraevsewenzqak.supabase.co",
    supabaseKey = supabaseKey
) {
    install(Postgrest)
    install(Realtime)
    install(Auth) {
        flowType = FlowType.PKCE
    }
    install(Storage)
}