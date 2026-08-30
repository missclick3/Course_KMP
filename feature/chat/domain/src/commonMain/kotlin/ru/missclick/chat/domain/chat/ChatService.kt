package ru.missclick.chat.domain.chat

import ru.missclick.chat.domain.models.Chat
import ru.missclick.core.domain.util.DataError
import ru.missclick.core.domain.util.EmptyResult
import ru.missclick.core.domain.util.Result

interface ChatService {
    suspend fun createChat(
        otherUserIds: List<String>,
    ): Result<Chat, DataError.Remote>

    suspend fun getChats(): Result<List<Chat>, DataError.Remote>

    suspend fun getChatById(chatId: String): Result<Chat, DataError.Remote>

    suspend fun leaveChat(chatId: String): EmptyResult<DataError.Remote>

    suspend fun addParticipantsToChat(
        chatId: String,
        userIds: List<String>
    ): Result<Chat, DataError.Remote>
}