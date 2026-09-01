package ru.missclick.chat.presentation.mappers

import ru.missclick.chat.domain.models.MessageWithSender
import ru.missclick.chat.presentation.model.MessageUi
import ru.missclick.chat.presentation.util.DateUtils.formatMessageTime

fun MessageWithSender.toUi(
    localUserId: String
): MessageUi {
    val isFromLocalUser = this.sender.userId == localUserId

    return if (isFromLocalUser) {
        MessageUi.LocalUserMessage(
            id = message.id,
            content = message.content,
            deliveryStatus = message.deliveryStatus,
            formattedSentTime = formatMessageTime(
                instant = message.createdAt
            )
        )
    } else {
        MessageUi.OtherUserMessage(
            id = message.id,
            content = message.content,
            formattedSentTime = formatMessageTime(
                instant = message.createdAt
            ),
            sender = sender.toUi()
        )
    }
}