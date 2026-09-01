package ru.missclick.chat.domain.error

import ru.missclick.core.domain.util.Error

enum class ConnectionError: Error {
    NOT_CONNECTED,
    MESSAGE_SEND_FAILED
}