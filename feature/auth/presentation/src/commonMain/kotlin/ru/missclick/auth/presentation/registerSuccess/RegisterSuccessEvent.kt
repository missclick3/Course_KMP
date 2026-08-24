package ru.missclick.auth.presentation.registerSuccess

sealed interface RegisterSuccessEvent {
    data object ResendVerificationEmailSuccess: RegisterSuccessEvent
}