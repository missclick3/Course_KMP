package ru.missclick.chat.presentation.chatList

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import ru.missclick.chat.domain.chat.ChatRepository
import ru.missclick.chat.presentation.mappers.toUi
import ru.missclick.core.domain.auth.SessionStorage

class ChatListViewModel(
    private val repository: ChatRepository,
    sessionStorage: SessionStorage
) : ViewModel() {

    private var hasLoadedInitialData = false

    private val _state = MutableStateFlow(ChatListState())
    val state = combine(
        _state,
        repository.getChats(),
        sessionStorage.observeAuthInfo()
    ) { currentState, chats, authInfo ->
        if (authInfo == null) {
            return@combine ChatListState()
        }
        currentState.copy(
            chats = chats.map { it.toUi(authInfo.user.id) },
            localParticipantUi = authInfo.user.toUi()
        )
    }
        .onStart {
            if (!hasLoadedInitialData) {
                loadChats()
                hasLoadedInitialData = true
            }
        }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5_000L),
            initialValue = ChatListState()
        )

    fun onAction(action: ChatListAction) {
        when (action) {
            is ChatListAction.OnChatClick -> {
                _state.update {
                    it.copy(
                        selectedChatId = action.chat.id
                    )
                }
            }
            else -> Unit
        }
    }

    private fun loadChats() {
        viewModelScope.launch {
            repository.fetchChats()
        }
    }
}