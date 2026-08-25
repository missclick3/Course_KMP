package ru.missclick.auth.presentation.register

import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import course_kmp.feature.auth.presentation.generated.resources.Res
import course_kmp.feature.auth.presentation.generated.resources.email
import course_kmp.feature.auth.presentation.generated.resources.email_placeholder
import course_kmp.feature.auth.presentation.generated.resources.login
import course_kmp.feature.auth.presentation.generated.resources.password
import course_kmp.feature.auth.presentation.generated.resources.password_hint
import course_kmp.feature.auth.presentation.generated.resources.register
import course_kmp.feature.auth.presentation.generated.resources.username
import course_kmp.feature.auth.presentation.generated.resources.username_hint
import course_kmp.feature.auth.presentation.generated.resources.username_placeholder
import course_kmp.feature.auth.presentation.generated.resources.welcome_to_chirp
import org.jetbrains.compose.resources.stringResource
import org.koin.compose.viewmodel.koinViewModel
import ru.missclick.core.designsystem.components.brand.CourseBrandLogo
import ru.missclick.core.designsystem.components.buttons.CourseButton
import ru.missclick.core.designsystem.components.buttons.CourseButtonStyle
import ru.missclick.core.designsystem.components.layouts.CourseAdaptiveFormLayout
import ru.missclick.core.designsystem.components.layouts.CourseSnackbarScaffold
import ru.missclick.core.designsystem.components.textfields.CoursePasswordTextField
import ru.missclick.core.designsystem.components.textfields.CourseTextField
import ru.missclick.core.designsystem.theme.CourseTheme
import ru.missclick.core.presentation.util.ObserveAsEvents

@Composable
fun RegisterRoot(
    viewModel: RegisterViewModel = koinViewModel(),
    onRegisterSuccess: (String) -> Unit,
    onLoginClick: () -> Unit,
) {
    val state by viewModel.state.collectAsStateWithLifecycle()

    val snackbarHostState = remember { SnackbarHostState() }

    ObserveAsEvents(viewModel.events) { event ->
        when (event) {
            is RegisterEvent.Success -> {
                onRegisterSuccess(event.email)
            }
        }
    }

    RegisterScreen(
        state = state,
        onAction = { action ->
            when (action) {
                is RegisterAction.OnLoginClick -> onLoginClick()
                else -> Unit
            }
            viewModel.onAction(action)
        },
        snackbarHostState = snackbarHostState
    )
}

@Composable
fun RegisterScreen(
    state: RegisterState,
    onAction: (RegisterAction) -> Unit,
    snackbarHostState: SnackbarHostState
) {
    CourseSnackbarScaffold(
        snackbarHostState = snackbarHostState
    ) {
        CourseAdaptiveFormLayout(
            headerText = stringResource(Res.string.welcome_to_chirp),
            errorText = state.registrationError?.asString(),
            logo = { CourseBrandLogo() }
        ) {
            CourseTextField(
                state = state.usernameTextState,
                placeholder = stringResource(Res.string.username_placeholder),
                title = stringResource(Res.string.username),
                supportingText = state.usernameError?.asString()
                    ?: stringResource(Res.string.username_hint),
                isError = state.usernameError != null,
                onFocusChanged = { isFocused->
                    if (isFocused) {
                        onAction(RegisterAction.OnInputTextFocusGain)
                    }
                }
            )
            Spacer(Modifier.height(16.dp))
            CourseTextField(
                state = state.emailTextState,
                placeholder = stringResource(Res.string.email_placeholder),
                title = stringResource(Res.string.email),
                supportingText = state.emailError?.asString(),
                isError = state.emailError != null,
                keyboardType = KeyboardType.Email,
                onFocusChanged = { isFocused->
                    if (isFocused) {
                        onAction(RegisterAction.OnInputTextFocusGain)
                    }
                }
            )
            Spacer(Modifier.height(16.dp))
            CoursePasswordTextField(
                state = state.passwordTextState,
                placeholder = stringResource(Res.string.password),
                title = stringResource(Res.string.password),
                supportingText = state.passwordError?.asString()
                    ?: stringResource(Res.string.password_hint),
                isError = state.passwordError != null,
                onFocusChanged = { isFocused->
                    if (isFocused) {
                        onAction(RegisterAction.OnInputTextFocusGain)
                    }
                },
                onToggleVisibilityClick = {
                    onAction(RegisterAction.OnTogglePasswordVisibility)
                },
                isPasswordVisible = state.isPasswordVisible
            )
            Spacer(Modifier.height(16.dp))

            CourseButton(
                text = stringResource(Res.string.register),
                onClick = {
                    onAction(RegisterAction.OnRegisterClick)
                },
                enabled = state.canRegister,
                isLoading = state.isRegistering,
                modifier = Modifier.fillMaxWidth()
            )
            Spacer(Modifier.height(8.dp))
            CourseButton(
                text = stringResource(Res.string.login),
                onClick = {
                    onAction(RegisterAction.OnLoginClick)
                },
                style = CourseButtonStyle.SECONDARY,
                modifier = Modifier.fillMaxWidth()
            )
        }
    }
}

@Preview
@Composable
private fun Preview() {
    CourseTheme {
        RegisterScreen(
            state = RegisterState(),
            onAction = {},
            snackbarHostState = remember { SnackbarHostState() }
        )
    }
}