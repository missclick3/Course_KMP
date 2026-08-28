package ru.missclick.chat.presentation.chatDetail.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import ru.missclick.chat.presentation.model.MessageUi
import ru.missclick.core.designsystem.components.avatar.CourseAvatarPhoto
import ru.missclick.core.designsystem.components.chat.CourseChatBubble
import ru.missclick.core.designsystem.components.chat.TrianglePosition

@Composable
fun OtherUserMessage(
    message: MessageUi.OtherUserMessage,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier.fillMaxWidth(),
        verticalAlignment = Alignment.Bottom,
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        CourseAvatarPhoto(
            displayText = message.sender.initials,
            imageUrl = message.sender.imageUrl
        )
        CourseChatBubble(
            messageContent = message.content,
            sender = message.sender.username,
            trianglePosition = TrianglePosition.LEFT,
            formattedDateTime = message.formattedSentTime.asString()
        )
    }
}