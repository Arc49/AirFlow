package com.arc49.airflow.data

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import org.koin.core.module.Module
import org.koin.dsl.module

expect val dataStoreModule: Module

expect fun createDataStore(): DataStore<Preferences> 