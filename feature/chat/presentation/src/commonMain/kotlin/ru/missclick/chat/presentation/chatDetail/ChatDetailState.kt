package ru.missclick.chat.presentation.chatDetail

import androidx.compose.foundation.text.input.TextFieldState
import ru.missclick.chat.domain.models.ConnectionState
import ru.missclick.chat.presentation.model.ChatUi
import ru.missclick.chat.presentation.model.MessageUi
import ru.missclick.core.presentation.util.UiText

data class ChatDetailState(
    val chatUi: ChatUi? = null,
    val isLoading: Boolean = false,
    val messages: List<MessageUi> = emptyList(),
    val error: UiText? = null,
    val messageTextFieldState: TextFieldState = TextFieldState(),
    val canSendMessage: Boolean = false,
    val isPaginationLoading: Boolean = false,
    val paginationError: UiText? = null,
    val endReached: Boolean = false,
    val bannerState: BannerState = BannerState(),
    val isNearBottom: Boolean = false,
    val isChatOptionsOpen: Boolean = false,
    val connectionState: ConnectionState = ConnectionState.DISCONNECTED
)

data class BannerState(
    val formattedDate: UiText? = null,
    val isVisible: Boolean = false,
)