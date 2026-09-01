package ru.missclick.chat.domain.message

import ru.missclick.chat.domain.models.ChatMessage
import ru.missclick.core.domain.util.DataError
import ru.missclick.core.domain.util.Result

interface ChatMessageService {
    suspend fun fetchMessages(
        chatId: String,
        before: String? = null
    ): Result<List<ChatMessage>, DataError.Remote>
}