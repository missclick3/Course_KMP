package ru.missclick.chat.presentation.mappers

import ru.missclick.chat.domain.models.ChatParticipant
import ru.missclick.core.designsystem.components.avatar.ChatParticipantUi

fun ChatParticipant.toUi() = ChatParticipantUi(
    id = userId,
    username = username,
    initials = initials,
    imageUrl = profilePictureUrl
)