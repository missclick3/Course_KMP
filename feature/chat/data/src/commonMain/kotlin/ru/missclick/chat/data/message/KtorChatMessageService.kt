package ru.missclick.chat.data.message

import io.ktor.client.HttpClient
import ru.missclick.chat.data.dto.ChatMessageDto
import ru.missclick.chat.data.mappers.toDomain
import ru.missclick.chat.data.message.ChatMessageConstants.PAGE_SIZE
import ru.missclick.chat.domain.message.ChatMessageService
import ru.missclick.chat.domain.models.ChatMessage
import ru.missclick.core.data.networking.delete
import ru.missclick.core.data.networking.get
import ru.missclick.core.domain.util.DataError
import ru.missclick.core.domain.util.EmptyResult
import ru.missclick.core.domain.util.Result
import ru.missclick.core.domain.util.map

class KtorChatMessageService(
    private val httpClient: HttpClient
): ChatMessageService {

    override suspend fun fetchMessages(
        chatId: String,
        before: String?
    ): Result<List<ChatMessage>, DataError.Remote> {
        return httpClient.get<List<ChatMessageDto>>(
            route = "/chat/$chatId/messages",
            queryParams = buildMap {
                this["pageSize"] = PAGE_SIZE
                if (before != null) {
                    this["before"] = before
                }
            }
        ).map { it.map { it.toDomain() } }
    }

    override suspend fun deleteMessage(messageId: String): EmptyResult<DataError.Remote> {
        return httpClient.delete(
            route = "/messages/$messageId"
        )
    }
}