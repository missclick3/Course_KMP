package ru.missclick.chat.domain.chat

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.StateFlow
import ru.missclick.chat.domain.models.ChatMessage
import ru.missclick.chat.domain.models.ConnectionState

interface ChatConnectionClient {
    val chatMessages: Flow<ChatMessage>
    val connectionState: StateFlow<ConnectionState>
}