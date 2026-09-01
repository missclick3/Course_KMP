package ru.missclick.chat.domain.chat

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.StateFlow
import ru.missclick.chat.domain.error.ConnectionError
import ru.missclick.chat.domain.models.ChatMessage
import ru.missclick.chat.domain.models.ConnectionState
import ru.missclick.core.domain.util.EmptyResult

interface ChatConnectionClient {
    val chatMessages: Flow<ChatMessage>
    val connectionState: StateFlow<ConnectionState>
    suspend fun sendChatMessage(message: ChatMessage): EmptyResult<ConnectionError>
}