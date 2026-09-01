package ru.missclick.chat.data.chat

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.serialization.json.Json
import ru.missclick.chat.data.dto.websocket.WebSocketMessageDto
import ru.missclick.chat.data.mappers.toNewMessage
import ru.missclick.chat.data.network.KtorWebSocketConnector
import ru.missclick.chat.database.CourseChatDatabase
import ru.missclick.chat.domain.chat.ChatConnectionClient
import ru.missclick.chat.domain.chat.ChatRepository
import ru.missclick.chat.domain.chat.MessageRepository
import ru.missclick.chat.domain.error.ConnectionError
import ru.missclick.chat.domain.models.ChatMessage
import ru.missclick.chat.domain.models.ChatMessageDeliveryStatus
import ru.missclick.chat.domain.models.ConnectionState
import ru.missclick.core.domain.auth.SessionStorage
import ru.missclick.core.domain.util.EmptyResult
import ru.missclick.core.domain.util.onFailure

class WebSocketChatConnectionClient(
    private val webSocketConnector: KtorWebSocketConnector,
    private val chatRepository: ChatRepository,
    private val database: CourseChatDatabase,
    private val sessionStorage: SessionStorage,
    private val json: Json,
    private val messageRepository: MessageRepository
): ChatConnectionClient {

    override val chatMessages: Flow<ChatMessage> = TODO()
    override val connectionState: StateFlow<ConnectionState> = webSocketConnector.connectionState

    override suspend fun sendChatMessage(message: ChatMessage): EmptyResult<ConnectionError> {
        val outgoingDto = message.toNewMessage()
        val webSocketMessage = WebSocketMessageDto(
            type = outgoingDto.type.name,
            payload = json.encodeToString(outgoingDto)
        )
        val rawJsonPayload = json.encodeToString(webSocketMessage)

        return webSocketConnector
            .sendMessage(rawJsonPayload)
            .onFailure { error ->
                messageRepository.updateMessageDeliveryStatus(
                    messageId = message.id,
                    status = ChatMessageDeliveryStatus.FAILED
                )
            }
    }
}