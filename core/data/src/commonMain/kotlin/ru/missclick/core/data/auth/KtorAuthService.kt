package ru.missclick.core.data.auth

import io.ktor.client.HttpClient
import ru.missclick.core.data.dto.requests.RegisterRequest
import ru.missclick.core.data.networking.post
import ru.missclick.core.domain.auth.AuthService
import ru.missclick.core.domain.util.DataError
import ru.missclick.core.domain.util.EmptyResult

class KtorAuthService(
    private val httpClient: HttpClient
): AuthService {
    override suspend fun register(
        email: String,
        username: String,
        password: String
    ): EmptyResult<DataError.Remote> {
        return httpClient.post(
            route = "/auth/register",
            body = RegisterRequest(
                email = email,
                username = username,
                password = password
            )
        )
    }
}