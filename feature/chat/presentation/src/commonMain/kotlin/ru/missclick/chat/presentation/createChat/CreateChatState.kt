package ru.missclick.chat.presentation.createChat

import androidx.compose.foundation.text.input.TextFieldState
import ru.missclick.core.designsystem.components.avatar.ChatParticipantUi
import ru.missclick.core.presentation.util.UiText

data class CreateChatState(
    val queryTextState: TextFieldState = TextFieldState(),
    val selectedChatParticipants: List<ChatParticipantUi> = emptyList(),
    val isAddingParticipants: Boolean = false,
    val isLoadingParticipants: Boolean = false,
    val isCreatingChat: Boolean = false,
    val canAddParticipant: Boolean = false,
    val currentSearchResult: ChatParticipantUi? = null,
    val searchError: UiText? = null
)