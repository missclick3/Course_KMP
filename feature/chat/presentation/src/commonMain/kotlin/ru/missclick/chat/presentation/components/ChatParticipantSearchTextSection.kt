package ru.missclick.chat.presentation.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.input.TextFieldState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import course_kmp.feature.chat.presentation.generated.resources.Res
import course_kmp.feature.chat.presentation.generated.resources.add
import course_kmp.feature.chat.presentation.generated.resources.email_or_username
import org.jetbrains.compose.resources.stringResource
import ru.missclick.core.designsystem.components.buttons.CourseButton
import ru.missclick.core.designsystem.components.buttons.CourseButtonStyle
import ru.missclick.core.designsystem.components.textfields.CourseTextField
import ru.missclick.core.presentation.util.UiText

@Composable
fun ChatParticipantSearchTextSection(
    queryState: TextFieldState,
    onAddClick: () -> Unit,
    isSearchEnabled: Boolean,
    isLoading: Boolean,
    modifier: Modifier = Modifier,
    error: UiText? = null,
    onFocusChanged: (Boolean) -> Unit,
) {
    Row(
        modifier = modifier
            .padding(
                horizontal = 20.dp,
                vertical = 16.dp
            ),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        CourseTextField(
            state = queryState,
            modifier = Modifier.weight(1f),
            placeholder = stringResource(Res.string.email_or_username),
            title = null,
            supportingText = error?.asString(),
            isError = error != null,
            keyboardType = KeyboardType.Email,
            singleLine = true,
            onFocusChanged = onFocusChanged
        )
        CourseButton(
            text = stringResource(Res.string.add),
            onClick = onAddClick,
            style = CourseButtonStyle.SECONDARY,
            enabled = isSearchEnabled,
            isLoading = isLoading
        )
    }
}