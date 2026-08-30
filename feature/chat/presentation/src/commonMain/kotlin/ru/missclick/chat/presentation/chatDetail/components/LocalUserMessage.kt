package ru.missclick.chat.presentation.chatDetail.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import course_kmp.feature.chat.presentation.generated.resources.Res
import course_kmp.feature.chat.presentation.generated.resources.delete_for_everyone
import course_kmp.feature.chat.presentation.generated.resources.reload_icon
import course_kmp.feature.chat.presentation.generated.resources.retry
import course_kmp.feature.chat.presentation.generated.resources.you
import org.jetbrains.compose.resources.stringResource
import org.jetbrains.compose.resources.vectorResource
import ru.missclick.chat.domain.models.ChatMessageDeliveryStatus
import ru.missclick.chat.presentation.model.MessageUi
import ru.missclick.core.designsystem.components.chat.CourseChatBubble
import ru.missclick.core.designsystem.components.chat.TrianglePosition
import ru.missclick.core.designsystem.components.dropdown.CourseDropDownMenu
import ru.missclick.core.designsystem.components.dropdown.DropDownItem
import ru.missclick.core.designsystem.theme.extended

@Composable
fun LocalUserMessage(
    message: MessageUi.LocalUserMessage,
    onMessageLongClick: () -> Unit,
    onDismissMessageMenu: () -> Unit,
    onDeleteClick: () -> Unit,
    onRetryClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier
            .fillMaxWidth(),
        verticalAlignment = Alignment.Bottom,
        horizontalArrangement = Arrangement.spacedBy(8.dp, Alignment.End)
    ) {
        Box(Modifier.weight(1f)) {
            CourseChatBubble(
                messageContent = message.content,
                sender = stringResource(Res.string.you),
                formattedDateTime = message.formattedSentTime.asString(),
                trianglePosition = TrianglePosition.RIGHT,
                messageStatus = {
                    MessageStatusUi(
                        status = message.deliveryStatus
                    )
                },
                onLongClick = {
                    onMessageLongClick()
                }
            )

            CourseDropDownMenu(
                isOpen = message.isMenuOpen,
                items = listOf(
                    DropDownItem(
                        title = stringResource(Res.string.delete_for_everyone),
                        icon = Icons.Default.Delete,
                        contentColor = MaterialTheme.colorScheme.extended.destructiveHover,
                        onClick = onDeleteClick
                    )
                ),
                onDismiss = onDismissMessageMenu,
            )
        }

        if (message.deliveryStatus == ChatMessageDeliveryStatus.FAILED) {
            IconButton(
                onClick = onRetryClick
            ) {
                Icon(
                    imageVector = vectorResource(Res.drawable.reload_icon),
                    contentDescription = stringResource(Res.string.retry),
                    tint = MaterialTheme.colorScheme.error
                )
            }
        }
    }
}