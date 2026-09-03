package ru.missclick.chat.data.notification

import io.ktor.client.HttpClient
import ru.missclick.chat.domain.notification.DeviceTokenService
import ru.missclick.chat.data.dto.request.RegisterDeviceTokenRequest
import ru.missclick.core.data.networking.delete
import ru.missclick.core.data.networking.post
import ru.missclick.core.domain.util.DataError
import ru.missclick.core.domain.util.EmptyResult

class KtorDeviceTokenService(
    private val httpClient: HttpClient
): DeviceTokenService {

    override suspend fun registerToken(
        token: String,
        platform: String
    ): EmptyResult<DataError.Remote> {
        return httpClient.post(
            route = "/notification/register",
            body = RegisterDeviceTokenRequest(
                token = token,
                platform = platform
            )
        )
    }

    override suspend fun unregisterToken(token: String): EmptyResult<DataError.Remote> {
        return httpClient.delete(
            route = "/notification/$token"
        )
    }
}