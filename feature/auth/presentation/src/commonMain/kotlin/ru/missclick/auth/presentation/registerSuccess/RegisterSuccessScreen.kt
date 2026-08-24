package ru.missclick.auth.presentation.registerSuccess

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import course_kmp.feature.auth.presentation.generated.resources.Res
import course_kmp.feature.auth.presentation.generated.resources.account_successfully_created
import course_kmp.feature.auth.presentation.generated.resources.login
import course_kmp.feature.auth.presentation.generated.resources.resend_verification_email
import course_kmp.feature.auth.presentation.generated.resources.verification_email_send_to_x
import org.jetbrains.compose.resources.stringResource
import org.koin.compose.viewmodel.koinViewModel
import ru.missclick.core.designsystem.components.brand.CourseBrandLogo
import ru.missclick.core.designsystem.components.brand.CourseSuccessIcon
import ru.missclick.core.designsystem.components.buttons.CourseButton
import ru.missclick.core.designsystem.components.buttons.CourseButtonStyle
import ru.missclick.core.designsystem.components.layouts.CourseAdaptiveResultLayout
import ru.missclick.core.designsystem.components.layouts.CourseSimpleSuccessLayout
import ru.missclick.core.designsystem.theme.CourseTheme

@Composable
fun RegisterSuccessRoot(
    viewModel: RegisterSuccessViewModel = koinViewModel()
) {
    val state by viewModel.state.collectAsStateWithLifecycle()

    RegisterSuccessScreen(
        state = state,
        onAction = viewModel::onAction
    )
}

@Composable
fun RegisterSuccessScreen(
    state: RegisterSuccessState,
    onAction: (RegisterSuccessAction) -> Unit,
) {
    CourseAdaptiveResultLayout {
        CourseSimpleSuccessLayout(
            title = stringResource(Res.string.account_successfully_created),
            description = stringResource(Res.string.verification_email_send_to_x, state.registeredEmail),
            icon = {
                CourseSuccessIcon()
            },
            primaryButton = {
                CourseButton(
                    text = stringResource(Res.string.login),
                    onClick = {
                        onAction(RegisterSuccessAction.OnLoginClick)
                    },
                    modifier = Modifier.fillMaxWidth(),
                )
            },
            secondaryButton = {
                CourseButton(
                    text = stringResource(Res.string.resend_verification_email),
                    onClick = {
                        onAction(RegisterSuccessAction.OnResendVerificationEmailClick)
                    },
                    style = CourseButtonStyle.SECONDARY,
                    enabled = !state.isResendingVerificationEmail,
                    isLoading = state.isResendingVerificationEmail,
                    modifier = Modifier.fillMaxWidth(),
                )
            }
        )
    }
}

@Preview
@Composable
private fun Preview() {
    CourseTheme {
        RegisterSuccessScreen(
            state = RegisterSuccessState(registeredEmail = "test@email.com"),
            onAction = {}
        )
    }
}