package ru.missclick.chat.data.mappers

import ru.missclick.chat.data.dto.ChatMessageDto
import ru.missclick.chat.data.dto.websocket.OutgoingWebSocketDto
import ru.missclick.chat.database.entities.ChatMessageEntity
import ru.missclick.chat.database.view.LastMessageView
import ru.missclick.chat.domain.models.ChatMessage
import ru.missclick.chat.domain.models.ChatMessageDeliveryStatus
import kotlin.time.Instant

fun ChatMessageDto.toDomain() = ChatMessage(
    id = id,
    chatId = chatId,
    content = content,
    senderId = senderId,
    createdAt = Instant.parse(createdAt),
    deliveryStatus = ChatMessageDeliveryStatus.SENT
)

fun LastMessageView.toDomain(): ChatMessage {
    return ChatMessage(
        id = messageId,
        chatId = chatId,
        content = content,
        createdAt = Instant.fromEpochSeconds(timestamp),
        senderId = senderId,
        deliveryStatus = ChatMessageDeliveryStatus.valueOf(this.deliveryStatus)
    )
}

fun ChatMessage.toEntity(): ChatMessageEntity {
    return ChatMessageEntity(
        messageId = id,
        chatId = chatId,
        senderId = senderId,
        deliveryStatus = deliveryStatus.name,
        content = content,
        timestamp = createdAt.toEpochMilliseconds()
    )
}

fun ChatMessage.toLastMessageView(): LastMessageView {
    return LastMessageView(
        messageId = id,
        chatId = chatId,
        senderId = senderId,
        deliveryStatus = deliveryStatus.name,
        content = content,
        timestamp = createdAt.toEpochMilliseconds()
    )
}

fun LastMessageView.toEntity(): ChatMessageEntity {
    return ChatMessageEntity(
        messageId = messageId,
        chatId = chatId,
        senderId = senderId,
        deliveryStatus = deliveryStatus,
        content = content,
        timestamp = timestamp
    )
}

fun ChatMessageEntity.toDomain() = ChatMessage(
    id = messageId,
    chatId = chatId,
    content = content,
    senderId = senderId,
    createdAt = Instant.fromEpochSeconds(timestamp),
    deliveryStatus = ChatMessageDeliveryStatus.SENT
)

fun ChatMessage.toNewMessage(): OutgoingWebSocketDto.NewMessage {
    return OutgoingWebSocketDto.NewMessage(
        messageId = id,
        chatId = chatId,
        content = content,
    )
}
