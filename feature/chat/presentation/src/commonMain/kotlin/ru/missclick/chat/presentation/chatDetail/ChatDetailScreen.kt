package ru.missclick.chat.presentation.chatDetail

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.input.rememberTextFieldState
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.backhandler.BackHandler
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import org.koin.compose.viewmodel.koinViewModel
import ru.missclick.chat.domain.models.ChatMessage
import ru.missclick.chat.domain.models.ChatMessageDeliveryStatus
import ru.missclick.chat.presentation.chatDetail.components.ChatDetailHeader
import ru.missclick.chat.presentation.chatDetail.components.MessageBox
import ru.missclick.chat.presentation.chatDetail.components.MessageList
import ru.missclick.chat.presentation.components.ChatHeader
import ru.missclick.chat.presentation.model.ChatUi
import ru.missclick.chat.presentation.model.MessageUi
import ru.missclick.core.designsystem.components.avatar.ChatParticipantUi
import ru.missclick.core.designsystem.theme.CourseTheme
import ru.missclick.core.designsystem.theme.extended
import ru.missclick.core.presentation.util.ObserveAsEvents
import ru.missclick.core.presentation.util.UiText
import ru.missclick.core.presentation.util.clearFocusOnTap
import ru.missclick.core.presentation.util.currentDeviceConfiguration
import kotlin.time.Clock
import kotlin.uuid.Uuid

@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun ChatDetailRoot(
    chatId: String?,
    isDetailPresent: Boolean,
    onBack: () -> Unit,
    viewModel: ChatDetailViewModel = koinViewModel()
) {
    val state by viewModel.state.collectAsStateWithLifecycle()

    LaunchedEffect(chatId) {
        viewModel.onAction(ChatDetailAction.OnSelectChat(chatId))
    }

    BackHandler(
        enabled = !isDetailPresent
    ) {
        viewModel.onAction(ChatDetailAction.OnSelectChat(null))
        onBack()
    }

    val snackbarState = remember { SnackbarHostState() }
    ObserveAsEvents(viewModel.events) { event ->
        when (event) {
            ChatDetailEvent.OnChatLeft -> onBack()
            is ChatDetailEvent.OnError -> {
                snackbarState.showSnackbar(event.error.asStringAsync())
            }
        }
    }

    ChatDetailScreen(
        state = state,
        isDetailPresent = isDetailPresent,
        onAction = viewModel::onAction,
        snackbarHostState = snackbarState
    )
}

@Composable
fun ChatDetailScreen(
    state: ChatDetailState,
    onAction: (ChatDetailAction) -> Unit,
    isDetailPresent: Boolean,
    snackbarHostState: SnackbarHostState,
) {
    val configuration = currentDeviceConfiguration()
    val messageListState = rememberLazyListState()

    Scaffold(
        modifier = Modifier
            .fillMaxSize(),
        containerColor = if (!configuration.isWideScreen) {
            MaterialTheme.colorScheme.surface
        } else MaterialTheme.colorScheme.extended.surfaceLower,
        snackbarHost = {
            SnackbarHost(snackbarHostState)
        }
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .clearFocusOnTap()
                .padding(innerPadding)
                .then(
                    if (configuration.isWideScreen) {
                        Modifier.padding(horizontal = 8.dp)
                    } else Modifier
                )
        ) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                DynamicRoundedCornerColumn(
                    isCornerRounded = configuration.isWideScreen,
                    modifier = Modifier.weight(1f).fillMaxWidth()
                ) {
                    ChatHeader {
                        ChatDetailHeader(
                            chatUi = state.chatUi,
                            isDetailPresent = isDetailPresent,
                            isChatOptionsDropDownOpen = state.isChatOptionsOpen,
                            onChatOptionsClick = {
                                onAction(ChatDetailAction.OnChatOptionsClick)
                            },
                            onDismissChatOptions = {
                                onAction(ChatDetailAction.OnDismissChatOptions)
                            },
                            onManageChatClick = {
                                onAction(ChatDetailAction.OnChatMembersClick)
                            },
                            onLeaveChatClick = {
                                onAction(ChatDetailAction.OnLeaveChatClick)
                            },
                            onBackClick = {
                                onAction(ChatDetailAction.OnBackClick)
                            },
                            modifier = Modifier.fillMaxWidth()
                        )
                    }

                    MessageList(
                        messages = state.messages,
                        listState = messageListState,
                        onMessageLongClick = { message ->
                            onAction(ChatDetailAction.OnMessageLongClick(message))
                        },
                        onMessageRetryClick = { message ->
                            onAction(ChatDetailAction.OnRetryClick(message))
                        },
                        onDismissMessageMenu = {
                            onAction(ChatDetailAction.OnDismissMessageMenu)
                        },
                        onDeleteMessageClick = { message ->
                            onAction(ChatDetailAction.OnDeleteMessageClick(message))
                        },
                        modifier = Modifier
                            .fillMaxWidth()
                            .weight(1f)
                    )

                    AnimatedVisibility(
                        visible = !configuration.isWideScreen && state.chatUi != null
                    ) {
                        MessageBox(
                            messageTextFieldState = state.messageTextFieldState,
                            isTextInputEnabled = state.canSendMessage,
                            connectionState = state.connectionState,
                            onSendClick = {
                                onAction(ChatDetailAction.OnSendMessageClick)
                            },
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(
                                    vertical = 8.dp,
                                    horizontal = 16.dp
                                )
                        )
                    }
                }

                if (configuration.isWideScreen) {
                    Spacer(Modifier.height(8.dp))
                }

                AnimatedVisibility(
                    visible = configuration.isWideScreen && state.chatUi != null
                ) {
                    DynamicRoundedCornerColumn(
                        isCornerRounded = configuration.isWideScreen
                    ) {
                        MessageBox(
                            messageTextFieldState = state.messageTextFieldState,
                            isTextInputEnabled = state.canSendMessage,
                            connectionState = state.connectionState,
                            onSendClick = {
                                onAction(ChatDetailAction.OnSendMessageClick)
                            },
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(8.dp)
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun DynamicRoundedCornerColumn(
    isCornerRounded: Boolean,
    modifier: Modifier = Modifier,
    content: @Composable ColumnScope.() -> Unit
) {
    Column(
        modifier = modifier
            .shadow(
                elevation = if (isCornerRounded) 8.dp else 0.dp,
                shape = if (isCornerRounded) RoundedCornerShape(24.dp) else RectangleShape,
                spotColor = Color.Black.copy(alpha = 0.2f)
            )
            .background(
                color = MaterialTheme.colorScheme.surface,
                shape = if (isCornerRounded) RoundedCornerShape(24.dp) else RectangleShape
            )
    ) {
        content()
    }
}

@Preview
@Composable
private fun ChatDetailEmptyPreview() {
    CourseTheme {
        ChatDetailScreen(
            state = ChatDetailState(),
            isDetailPresent = false,
            onAction = {},
            snackbarHostState = remember { SnackbarHostState() }
        )
    }
}

@Preview
@Composable
private fun ChatDetailMessagesPreview() {
    CourseTheme(darkTheme = true) {
        ChatDetailScreen(
            state = ChatDetailState(
                messageTextFieldState = rememberTextFieldState(
                    initialText = "This is a new message!"
                ),
                canSendMessage = true,
                chatUi = ChatUi(
                    id = "1",
                    localParticipant = ChatParticipantUi(
                        id = "1",
                        username = "Philipp",
                        initials = "PH",
                    ),
                    otherParticipants = listOf(
                        ChatParticipantUi(
                            id = "2",
                            username = "Cinderella",
                            initials = "CI",
                        ),
                        ChatParticipantUi(
                            id = "3",
                            username = "Josh",
                            initials = "JO",
                        )
                    ),
                    lastMessage = ChatMessage(
                        id = "1",
                        chatId = "1",
                        content = "This is a last chat message that was sent by Philipp " +
                                "and goes over multiple lines to showcase the ellipsis",
                        createdAt = Clock.System.now(),
                        senderId = "1",
                        deliveryStatus = ChatMessageDeliveryStatus.SENT
                    ),
                    lastMessageSenderUsername = "Philipp"
                ),
                messages = (1..20).map {
                    if(it % 2 == 0) {
                        MessageUi.LocalUserMessage(
                            id = Uuid.random().toString(),
                            content = "Hello world!",
                            deliveryStatus = ChatMessageDeliveryStatus.SENT,
                            isMenuOpen = false,
                            formattedSentTime = UiText.DynamicString("Friday, Aug 20")
                        )
                    } else {
                        MessageUi.OtherUserMessage(
                            id = Uuid.random().toString(),
                            content = "Hello world!",
                            sender = ChatParticipantUi(
                                id = Uuid.random().toString(),
                                username = "John",
                                initials = "JO"
                            ),
                            formattedSentTime = UiText.DynamicString("Friday, Aug 20"),
                        )
                    }
                }
            ),
            isDetailPresent = true,
            onAction = {},
            snackbarHostState = remember { SnackbarHostState() }
        )
    }
}