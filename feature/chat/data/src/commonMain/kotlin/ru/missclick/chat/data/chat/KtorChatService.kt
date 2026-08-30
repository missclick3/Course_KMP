package ru.missclick.chat.data.chat

import io.ktor.client.HttpClient
import ru.missclick.chat.data.dto.ChatDto
import ru.missclick.chat.data.dto.request.CreateChatRequest
import ru.missclick.chat.data.mappers.toDomain
import ru.missclick.chat.domain.chat.ChatService
import ru.missclick.chat.domain.models.Chat
import ru.missclick.core.data.networking.delete
import ru.missclick.core.data.networking.get
import ru.missclick.core.data.networking.post
import ru.missclick.core.domain.util.DataError
import ru.missclick.core.domain.util.EmptyResult
import ru.missclick.core.domain.util.Result
import ru.missclick.core.domain.util.asEmptyResult
import ru.missclick.core.domain.util.map

class KtorChatService(
    private val httpClient: HttpClient
): ChatService {
    override suspend fun createChat(otherUserIds: List<String>): Result<Chat, DataError.Remote> {
        return httpClient.post<CreateChatRequest, ChatDto>(
            route = "/chat",
            body = CreateChatRequest(
                otherUserIds = otherUserIds
            )
        ).map { it.toDomain() }
    }

    override suspend fun getChats(): Result<List<Chat>, DataError.Remote> {
        return httpClient.get<List<ChatDto>>(
            route = "/chat"
        ).map { chatDtos ->
            chatDtos.map { it.toDomain() }
        }
    }

    override suspend fun getChatById(chatId: String): Result<Chat, DataError.Remote> {
        return httpClient.get<ChatDto>(
            route = "/chat/$chatId"
        ).map { it.toDomain() }
    }

    override suspend fun leaveChat(chatId: String): EmptyResult<DataError.Remote> {
        return httpClient.delete<Unit>(
            route = "/chat/$chatId/leave"
        ).asEmptyResult()
    }
}