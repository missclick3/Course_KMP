package ru.missclick.chat.data.di

import androidx.sqlite.driver.bundled.BundledSQLiteDriver
import org.koin.core.module.Module
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.bind
import org.koin.dsl.module
import ru.missclick.chat.data.chat.KtorChatParticipantService
import ru.missclick.chat.data.chat.KtorChatService
import ru.missclick.chat.data.chat.OfflineFirstChatRepository
import ru.missclick.chat.database.DatabaseFactory
import ru.missclick.chat.domain.chat.ChatParticipantService
import ru.missclick.chat.domain.chat.ChatRepository
import ru.missclick.chat.domain.chat.ChatService

expect val platformChatDataModule: Module

val chatDataModule = module {
    includes(platformChatDataModule)

    singleOf(::KtorChatParticipantService) bind ChatParticipantService::class
    singleOf(::KtorChatService) bind ChatService::class
    singleOf(::OfflineFirstChatRepository) bind ChatRepository::class
    single {
        get<DatabaseFactory>()
            .create()
            .setDriver(BundledSQLiteDriver())
            .build()
    }
}