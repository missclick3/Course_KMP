package ru.missclick.chat.presentation.chatList

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawing
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import course_kmp.feature.chat.presentation.generated.resources.Res
import course_kmp.feature.chat.presentation.generated.resources.cancel
import course_kmp.feature.chat.presentation.generated.resources.create_chat
import course_kmp.feature.chat.presentation.generated.resources.do_you_want_to_log_out
import course_kmp.feature.chat.presentation.generated.resources.do_you_want_to_log_out_desc
import course_kmp.feature.chat.presentation.generated.resources.logout
import org.jetbrains.compose.resources.stringResource
import org.koin.compose.viewmodel.koinViewModel
import ru.missclick.chat.presentation.chatList.components.ChatListHeader
import ru.missclick.chat.presentation.chatList.components.ChatListItemUi
import ru.missclick.chat.presentation.chatList.components.EmptyChatSection
import ru.missclick.chat.presentation.model.ChatUi
import ru.missclick.core.designsystem.components.brand.CourseHorizontalDivider
import ru.missclick.core.designsystem.components.buttons.CourseFloatingActionButton
import ru.missclick.core.designsystem.components.dialog.DestructiveConfirmationDialog
import ru.missclick.core.designsystem.theme.CourseTheme
import ru.missclick.core.designsystem.theme.extended

@Composable
fun ChatListRoot(
    onChatClick: (ChatUi) -> Unit,
    onConfirmLogoutClick: () -> Unit,
    onCreateChatClick: () -> Unit,
    onProfileSettingsClick: () -> Unit,
    viewModel: ChatListViewModel = koinViewModel()
) {
    val state by viewModel.state.collectAsStateWithLifecycle()

    val snackbarHostState = remember { SnackbarHostState() }

    ChatListScreen(
        state = state,
        onAction = { action ->
            when (action) {
                is ChatListAction.OnChatClick -> onChatClick(action.chat)
                ChatListAction.OnConfirmLogout -> onConfirmLogoutClick()
                ChatListAction.OnCreateChatClick -> onCreateChatClick()
                ChatListAction.OnProfileSettingsClick -> onProfileSettingsClick()
                else -> Unit
            }
            viewModel.onAction(action)
        },
        snackbarHostState = snackbarHostState
    )
}

@Composable
fun ChatListScreen(
    state: ChatListState,
    onAction: (ChatListAction) -> Unit,
    snackbarHostState: SnackbarHostState
) {
    Scaffold(
        modifier = Modifier.fillMaxSize(),
        containerColor = MaterialTheme.colorScheme.extended.surfaceLower,
        contentWindowInsets = WindowInsets.safeDrawing,
        snackbarHost = { SnackbarHost(snackbarHostState) },
        floatingActionButton = {
            CourseFloatingActionButton(
                onClick = {
                    onAction(ChatListAction.OnCreateChatClick)
                }
            ) {
                Icon(
                    imageVector = Icons.Default.Add,
                    contentDescription = stringResource(Res.string.create_chat)
                )
            }
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            ChatListHeader(
                localParticipant = state.localParticipantUi,
                isUserMenuOpen = state.isUserMenuOpen,
                onUserAvatarClick = {
                    onAction(ChatListAction.OnUserAvatarClick)
                },
                onLogoutClick = {
                    onAction(ChatListAction.OnLogoutClick)
                },
                onDismissMenu = {
                    onAction(ChatListAction.OnDismissUserMenu)
                },
                onProfileSettingsClick = {
                    onAction(ChatListAction.OnProfileSettingsClick)
                }
            )
            when {
                state.isLoading -> {
                    CircularProgressIndicator(
                        color = MaterialTheme.colorScheme.primary
                    )
                }
                state.chats.isEmpty() -> {
                    EmptyChatSection(
                        modifier = Modifier
                            .weight(1f)
                            .fillMaxWidth()
                            .padding(horizontal = 8.dp)
                    )
                }
                else -> {
                    LazyColumn(
                        modifier = Modifier
                            .fillMaxWidth()
                            .weight(1f)
                    ) {
                        items(items = state.chats, key = { it.id }) { chatUi ->
                            ChatListItemUi(
                                chat = chatUi,
                                isSelected = chatUi.id == state.selectedChatId,
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .clickable {
                                        onAction(ChatListAction.OnChatClick(chatUi))
                                    }
                            )
                            CourseHorizontalDivider()
                        }
                    }
                }
            }
        }
    }

    if (state.showLogoutConfirmation) {
        DestructiveConfirmationDialog(
            title = stringResource(Res.string.do_you_want_to_log_out),
            description = stringResource(Res.string.do_you_want_to_log_out_desc),
            confirmButtonText = stringResource(Res.string.logout),
            cancelButtonText = stringResource(Res.string.cancel),
            onConfirmClick = {
                onAction(ChatListAction.OnConfirmLogout)
            },
            onCancelClick = {
                onAction(ChatListAction.OnDismissLogoutDialog)
            },
            onDismiss = {
                onAction(ChatListAction.OnDismissLogoutDialog)
            }
        )
    }
}

@Preview
@Composable
private fun Preview() {
    CourseTheme {
        ChatListScreen(
            state = ChatListState(),
            onAction = {},
            snackbarHostState = remember { SnackbarHostState() }
        )
    }
}