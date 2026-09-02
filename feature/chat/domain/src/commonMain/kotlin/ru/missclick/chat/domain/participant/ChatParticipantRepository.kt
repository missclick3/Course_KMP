package ru.missclick.chat.domain.participant

import ru.missclick.chat.domain.models.ChatParticipant
import ru.missclick.core.domain.util.DataError
import ru.missclick.core.domain.util.EmptyResult
import ru.missclick.core.domain.util.Result

interface ChatParticipantRepository {

    suspend fun fetchLocalParticipant(): Result<ChatParticipant, DataError>

    suspend fun uploadProfilePicture(
        imageBytes: ByteArray,
        mimeType: String
    ): EmptyResult<DataError.Remote>
}