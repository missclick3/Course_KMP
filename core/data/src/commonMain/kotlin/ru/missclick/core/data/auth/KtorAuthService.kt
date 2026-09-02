package ru.missclick.core.data.auth

import io.ktor.client.HttpClient
import ru.missclick.core.data.dto.AuthInfoSerializable
import ru.missclick.core.data.dto.requests.ChangePasswordRequest
import ru.missclick.core.data.dto.requests.RegisterRequest
import ru.missclick.core.data.dto.requests.EmailRequest
import ru.missclick.core.data.dto.requests.LoginRequest
import ru.missclick.core.data.dto.requests.ResetPasswordRequest
import ru.missclick.core.data.mappers.toDomain
import ru.missclick.core.data.networking.get
import ru.missclick.core.data.networking.post
import ru.missclick.core.domain.auth.AuthInfo
import ru.missclick.core.domain.auth.AuthService
import ru.missclick.core.domain.util.DataError
import ru.missclick.core.domain.util.EmptyResult
import ru.missclick.core.domain.util.Result
import ru.missclick.core.domain.util.map

class KtorAuthService(
    private val httpClient: HttpClient
): AuthService {
    override suspend fun login(
        email: String,
        password: String
    ): Result<AuthInfo, DataError.Remote> {
        return httpClient.post<LoginRequest, AuthInfoSerializable>(
            route = "/auth/login",
            body = LoginRequest(
                email = email,
                password = password
            )
        ).map { authInfoSerializable ->
            authInfoSerializable.toDomain()
        }
    }

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

    override suspend fun resendVerificationEmail(email: String): EmptyResult<DataError.Remote> {
        return httpClient.post(
            route = "/auth/resend-verification",
            body = EmailRequest(
                email = email
            )
        )
    }

    override suspend fun verifyEmail(token: String): EmptyResult<DataError.Remote> {
        return httpClient.get(
            route = "/auth/verify",
            queryParams = mapOf("token" to token)
        )
    }

    override suspend fun forgotPassword(email: String): EmptyResult<DataError.Remote> {
        return httpClient.post<EmailRequest, Unit>(
            route = "/auth/forgot-password",
            body = EmailRequest(email)
        )
    }

    override suspend fun resetPassword(
        newPassword: String,
        token: String
    ): EmptyResult<DataError.Remote> {
        return httpClient.post(
            route = "/aith/reset-password",
            body = ResetPasswordRequest(
                newPassword = newPassword,
                token = token
            )
        )
    }

    override suspend fun changePassword(
        currentPassword: String,
        newPassword: String
    ): EmptyResult<DataError.Remote> {
        return httpClient.post(
            route = "/auth/change-password",
            body = ChangePasswordRequest(
                oldPassword = currentPassword,
                newPassword = newPassword
            )
        )
    }
}