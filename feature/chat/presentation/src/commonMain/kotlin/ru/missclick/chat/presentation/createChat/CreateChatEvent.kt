package ru.missclick.chat.presentation.createChat

import ru.missclick.chat.domain.models.Chat

sealed interface CreateChatEvent {
    data class OnChatCreated(val chat: Chat) : CreateChatEvent
}