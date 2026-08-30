package ru.missclick.chat.presentation.manageChat

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.stateIn
import ru.missclick.chat.domain.chat.ChatParticipantService
import ru.missclick.chat.domain.chat.ChatRepository
import ru.missclick.chat.presentation.components.manageChat.ManageChatAction
import ru.missclick.chat.presentation.components.manageChat.ManageChatState

class ManageChatViewModel(
    private val chatParticipantService: ChatParticipantService,
    private val chatRepository: ChatRepository
) : ViewModel() {

    private var hasLoadedInitialData = false

    private val eventChannel = Channel<ManageChatEvent>()
    val events = eventChannel.receiveAsFlow()

    private val _state = MutableStateFlow(ManageChatState())

    val state = _state
        .onStart {
            if (!hasLoadedInitialData) {
                hasLoadedInitialData = true
            }
        }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5_000L),
            initialValue = ManageChatState()
        )

    fun onAction(action: ManageChatAction) {
        when (action) {
            ManageChatAction.OnAddClick -> {}
            ManageChatAction.OnPrimaryActionClick -> {}
            else -> Unit
        }
    }
}