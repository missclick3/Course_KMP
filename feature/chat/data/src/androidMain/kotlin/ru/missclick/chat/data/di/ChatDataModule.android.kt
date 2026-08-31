package ru.missclick.chat.data.di

import org.koin.android.ext.koin.androidContext
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.module
import ru.missclick.chat.data.lifecycle.AppLifecycleObserver
import ru.missclick.chat.data.network.ConnectivityObserver
import ru.missclick.chat.database.DatabaseFactory

actual val platformChatDataModule = module {
    single { DatabaseFactory(androidContext()) }
    singleOf(::AppLifecycleObserver)
    singleOf(::ConnectivityObserver)
}