package ru.missclick.chat.data.di

import org.koin.core.module.dsl.singleOf
import org.koin.dsl.bind
import org.koin.dsl.module
import ru.missclick.chat.data.chat.KtorChatParticipantService
import ru.missclick.chat.data.chat.KtorChatService
import ru.missclick.chat.domain.chat.ChatParticipantService
import ru.missclick.chat.domain.chat.ChatService

val chatDataModule = module {
    singleOf(::KtorChatParticipantService) bind ChatParticipantService::class
    singleOf(::KtorChatService) bind ChatService::class
}