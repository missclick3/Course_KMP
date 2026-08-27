package ru.missclick.chat.presentation.di

import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.module
import ru.missclick.chat.presentation.chatListDetail.ChatListDetailViewModel

val chatPresentationModule = module {
    viewModelOf(::ChatListDetailViewModel)
}