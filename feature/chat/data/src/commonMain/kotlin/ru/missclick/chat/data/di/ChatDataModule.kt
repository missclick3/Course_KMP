package ru.missclick.chat.data.di

import org.koin.core.module.dsl.singleOf
import org.koin.dsl.bind
import org.koin.dsl.module
import ru.missclick.chat.data.chat.KtorChatParticipantService
import ru.missclick.chat.domain.chat.ChatParticipantService

val chatDataModule = module {
    singleOf(::KtorChatParticipantService) bind ChatParticipantService::class
}