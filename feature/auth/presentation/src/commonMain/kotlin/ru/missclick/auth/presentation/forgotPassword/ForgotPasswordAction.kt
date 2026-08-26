package ru.missclick.auth.presentation.forgotPassword

sealed interface ForgotPasswordAction {
    data object OnSubmitClick: ForgotPasswordAction
}