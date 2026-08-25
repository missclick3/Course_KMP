package ru.missclick.auth.presentation.login

sealed interface LoginEvent {
    data object Success: LoginEvent
}