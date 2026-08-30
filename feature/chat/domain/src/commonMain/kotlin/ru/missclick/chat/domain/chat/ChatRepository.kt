package ru.missclick.chat.domain.chat

import kotlinx.coroutines.flow.Flow
import ru.missclick.chat.domain.models.Chat
import ru.missclick.chat.domain.models.ChatInfo
import ru.missclick.core.domain.util.DataError
import ru.missclick.core.domain.util.EmptyResult
import ru.missclick.core.domain.util.Result

interface ChatRepository {
    fun getChats(): Flow<List<Chat>>
    fun getChatInfoById(chatId: String): Flow<ChatInfo>
    suspend fun fetchChats(): Result<List<Chat>, DataError.Remote>
    suspend fun fetchChatById(chatId: String): EmptyResult<DataError.Remote>
}