package ru.missclick.core.data.di

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import io.ktor.client.engine.HttpClientEngine
import io.ktor.client.engine.okhttp.OkHttp
import org.koin.android.ext.koin.androidContext
import org.koin.core.module.Module
import org.koin.dsl.module
import ru.missclick.core.data.auth.DataStoreSessionStorage
import ru.missclick.core.data.auth.createDataStore

actual val platformCoreDataModule: Module = module {
    single<HttpClientEngine> { OkHttp.create() }
    single<DataStore<Preferences>> {
        createDataStore(androidContext())
    }
}