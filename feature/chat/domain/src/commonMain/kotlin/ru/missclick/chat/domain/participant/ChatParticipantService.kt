package ru.missclick.chat.domain.participant

import ru.missclick.chat.domain.models.ChatParticipant
import ru.missclick.core.domain.util.DataError
import ru.missclick.core.domain.util.Result

interface ChatParticipantService {
    suspend fun searchParticipant(
        query: String
    ): Result<ChatParticipant, DataError.Remote>

    suspend fun getLocalParticipant(): Result<ChatParticipant, DataError.Remote>
}