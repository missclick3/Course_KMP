package ru.missclick.auth.presentation.login

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import course_kmp.feature.auth.presentation.generated.resources.Res
import course_kmp.feature.auth.presentation.generated.resources.create_account
import course_kmp.feature.auth.presentation.generated.resources.email
import course_kmp.feature.auth.presentation.generated.resources.email_placeholder
import course_kmp.feature.auth.presentation.generated.resources.forgot_password
import course_kmp.feature.auth.presentation.generated.resources.login
import course_kmp.feature.auth.presentation.generated.resources.password
import course_kmp.feature.auth.presentation.generated.resources.welcome_back
import org.jetbrains.compose.resources.stringResource
import org.koin.compose.viewmodel.koinViewModel
import ru.missclick.core.designsystem.components.brand.CourseBrandLogo
import ru.missclick.core.designsystem.components.buttons.CourseButton
import ru.missclick.core.designsystem.components.buttons.CourseButtonStyle
import ru.missclick.core.designsystem.components.layouts.CourseAdaptiveFormLayout
import ru.missclick.core.designsystem.components.textfields.CoursePasswordTextField
import ru.missclick.core.designsystem.components.textfields.CourseTextField
import ru.missclick.core.designsystem.theme.CourseTheme
import ru.missclick.core.presentation.util.ObserveAsEvents

@Composable
fun LoginRoot(
    viewModel: LoginViewModel = koinViewModel(),
    onLoginSuccess: () -> Unit,
    onForgotPasswordClick: () -> Unit,
    onCreateAccountClick: () -> Unit
) {
    val state by viewModel.state.collectAsStateWithLifecycle()


    ObserveAsEvents(viewModel.events) { event ->
        when (event) {
            LoginEvent.Success -> onLoginSuccess()
        }
    }

    LoginScreen(
        state = state,
        onAction = { action ->
            when(action) {
                LoginAction.OnForgotPasswordClick -> onForgotPasswordClick()
                LoginAction.OnSignUpClick -> onCreateAccountClick()
                else -> Unit
            }
            viewModel.onAction(action)
        }
    )
}

@Composable
fun LoginScreen(
    state: LoginState,
    onAction: (LoginAction) -> Unit,
) {
    CourseAdaptiveFormLayout(
        headerText = stringResource(Res.string.welcome_back),
        errorText = state.error?.asString(),
        logo = {
            CourseBrandLogo()
        }
    ) {
        CourseTextField(
            state = state.emailTextFieldState,
            placeholder = stringResource(Res.string.email_placeholder),
            title = stringResource(Res.string.email),
            keyboardType = KeyboardType.Email,
            singleLine = true,
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(Modifier.height(16.dp))
        CoursePasswordTextField(
            state = state.passwordTextFieldState,
            placeholder = stringResource(Res.string.password),
            title = stringResource(Res.string.password),
            isPasswordVisible = state.isPasswordVisible,
            onToggleVisibilityClick = {
                onAction(LoginAction.OnTogglePasswordVisibility)
            },
        )
        Spacer(modifier = Modifier.height(8.dp))
        Text(
            text = stringResource(Res.string.forgot_password),
            style = MaterialTheme.typography.titleSmall,
            color = MaterialTheme.colorScheme.tertiary,
            modifier = Modifier
                .align(Alignment.End)
                .clickable {
                    onAction(LoginAction.OnForgotPasswordClick)
                }
        )
        Spacer(Modifier.height(24.dp))

        CourseButton(
            text = stringResource(Res.string.login),
            onClick = {
                onAction(LoginAction.OnLoginClick)
            },
            enabled = state.canLogin,
            isLoading = state.isLogging,
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(Modifier.height(8.dp))
        CourseButton(
            text = stringResource(Res.string.create_account),
            onClick = {
                onAction(LoginAction.OnSignUpClick)
            },
            style = CourseButtonStyle.SECONDARY,
            modifier = Modifier.fillMaxWidth()
        )
    }
}

@Preview
@Composable
private fun Preview() {
    CourseTheme {
        LoginScreen(
            state = LoginState(),
            onAction = {}
        )
    }
}