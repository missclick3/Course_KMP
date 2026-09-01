package ru.missclick.chat.data.message

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import kotlinx.serialization.json.Json
import ru.missclick.chat.data.dto.websocket.OutgoingWebSocketDto
import ru.missclick.chat.data.dto.websocket.WebSocketMessageDto
import ru.missclick.chat.data.mappers.toDomain
import ru.missclick.chat.data.mappers.toEntity
import ru.missclick.chat.data.mappers.toWebSockedDto
import ru.missclick.chat.data.message.ChatMessageConstants.PAGE_SIZE
import ru.missclick.chat.data.network.KtorWebSocketConnector
import ru.missclick.chat.database.CourseChatDatabase
import ru.missclick.chat.domain.message.ChatMessageService
import ru.missclick.chat.domain.message.MessageRepository
import ru.missclick.chat.domain.models.ChatMessage
import ru.missclick.chat.domain.models.ChatMessageDeliveryStatus
import ru.missclick.chat.domain.models.MessageWithSender
import ru.missclick.chat.domain.models.OutgoingNewMessage
import ru.missclick.core.data.database.safeDatabaseUpdate
import ru.missclick.core.domain.auth.SessionStorage
import ru.missclick.core.domain.util.DataError
import ru.missclick.core.domain.util.EmptyResult
import ru.missclick.core.domain.util.Result
import ru.missclick.core.domain.util.asEmptyResult
import ru.missclick.core.domain.util.onFailure
import ru.missclick.core.domain.util.onSuccess
import kotlin.time.Clock

class OfflineFirstMessageRepository(
    private val database: CourseChatDatabase,
    private val chatMessageService: ChatMessageService,
    private val sessionStorage: SessionStorage,
    private val json: Json,
    private val webSocketConnector: KtorWebSocketConnector,
    private val applicationScope: CoroutineScope
): MessageRepository {

    override suspend fun sendMessage(message: OutgoingNewMessage): EmptyResult<DataError> {
        return safeDatabaseUpdate {
            val dto = message.toWebSockedDto()

            val localUser = sessionStorage.observeAuthInfo().firstOrNull()?.user
                ?: return Result.Failure(DataError.Local.NOT_FOUND)

            val entity = dto.toEntity(
                senderId = localUser.id,
                deliveryStatus = ChatMessageDeliveryStatus.SENDING
            )
            database.chatMessageDao.upsertMessage(entity)

            return webSocketConnector
                .sendMessage(dto.toJsonPayload())
                .onFailure { error ->
                    applicationScope.launch {
                        database.chatMessageDao.updateDeliveryStatus(
                            messageId = entity.messageId,
                            status = ChatMessageDeliveryStatus.FAILED.name,
                            timestamp = Clock.System.now().toEpochMilliseconds()
                        )
                    }.join()
                }
        }
    }

    override suspend fun updateMessageDeliveryStatus(
        messageId: String,
        status: ChatMessageDeliveryStatus
    ): EmptyResult<DataError.Local> {
        return safeDatabaseUpdate {
            database.chatMessageDao.updateDeliveryStatus(
                messageId = messageId,
                status = status.name,
                timestamp = Clock.System.now().toEpochMilliseconds()
            )
        }.asEmptyResult()
    }

    override suspend fun fetchMessages(
        chatId: String,
        before: String?
    ): Result<List<ChatMessage>, DataError> {
        return chatMessageService
            .fetchMessages(chatId, before)
            .onSuccess { messages ->
                return safeDatabaseUpdate {
                    val entities = messages.map { it.toEntity() }
                    database.chatMessageDao.upsertMessagesAndSyncIfNecessary(
                        chatId = chatId,
                        serverMessages = entities,
                        pageSize = PAGE_SIZE,
                        shouldSync = before == null
                    )
                    messages
                }
            }
    }

    override fun getMessagesForChat(chatId: String): Flow<List<MessageWithSender>> {
        return database
            .chatMessageDao
            .getMessagesByChat(chatId)
            .map { messages ->
                messages.map { it.toDomain() }
            }
    }

    override suspend fun retryMessage(messageId: String): EmptyResult<DataError> {
        return safeDatabaseUpdate {
            val message = database.chatMessageDao.getMessageById(messageId)
                ?: return Result.Failure(DataError.Local.NOT_FOUND)

            database.chatMessageDao.updateDeliveryStatus(
                messageId = messageId,
                timestamp = Clock.System.now().toEpochMilliseconds(),
                status = ChatMessageDeliveryStatus.SENDING.name
            )

            val outgoingNewMessage = OutgoingWebSocketDto.NewMessage(
                chatId = message.chatId,
                messageId = messageId,
                content = message.content
            )
            return webSocketConnector
                .sendMessage(outgoingNewMessage.toJsonPayload())
                .onFailure {
                    applicationScope.launch {
                        database.chatMessageDao.updateDeliveryStatus(
                            messageId = messageId,
                            status = ChatMessageDeliveryStatus.FAILED.name,
                            timestamp = Clock.System.now().toEpochMilliseconds()
                        )
                    }.join()
                }
        }
    }

    private fun OutgoingWebSocketDto.NewMessage.toJsonPayload(): String {
        val webSocketMessage = WebSocketMessageDto(
            type = type.name,
            payload = json.encodeToString(this)
        )
        return json.encodeToString(webSocketMessage)
    }
}