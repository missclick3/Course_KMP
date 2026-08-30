package ru.missclick.chat.data.mappers

import ru.missclick.chat.data.dto.ChatDto
import ru.missclick.chat.database.entities.ChatEntity
import ru.missclick.chat.database.entities.ChatWithParticipants
import ru.missclick.chat.domain.models.Chat
import kotlin.time.Instant

fun ChatDto.toDomain() = Chat(
    id = id,
    participants = participants.map { it.toDomain() },
    lastActivityAt = Instant.parse(lastActivityAt),
    lastMessage = lastMessage?.toDomain()
)

fun ChatWithParticipants.toDomain(): Chat {
    return Chat(
        id = chat.chatId,
        participants = participants.map { it.toDomain() },
        lastActivityAt = Instant.fromEpochSeconds(chat.lastActivityAt),
        lastMessage = lastMessage?.toDomain()
    )
}

fun Chat.toEntity(): ChatEntity {
    return ChatEntity(
        chatId = id,
        lastActivityAt = lastActivityAt.toEpochMilliseconds()
    )
}