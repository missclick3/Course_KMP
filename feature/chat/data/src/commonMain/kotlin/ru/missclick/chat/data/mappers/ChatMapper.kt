package ru.missclick.chat.data.mappers

import ru.missclick.chat.data.dto.ChatDto
import ru.missclick.chat.domain.models.Chat
import kotlin.time.Instant

fun ChatDto.toDomain() = Chat(
    id = id,
    participants = participants.map { it.toDomain() },
    lastActivityAt = Instant.parse(lastActivityAt),
    lastMessage = lastMessage?.toDomain()
)