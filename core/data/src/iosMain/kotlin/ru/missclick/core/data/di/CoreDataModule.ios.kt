package ru.missclick.core.data.di

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import io.ktor.client.engine.HttpClientEngine
import io.ktor.client.engine.darwin.Darwin
import org.koin.core.module.Module
import org.koin.dsl.module
import ru.missclick.core.data.auth.createDataStore

actual val platformCoreDataModule: Module = module {
    single<HttpClientEngine> { Darwin.create() }
    single<DataStore<Preferences>> {
        createDataStore()
    }
}