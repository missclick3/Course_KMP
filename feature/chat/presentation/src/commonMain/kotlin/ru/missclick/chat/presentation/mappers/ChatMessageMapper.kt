package ru.missclick.chat.presentation.mappers

import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime
import ru.missclick.chat.domain.models.MessageWithSender
import ru.missclick.chat.presentation.model.MessageUi
import ru.missclick.chat.presentation.util.DateUtils
import ru.missclick.chat.presentation.util.DateUtils.formatMessageTime

fun List<MessageWithSender>.toUiList(localUserId: String): List<MessageUi> {
    return this
        .sortedByDescending { it.message.createdAt }
        .groupBy {
            it.message.createdAt.toLocalDateTime(TimeZone.currentSystemDefault()).date
        }
        .flatMap { (date, messages) ->
            messages.map { it.toUi(localUserId) } + MessageUi.DateSeparator(
                id = date.toString(),
                date = DateUtils.formatDateSeparator(date)
            )
        }
}

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