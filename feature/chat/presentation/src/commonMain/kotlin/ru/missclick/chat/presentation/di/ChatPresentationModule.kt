package ru.missclick.chat.presentation.di

import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.module
import ru.missclick.chat.presentation.chatDetail.ChatDetailViewModel
import ru.missclick.chat.presentation.chatList.ChatListViewModel
import ru.missclick.chat.presentation.chatListDetail.ChatListDetailViewModel
import ru.missclick.chat.presentation.createChat.CreateChatViewModel
import ru.missclick.chat.presentation.manageChat.ManageChatViewModel

val chatPresentationModule = module {
    viewModelOf(::ChatListDetailViewModel)
    viewModelOf(::CreateChatViewModel)
    viewModelOf(::ChatListViewModel)
    viewModelOf(::ChatDetailViewModel)
    viewModelOf(::ManageChatViewModel)
}