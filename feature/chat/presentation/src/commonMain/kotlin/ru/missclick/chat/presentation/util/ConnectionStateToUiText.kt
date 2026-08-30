package ru.missclick.chat.presentation.util

import course_kmp.feature.chat.presentation.generated.resources.Res
import course_kmp.feature.chat.presentation.generated.resources.network_error
import course_kmp.feature.chat.presentation.generated.resources.offline
import course_kmp.feature.chat.presentation.generated.resources.online
import course_kmp.feature.chat.presentation.generated.resources.reconnecting
import course_kmp.feature.chat.presentation.generated.resources.unknown_error
import ru.missclick.chat.domain.models.ConnectionState
import ru.missclick.core.presentation.util.UiText

fun ConnectionState.toUiText(): UiText {
    val resource = when (this) {
        ConnectionState.DISCONNECTED -> Res.string.offline
        ConnectionState.CONNECTING -> Res.string.reconnecting
        ConnectionState.CONNECTED -> Res.string.online
        ConnectionState.ERROR_NETWORK -> Res.string.network_error
        ConnectionState.ERROR_UNKNOWN -> Res.string.unknown_error
    }

    return UiText.Resource(resource)
}