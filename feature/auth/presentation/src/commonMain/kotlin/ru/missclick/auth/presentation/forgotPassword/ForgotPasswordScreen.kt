package ru.missclick.auth.presentation.forgotPassword

import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import course_kmp.feature.auth.presentation.generated.resources.Res
import course_kmp.feature.auth.presentation.generated.resources.email
import course_kmp.feature.auth.presentation.generated.resources.email_placeholder
import course_kmp.feature.auth.presentation.generated.resources.forgot_password
import course_kmp.feature.auth.presentation.generated.resources.submit
import org.jetbrains.compose.resources.stringResource
import ru.missclick.core.designsystem.components.brand.CourseBrandLogo
import ru.missclick.core.designsystem.components.buttons.CourseButton
import ru.missclick.core.designsystem.components.layouts.CourseAdaptiveFormLayout
import ru.missclick.core.designsystem.components.textfields.CourseTextField
import ru.missclick.core.designsystem.theme.CourseTheme

@Composable
fun ForgotPasswordRoot(
    viewModel: ForgotPasswordViewModel = viewModel()
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
            isError = state.emailError != null,
            supportingText = state.emailError?.asString(),
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