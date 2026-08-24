package ru.missclick.auth.presentation.registerSuccess

data class RegisterSuccessState(
    val registeredEmail: String = "",
    val isResendingVerificationEmail: Boolean = false
)