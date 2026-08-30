package ru.missclick.chat.data.chat

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import ru.missclick.chat.data.mappers.toDomain
import ru.missclick.chat.data.mappers.toEntity
import ru.missclick.chat.data.mappers.toLastMessageView
import ru.missclick.chat.database.CourseChatDatabase
import ru.missclick.chat.database.entities.ChatWithParticipants
import ru.missclick.chat.domain.chat.ChatRepository
import ru.missclick.chat.domain.chat.ChatService
import ru.missclick.chat.domain.models.Chat
import ru.missclick.core.domain.util.DataError
import ru.missclick.core.domain.util.Result
import ru.missclick.core.domain.util.onSuccess

class OfflineFirstChatRepository(
    private val chatService: ChatService,
    private val db: CourseChatDatabase
): ChatRepository {

    override fun getChats(): Flow<List<Chat>> {
        return db.chatDao.getChatsWithActiveParticipants()
            .map { chatWithParticipantsList ->
                chatWithParticipantsList.map { it.toDomain()  }
            }
    }

    override suspend fun fetchChats(): Result<List<Chat>, DataError.Remote> {
        return chatService.getChats()
            .onSuccess { chats ->
                val chatsWithParticipants = chats.map { chat ->
                    ChatWithParticipants(
                        chat = chat.toEntity(),
                        participants = chat.participants.map { it.toEntity() },
                        lastMessage = chat.lastMessage?.toLastMessageView()
                    )
                }

                db.chatDao.upsertChatsWithParticipantsAndCrossRefs(
                    chats = chatsWithParticipants,
                    participantDao = db.chatParticipantDao,
                    crossRefDao = db.chatParticipantsCrossRefDao,
                    messageDao = db.chatMessageDao
                )
            }
    }
}