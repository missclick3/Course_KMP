package ru.missclick.chat.presentation.chatList.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import ru.missclick.chat.domain.models.ChatMessage
import ru.missclick.chat.domain.models.ChatMessageDeliveryStatus
import ru.missclick.chat.presentation.components.ChatItemHeaderRow
import ru.missclick.chat.presentation.model.ChatUi
import ru.missclick.core.designsystem.components.avatar.ChatParticipantUi
import ru.missclick.core.designsystem.theme.CourseTheme
import ru.missclick.core.designsystem.theme.extended
import kotlin.time.Clock

@Composable
fun ChatListItemUi(
    chat: ChatUi,
    isSelected: Boolean,
    modifier: Modifier = Modifier
) {
    val isGroupChat = chat.otherParticipants.size > 1

    Row(
        modifier = modifier
            .height(IntrinsicSize.Min)
            .background(
                color = if (isSelected) {
                    MaterialTheme.colorScheme.surface
                } else {
                    MaterialTheme.colorScheme.extended.surfaceLower
                }
            )
            .fillMaxWidth()
    ) {
        Column(
            modifier = Modifier
                .weight(1f)
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            ChatItemHeaderRow(
                chat = chat,
                isGroupChat = isGroupChat,
                modifier = Modifier.fillMaxWidth()
            )

            if (chat.lastMessage != null) {
                val previewMessage = buildAnnotatedString {
                    withStyle(
                        style = SpanStyle(
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.extended.textSecondary,
                        )
                    ) {
                        if (chat.lastMessageSenderUsername != null) {
                            append(chat.lastMessageSenderUsername + ": ")
                        }
                    }
                    append(chat.lastMessage.content)
                }
                Text(
                    text = previewMessage,
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.extended.textSecondary,
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis,
                    modifier = Modifier.fillMaxWidth()
                )
            }
        }
        Box(
            modifier = Modifier
                .alpha(
                    if (isSelected) 1f else 0f
                )
                .background(MaterialTheme.colorScheme.primary)
                .width(4.dp)
                .fillMaxHeight()
        )
    }
}

@Preview
@Composable
private fun ChatListItemUiPreview() {
    CourseTheme {
        ChatListItemUi(
            chat = ChatUi(
                id = "1",
                localParticipant = ChatParticipantUi(
                    id = "1",
                    username = "Alex",
                    initials = "AA",
                    imageUrl = null,
                ),
                otherParticipants = listOf(
                    ChatParticipantUi(
                        id = "2",
                        username = "Ann",
                        initials = "AV",
                        imageUrl = null,
                    ),
                    ChatParticipantUi(
                        id = "3",
                        username = "John",
                        initials = "JA",
                        imageUrl = null,
                    )
                ),
                lastMessage = ChatMessage(
                    id = "1",
                    chatId = "1",
                    content = "Lorem Ipsum is simply dummy text of the printing and typesetting industry. Lorem Ipsum has been the industry's standard dummy text ever since 1966, when designers at Letraset and James Mosley, the librarian at St Bride Printing Library in London, took a 1914 Cicero translation and scrambled it to make dummy text for Letraset's Body Type sheets. It has survived not only many decades, but also the leap into electronic typesetting, remaining essentially unchanged. It was popularised thanks to these sheets and more recently with desktop publishing software like Aldus PageMaker and Microsoft Word including versions of Lorem Ipsum.",
                    createdAt = Clock.System.now(),
                    senderId = "",
                    deliveryStatus = ChatMessageDeliveryStatus.SENT
                ),
                lastMessageSenderUsername = "Ann"
            ),
            isSelected = true,
            modifier = Modifier.fillMaxWidth()
        )
    }
}