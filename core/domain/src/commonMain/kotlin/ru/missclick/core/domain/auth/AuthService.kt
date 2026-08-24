package ru.missclick.core.domain.auth

import ru.missclick.core.domain.util.DataError
import ru.missclick.core.domain.util.EmptyResult

interface AuthService {
    suspend fun register(
        email: String,
        username: String,
        password: String
    ): EmptyResult<DataError.Remote>
}