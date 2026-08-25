package ru.missclick.auth.presentation.forgotPassword

import androidx.compose.foundation.text.input.TextFieldState
import ru.missclick.core.presentation.util.UiText

data class ForgotPasswordState(
    val emailTextFieldState: TextFieldState = TextFieldState(),
    val isLoading: Boolean = false,
    val errorText: UiText? = null,
    val isEmailSendSuccessfully: Boolean = false,
    val emailError: UiText? = null,
    val canSubmit: Boolean = false
)