package ru.missclick.chat.presentation.mappers

import ru.missclick.chat.domain.models.ChatParticipant
import ru.missclick.core.designsystem.components.avatar.ChatParticipantUi
import ru.missclick.core.domain.auth.User

fun ChatParticipant.toUi() = ChatParticipantUi(
    id = userId,
    username = username,
    initials = initials,
    imageUrl = profilePictureUrl
)

fun User.toUi(): ChatParticipantUi {
    return ChatParticipantUi(
        id = id,
        username = username,
        initials = username.take(2).uppercase(),
        imageUrl = profilePictureUrl
    )
}