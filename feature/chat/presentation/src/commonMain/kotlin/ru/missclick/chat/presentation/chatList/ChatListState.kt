package ru.missclick.chat.presentation.chatList

import ru.missclick.chat.presentation.model.ChatUi
import ru.missclick.core.designsystem.components.avatar.ChatParticipantUi
import ru.missclick.core.presentation.util.UiText

data class ChatListState(
    val chats: List<ChatUi> = emptyList(),
    val error: UiText? = null,
    val localParticipantUi: ChatParticipantUi? = null,
    val isUserMenuOpen: Boolean = false,
    val showLogoutConfirmation: Boolean = false,
    val selectedChatId: String? = null,
    val isLoading: Boolean = false,
)