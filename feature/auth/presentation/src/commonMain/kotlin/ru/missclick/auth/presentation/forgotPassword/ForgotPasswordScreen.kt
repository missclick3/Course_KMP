package ru.missclick.auth.presentation.forgotPassword

import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import course_kmp.feature.auth.presentation.generated.resources.Res
import course_kmp.feature.auth.presentation.generated.resources.email
import course_kmp.feature.auth.presentation.generated.resources.email_placeholder
import course_kmp.feature.auth.presentation.generated.resources.forgot_password
import course_kmp.feature.auth.presentation.generated.resources.forgot_password_email_sent_successfully
import course_kmp.feature.auth.presentation.generated.resources.submit
import org.jetbrains.compose.resources.stringResource
import org.koin.compose.viewmodel.koinViewModel
import ru.missclick.core.designsystem.components.brand.CourseBrandLogo
import ru.missclick.core.designsystem.components.buttons.CourseButton
import ru.missclick.core.designsystem.components.layouts.CourseAdaptiveFormLayout
import ru.missclick.core.designsystem.components.layouts.CourseSnackbarScaffold
import ru.missclick.core.designsystem.components.textfields.CourseTextField
import ru.missclick.core.designsystem.theme.CourseTheme
import ru.missclick.core.designsystem.theme.extended

@Composable
fun ForgotPasswordRoot(
    viewModel: ForgotPasswordViewModel = koinViewModel()
) {
    val state by viewModel.state.collectAsStateWithLifecycle()

    ForgotPasswordScreen(
        state = state,
        onAction = viewModel::onAction
    )
}

@Composable
fun ForgotPasswordScreen(
    state: ForgotPasswordState,
    onAction: (ForgotPasswordAction) -> Unit,
) {
    CourseSnackbarScaffold {
        CourseAdaptiveFormLayout(
            headerText = stringResource(Res.string.forgot_password),
            errorText = state.errorText?.asString(),
            logo = {
                CourseBrandLogo()
            }
        ) {
            CourseTextField(
                state = state.emailTextFieldState,
                modifier = Modifier.fillMaxWidth(),
                placeholder = stringResource(Res.string.email_placeholder),
                title = stringResource(Res.string.email),
                isError = state.errorText != null,
                supportingText = state.errorText?.asString(),
                keyboardType = KeyboardType.Email,
                singleLine = true
            )
            Spacer(Modifier.height(16.dp))
            CourseButton(
                text = stringResource(Res.string.submit),
                onClick = {
                    onAction(ForgotPasswordAction.OnSubmitClick)
                },
                modifier = Modifier.fillMaxWidth(),
                enabled = !state.isLoading && state.canSubmit,
                isLoading = state.isLoading
            )
            Spacer(Modifier.height(8.dp))
            if (state.isEmailSendSuccessfully) {
                Text(
                    text = stringResource(Res.string.forgot_password_email_sent_successfully),
                    style = MaterialTheme.typography.labelSmall,
                    color = MaterialTheme.colorScheme.extended.success,
                    modifier = Modifier
                        .fillMaxWidth(),
                    textAlign = TextAlign.Center
                )
            }
        }
    }
}

@Preview
@Composable
private fun Preview() {
    CourseTheme {
        ForgotPasswordScreen(
            state = ForgotPasswordState(),
            onAction = {}
        )
    }
}