package ru.missclick.chat.presentation.chatDetail.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import course_kmp.core.designsystem.generated.resources.Res as DesignSystemRes
import course_kmp.core.designsystem.generated.resources.arrow_left_icon
import course_kmp.core.designsystem.generated.resources.dots_icon
import course_kmp.core.designsystem.generated.resources.log_out_icon
import course_kmp.feature.chat.presentation.generated.resources.Res
import course_kmp.feature.chat.presentation.generated.resources.chat_members
import course_kmp.feature.chat.presentation.generated.resources.go_back
import course_kmp.feature.chat.presentation.generated.resources.leave_chat
import course_kmp.feature.chat.presentation.generated.resources.open_chat_options_menu
import course_kmp.feature.chat.presentation.generated.resources.users_icon
import org.jetbrains.compose.resources.stringResource
import org.jetbrains.compose.resources.vectorResource
import ru.missclick.chat.domain.models.ChatMessage
import ru.missclick.chat.presentation.components.ChatHeader
import ru.missclick.chat.presentation.components.ChatItemHeaderRow
import ru.missclick.chat.presentation.model.ChatUi
import ru.missclick.core.designsystem.components.avatar.ChatParticipantUi
import ru.missclick.core.designsystem.components.buttons.CourseIconButton
import ru.missclick.core.designsystem.components.dropdown.CourseDropDownMenu
import ru.missclick.core.designsystem.components.dropdown.DropDownItem
import ru.missclick.core.designsystem.theme.CourseTheme
import ru.missclick.core.designsystem.theme.extended
import kotlin.time.Clock

@Composable
fun ChatDetailHeader(
    chatUi: ChatUi?,
    isDetailPresent: Boolean,
    isChatOptionsDropDownOpen: Boolean,
    onChatOptionsClick: () -> Unit,
    onDismissChatOptions: () -> Unit,
    onManageChatClick: () -> Unit,
    onLeaveChatClick: () -> Unit,
    onBackClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .background(
                color = MaterialTheme.colorScheme.surface
            ),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        if (!isDetailPresent) {
            CourseIconButton(
                onClick = onBackClick
            ) {
                Icon(
                    imageVector = vectorResource(DesignSystemRes.drawable.arrow_left_icon),
                    contentDescription = stringResource(Res.string.go_back),
                    modifier = Modifier.size(20.dp),
                    tint = MaterialTheme.colorScheme.extended.textSecondary
                )
            }
        }

        if (chatUi != null) {
            val isGroupChat = chatUi.otherParticipants.size > 1
            ChatItemHeaderRow(
                chat = chatUi,
                isGroupChat = isGroupChat,
                modifier = Modifier
                    .weight(1f)
                    .clickable {
                        onManageChatClick()
                    }
            )
        } else {
            Spacer(Modifier.weight(1f))
        }

        Box {
            CourseIconButton(
                onClick = onChatOptionsClick
            ) {
                Icon(
                    imageVector = vectorResource(DesignSystemRes.drawable.dots_icon),
                    contentDescription = stringResource(Res.string.open_chat_options_menu),
                    modifier = Modifier.size(20.dp),
                    tint = MaterialTheme.colorScheme.extended.textSecondary
                )
            }

            CourseDropDownMenu(
                isOpen = isChatOptionsDropDownOpen,
                onDismiss = onDismissChatOptions,
                items = listOf(
                    DropDownItem(
                        title = stringResource(Res.string.chat_members),
                        icon = vectorResource(Res.drawable.users_icon),
                        contentColor = MaterialTheme.colorScheme.extended.textSecondary,
                        onClick = onManageChatClick
                    ),
                    DropDownItem(
                        title = stringResource(Res.string.leave_chat),
                        icon = vectorResource(DesignSystemRes.drawable.log_out_icon),
                        contentColor = MaterialTheme.colorScheme.extended.destructiveHover,
                        onClick = onLeaveChatClick
                    )
                )
            )
        }
    }
}

@Preview
@Composable
private fun ChatDetailHeaderPreview() {
    CourseTheme {
        Column(Modifier.fillMaxSize()) {
            ChatHeader {
                ChatDetailHeader(
                    chatUi = ChatUi(
                        id = "1",
                        localParticipant = ChatParticipantUi(
                            id = "1",
                            username = "Alex",
                            initials = "AA"
                        ),
                        otherParticipants = listOf(
                            ChatParticipantUi(
                                id = "2",
                                username = "Ann",
                                initials = "AV"
                            ),
                            ChatParticipantUi(
                                id = "3",
                                username = "John",
                                initials = "JJ"
                            )
                        ),
                        lastMessage = ChatMessage(
                            id = "1",
                            chatId = "1",
                            content = "Lorem Ipsum is simply dummy text of the printing and typesetting industry. Lorem Ipsum has been the industry's standard dummy text ever since 1966, when designers at Letraset and James Mosley, the librarian at St Bride Printing Library in London, took a 1914 Cicero translation and scrambled it to make dummy text for Letraset's Body Type sheets. It has survived not only many decades, but also the leap into electronic typesetting, remaining essentially unchanged. It was popularised thanks to these sheets and more recently with desktop publishing software like Aldus PageMaker and Microsoft Word including versions of Lorem Ipsum.",
                            createdAt = Clock.System.now(),
                            senderId = ""
                        ),
                        lastMessageSenderUsername = "Ann"
                    ),
                    isDetailPresent = false,
                    isChatOptionsDropDownOpen = true,
                    onChatOptionsClick = {},
                    onManageChatClick = {},
                    onLeaveChatClick = {},
                    onDismissChatOptions = {},
                    onBackClick = {}
                )
            }
        }
    }
}