package ru.missclick.chat.data.chat

import io.ktor.client.HttpClient
import ru.missclick.chat.data.dto.ChatParticipantDto
import ru.missclick.chat.data.mappers.toDomain
import ru.missclick.chat.domain.chat.ChatParticipantService
import ru.missclick.chat.domain.models.ChatParticipant
import ru.missclick.core.data.networking.get
import ru.missclick.core.domain.util.DataError
import ru.missclick.core.domain.util.Result
import ru.missclick.core.domain.util.map

class KtorChatParticipantService(
    private val httpClient: HttpClient
): ChatParticipantService {
    override suspend fun searchParticipant(query: String): Result<ChatParticipant, DataError.Remote> {
        return httpClient.get<ChatParticipantDto>(
            route = "/participants",
            queryParams = mapOf(
                "query" to query
            )
        ).map { it.toDomain() }
    }
}