package ru.missclick.chat.data.mappers

import ru.missclick.chat.data.dto.ChatDto
import ru.missclick.chat.database.entities.ChatEntity
import ru.missclick.chat.database.entities.ChatInfoEntity
import ru.missclick.chat.database.entities.ChatWithParticipants
import ru.missclick.chat.database.entities.MessageWithSender
import ru.missclick.chat.domain.models.Chat
import ru.missclick.chat.domain.models.ChatInfo
import ru.missclick.chat.domain.models.ChatMessage
import ru.missclick.chat.domain.models.ChatMessageDeliveryStatus
import ru.missclick.chat.domain.models.ChatParticipant
import kotlin.time.Instant

typealias DataMessageWithSender = MessageWithSender
typealias DomainMessageWithSender = ru.missclick.chat.domain.models.MessageWithSender

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

fun ChatInfoEntity.toDomain(): ChatInfo {
    return ChatInfo(
        chat = chat.toDomain(
            participants = this.participants.map { it.toDomain() }
        ),
        messages = messagesWithSenders.map { it.toDomain() }
    )
}

fun DataMessageWithSender.toDomain(): DomainMessageWithSender {
    return DomainMessageWithSender(
        message = message.toDomain(),
        sender = sender.toDomain(),
        deliveryStatus = ChatMessageDeliveryStatus.valueOf(this.message.deliveryStatus)
    )
}

fun ChatEntity.toDomain(
    participants: List<ChatParticipant>,
    lastMessage: ChatMessage? = null
) = Chat(
    id = chatId,
    participants = participants,
    lastActivityAt = Instant.fromEpochSeconds(lastActivityAt),
    lastMessage = lastMessage
)