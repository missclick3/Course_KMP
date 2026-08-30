package ru.missclick.chat.data.mappers

import ru.missclick.chat.data.dto.ChatParticipantDto
import ru.missclick.chat.database.entities.ChatParticipantEntity
import ru.missclick.chat.domain.models.ChatParticipant

fun ChatParticipantDto.toDomain() = ChatParticipant(
    userId = userId,
    username = username,
    profilePictureUrl = profilePictureUrl
)

fun ChatParticipantEntity.toDomain(): ChatParticipant {
    return ChatParticipant(
        userId = userId,
        username = username,
        profilePictureUrl = profilePictureUrl
    )
}

fun ChatParticipant.toEntity(): ChatParticipantEntity {
    return ChatParticipantEntity(
        userId = userId,
        username = username,
        profilePictureUrl = profilePictureUrl
    )
}