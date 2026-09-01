package ru.missclick.chat.data.message

import ru.missclick.chat.database.CourseChatDatabase
import ru.missclick.chat.domain.chat.MessageRepository
import ru.missclick.chat.domain.models.ChatMessageDeliveryStatus
import ru.missclick.core.data.database.safeDatabaseUpdate
import ru.missclick.core.domain.util.DataError
import ru.missclick.core.domain.util.EmptyResult
import ru.missclick.core.domain.util.asEmptyResult
import kotlin.time.Clock

class OfflineFirstMessageRepository(
    private val database: CourseChatDatabase
): MessageRepository {
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
}