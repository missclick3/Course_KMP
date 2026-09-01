package ru.missclick.chat.data.di

import androidx.sqlite.driver.bundled.BundledSQLiteDriver
import kotlinx.serialization.json.Json
import org.koin.core.module.Module
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.bind
import org.koin.dsl.module
import ru.missclick.chat.data.chat.KtorChatParticipantService
import ru.missclick.chat.data.chat.KtorChatService
import ru.missclick.chat.data.chat.OfflineFirstChatRepository
import ru.missclick.chat.data.chat.WebSocketChatConnectionClient
import ru.missclick.chat.data.message.OfflineFirstMessageRepository
import ru.missclick.chat.data.network.KtorWebSocketConnector
import ru.missclick.chat.database.DatabaseFactory
import ru.missclick.chat.domain.chat.ChatConnectionClient
import ru.missclick.chat.domain.chat.ChatParticipantService
import ru.missclick.chat.domain.chat.ChatRepository
import ru.missclick.chat.domain.chat.ChatService
import ru.missclick.chat.domain.chat.MessageRepository

expect val platformChatDataModule: Module

val chatDataModule = module {
    includes(platformChatDataModule)

    singleOf(::KtorChatParticipantService) bind ChatParticipantService::class
    singleOf(::KtorChatService) bind ChatService::class
    singleOf(::OfflineFirstChatRepository) bind ChatRepository::class
    singleOf(::OfflineFirstMessageRepository) bind MessageRepository::class
    singleOf(::WebSocketChatConnectionClient) bind ChatConnectionClient::class
    singleOf(::KtorWebSocketConnector)
    single {
        Json {
            ignoreUnknownKeys = true
        }
    }
    single {
        get<DatabaseFactory>()
            .create()
            .setDriver(BundledSQLiteDriver())
            .build()
    }
}