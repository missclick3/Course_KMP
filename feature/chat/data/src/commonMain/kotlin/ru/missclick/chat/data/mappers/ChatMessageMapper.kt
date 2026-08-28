package ru.missclick.chat.data.mappers

import ru.missclick.chat.data.dto.ChatMessageDto
import ru.missclick.chat.domain.models.ChatMessage
import kotlin.time.Instant

fun ChatMessageDto.toDomain() = ChatMessage(
    id = id,
    chatId = chatId,
    content = content,
    senderId = senderId,
    createdAt = Instant.parse(createdAt)
)