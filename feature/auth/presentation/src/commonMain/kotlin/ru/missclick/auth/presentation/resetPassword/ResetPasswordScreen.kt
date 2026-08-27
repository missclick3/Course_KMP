package ru.missclick.auth.presentation.resetPassword

import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import course_kmp.feature.auth.presentation.generated.resources.Res
import course_kmp.feature.auth.presentation.generated.resources.new_password
import course_kmp.feature.auth.presentation.generated.resources.password
import course_kmp.feature.auth.presentation.generated.resources.password_hint
import course_kmp.feature.auth.presentation.generated.resources.reset_password_successfully
import course_kmp.feature.auth.presentation.generated.resources.set_new_password
import course_kmp.feature.auth.presentation.generated.resources.submit
import org.jetbrains.compose.resources.stringResource
import org.koin.compose.viewmodel.koinViewModel
import ru.missclick.core.designsystem.components.brand.CourseBrandLogo
import ru.missclick.core.designsystem.components.buttons.CourseButton
import ru.missclick.core.designsystem.components.layouts.CourseAdaptiveFormLayout
import ru.missclick.core.designsystem.components.layouts.CourseSnackbarScaffold
import ru.missclick.core.designsystem.components.textfields.CoursePasswordTextField
import ru.missclick.core.designsystem.theme.CourseTheme
import ru.missclick.core.designsystem.theme.extended

@Composable
fun ResetPasswordRoot(
    viewModel: ResetPasswordViewModel = koinViewModel()
) {
    val state by viewModel.state.collectAsStateWithLifecycle()

    ResetPasswordScreen(
        state = state,
        onAction = viewModel::onAction
    )
}

@Composable
fun ResetPasswordScreen(
    state: ResetPasswordState,
    onAction: (ResetPasswordAction) -> Unit,
) {
    CourseSnackbarScaffold {
        CourseAdaptiveFormLayout(
            headerText = stringResource(Res.string.set_new_password),
            errorText = state.errorText?.asString(),
            logo = { CourseBrandLogo() }
        ) {
            CoursePasswordTextField(
                state = state.passwordTextFieldState,
                isPasswordVisible = state.isPasswordVisible,
                onToggleVisibilityClick = {
                    onAction(ResetPasswordAction.OnTogglePasswordVisibilityClick)
                },
                modifier = Modifier.fillMaxWidth(),
                placeholder = stringResource(Res.string.password),
                title = stringResource(Res.string.new_password),
                supportingText = stringResource(Res.string.password_hint),
            )
            Spacer(Modifier.height(16.dp))
            CourseButton(
                text = stringResource(Res.string.submit),
                onClick = {
                    onAction(ResetPasswordAction.OnSubmitClick)
                },
                modifier = Modifier.fillMaxWidth(),
                enabled = !state.isLoading && state.canSubmit,
                isLoading = state.isLoading
            )
            if (state.isResetSuccessful) {
                Spacer(Modifier.height(8.dp))
                Text(
                    text = stringResource(Res.string.reset_password_successfully),
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
        ResetPasswordScreen(
            state = ResetPasswordState(),
            onAction = {}
        )
    }
}
