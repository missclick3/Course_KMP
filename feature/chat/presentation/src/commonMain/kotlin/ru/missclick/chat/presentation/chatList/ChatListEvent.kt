package ru.missclick.chat.presentation.chatList

import ru.missclick.core.presentation.util.UiText

sealed interface ChatListEvent {
    data object OnLogoutSuccess: ChatListEvent
    data class OnLogoutError(val error: UiText): ChatListEvent
}