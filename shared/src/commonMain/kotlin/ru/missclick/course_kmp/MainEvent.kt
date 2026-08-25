package ru.missclick.course_kmp

sealed interface MainEvent {
    data object OnSessionExpired: MainEvent
}