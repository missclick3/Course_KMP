package ru.missclick.chat.data.di

import org.koin.android.ext.koin.androidContext
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.bind
import org.koin.dsl.module
import ru.missclick.chat.data.lifecycle.AppLifecycleObserver
import ru.missclick.chat.data.network.ConnectionErrorHandler
import ru.missclick.chat.data.network.ConnectivityObserver
import ru.missclick.chat.data.notification.FirebasePushNotificationService
import ru.missclick.chat.database.DatabaseFactory
import ru.missclick.chat.domain.notification.PushNotificationService

actual val platformChatDataModule = module {
    single { DatabaseFactory(androidContext()) }
    singleOf(::AppLifecycleObserver)
    singleOf(::ConnectivityObserver)
    singleOf(::ConnectionErrorHandler)
    singleOf(::FirebasePushNotificationService) bind PushNotificationService::class
}