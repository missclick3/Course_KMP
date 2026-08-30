package ru.missclick.chat.presentation.manageChat

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import course_kmp.feature.chat.presentation.generated.resources.Res
import course_kmp.feature.chat.presentation.generated.resources.chat_members
import course_kmp.feature.chat.presentation.generated.resources.save
import org.jetbrains.compose.resources.stringResource
import org.koin.compose.viewmodel.koinViewModel
import ru.missclick.chat.presentation.components.manageChat.ManageChatAction
import ru.missclick.chat.presentation.components.manageChat.ManageChatScreen
import ru.missclick.core.designsystem.components.dialog.CourseAdaptiveDialogSheetLayout
import ru.missclick.core.presentation.util.ObserveAsEvents

@Composable
fun ManageChatRoot(
    onDismiss: () -> Unit,
    onMembersAdded: () -> Unit,
    viewModel: ManageChatViewModel = koinViewModel()
) {
    val state by viewModel.state.collectAsStateWithLifecycle()

    ObserveAsEvents(viewModel.events) { event ->
        when (event) {
            ManageChatEvent.OnMembersAdded -> onMembersAdded()
        }
    }

    CourseAdaptiveDialogSheetLayout(
        onDismiss = onDismiss
    ) {
        ManageChatScreen(
            headerText = stringResource(Res.string.chat_members),
            primaryButtonText = stringResource(Res.string.save),
            state = state,
            onAction = { action ->
                when (action) {
                    ManageChatAction.OnDismissDialog -> onDismiss()
                    else -> Unit
                }
                viewModel.onAction(action)
            }
        )
    }
}