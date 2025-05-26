package com.arc49.airflow.domain.supabase

import io.github.jan.supabase.SupabaseClient
import io.github.jan.supabase.createSupabaseClient
import io.github.jan.supabase.gotrue.gotrue
import io.github.jan.supabase.postgrest.postgrest
import io.github.jan.supabase.realtime.realtime

object SupabaseClientProvider {

    private const val SUPABASE_URL = "https://vrimakcvevrxcuzlscuq.supabase.co"
    private const val SUPABASE_ANON_KEY = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJpc3MiOiJzdXBhYmFzZSIsInJlZiI6InZyaW1ha2N2ZXZyeGN1emxzY3VxIiwicm9sZSI6ImFub24iLCJpYXQiOjE3NDYzOTA5NzAsImV4cCI6MjA2MTk2Njk3MH0.PYqPVHWN4X0BBRwFC-rZ4LFWS9jNt-VGwbQKyZ_ypu8"

    val client: SupabaseClient by lazy {
        createSupabaseClient(
            supabaseUrl = SUPABASE_URL,
            supabaseKey = SUPABASE_ANON_KEY
        ) {
            install(io.github.jan.supabase.gotrue.GoTrue)
            install(io.github.jan.supabase.postgrest.Postgrest)
            install(io.github.jan.supabase.realtime.Realtime)
        }
    }
}