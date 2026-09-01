package ru.missclick.chat.domain.chat

import ru.missclick.chat.domain.models.ChatMessageDeliveryStatus
import ru.missclick.core.domain.util.DataError
import ru.missclick.core.domain.util.EmptyResult

interface MessageRepository {
    suspend fun updateMessageDeliveryStatus(
        messageId: String,
        status: ChatMessageDeliveryStatus
    ): EmptyResult<DataError.Local>
}