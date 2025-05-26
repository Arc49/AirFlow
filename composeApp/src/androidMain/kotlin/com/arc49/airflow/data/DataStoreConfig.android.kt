package com.arc49.airflow.data

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import org.koin.android.ext.koin.androidContext
import org.koin.core.module.Module
import org.koin.dsl.module

private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "settings")

actual val dataStoreModule: Module = module {
    single { androidContext().dataStore }
}

actual fun createDataStore(): DataStore<Preferences> = androidContext().dataStore 