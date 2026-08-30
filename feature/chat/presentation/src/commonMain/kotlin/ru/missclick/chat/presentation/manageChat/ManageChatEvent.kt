package ru.missclick.chat.presentation.manageChat

sealed interface ManageChatEvent {
    data object OnMembersAdded: ManageChatEvent
}