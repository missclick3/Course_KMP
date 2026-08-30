package ru.missclick.chat.presentation.components.manageChat

import androidx.compose.foundation.text.input.TextFieldState
import ru.missclick.core.designsystem.components.avatar.ChatParticipantUi
import ru.missclick.core.presentation.util.UiText

data class ManageChatState(
    val queryTextState: TextFieldState = TextFieldState(),
    val existingChatParticipants: List<ChatParticipantUi> = emptyList(),
    val selectedChatParticipants: List<ChatParticipantUi> = emptyList(),
    val isSearching: Boolean = false,
    val isSubmitting: Boolean = false,
    val canAddParticipant: Boolean = false,
    val currentSearchResult: ChatParticipantUi? = null,
    val searchError: UiText? = null,
    val submitError: UiText? = null
)