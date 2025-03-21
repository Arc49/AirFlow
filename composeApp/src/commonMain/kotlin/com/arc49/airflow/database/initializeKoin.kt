package com.arc49.airflow.database

import com.arc49.airflow.database.supabase.supabaseModule
import org.koin.core.context.startKoin

fun initKoin() {
    startKoin {
        modules(supabaseModule)
    }
}