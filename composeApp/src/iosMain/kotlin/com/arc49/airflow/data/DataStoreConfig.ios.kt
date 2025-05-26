package com.arc49.airflow.data

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import org.koin.core.module.Module
import org.koin.dsl.module
import platform.Foundation.NSHomeDirectory
import platform.Foundation.NSURL
import platform.Foundation.NSUserDefaults

private val dataStore = preferencesDataStore(
    name = "settings",
    produceMigrations = { context ->
        listOf()
    }
)

actual val dataStoreModule: Module = module {
    single { dataStore }
}

actual fun createDataStore(): DataStore<Preferences> = dataStore 