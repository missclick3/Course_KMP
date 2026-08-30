package ru.missclick.chat.data.di

import org.koin.core.module.dsl.singleOf
import org.koin.dsl.module
import ru.missclick.chat.data.lifecycle.AppLifecycleObserver
import ru.missclick.chat.database.DatabaseFactory

actual val platformChatDataModule = module {
    single { DatabaseFactory() }
    singleOf(::AppLifecycleObserver)
}