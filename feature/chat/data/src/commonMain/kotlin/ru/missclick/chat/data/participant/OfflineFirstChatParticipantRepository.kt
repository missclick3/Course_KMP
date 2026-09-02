package ru.missclick.chat.data.participant

import kotlinx.coroutines.flow.first
import ru.missclick.chat.domain.models.ChatParticipant
import ru.missclick.chat.domain.participant.ChatParticipantRepository
import ru.missclick.chat.domain.participant.ChatParticipantService
import ru.missclick.core.domain.auth.SessionStorage
import ru.missclick.core.domain.util.DataError
import ru.missclick.core.domain.util.Result
import ru.missclick.core.domain.util.onSuccess

class OfflineFirstChatParticipantRepository(
    private val sessionStorage: SessionStorage,
    private val chatParticipantService: ChatParticipantService
): ChatParticipantRepository {
    override suspend fun fetchLocalParticipant(): Result<ChatParticipant, DataError> {
        return chatParticipantService
            .getLocalParticipant()
            .onSuccess { participant ->
                val currentAuthInfo = sessionStorage.observeAuthInfo().first()
                sessionStorage.set(
                    currentAuthInfo?.copy(
                        user = currentAuthInfo.user.copy(
                            id = participant.userId,
                            username = participant.username,
                            profilePictureUrl = participant.profilePictureUrl
                        )
                    )
                )
            }
    }
}