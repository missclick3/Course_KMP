package ru.missclick.auth.presentation.resetPassword

import androidx.compose.foundation.text.input.TextFieldState
import ru.missclick.core.presentation.util.UiText

data class ResetPasswordState(
    val passwordTextFieldState: TextFieldState = TextFieldState(),
    val isLoading: Boolean = false,
    val errorText: UiText? = null,
    val isPasswordVisible: Boolean = false,
    val canSubmit: Boolean = false,
    val isResetSuccessful: Boolean = false
)