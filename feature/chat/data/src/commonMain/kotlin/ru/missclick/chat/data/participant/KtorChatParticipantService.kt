package ru.missclick.chat.data.participant

import io.ktor.client.HttpClient
import io.ktor.client.request.header
import io.ktor.client.request.put
import io.ktor.client.request.setBody
import io.ktor.client.request.url
import ru.missclick.chat.data.dto.ChatParticipantDto
import ru.missclick.chat.data.dto.request.ConfirmProfilePictureRequest
import ru.missclick.chat.data.dto.response.ProfilePictureUploadUrlsResponse
import ru.missclick.chat.data.mappers.toDomain
import ru.missclick.chat.domain.models.ChatParticipant
import ru.missclick.chat.domain.models.ProfilePictureUploadUrls
import ru.missclick.chat.domain.participant.ChatParticipantService
import ru.missclick.core.data.networking.delete
import ru.missclick.core.data.networking.get
import ru.missclick.core.data.networking.post
import ru.missclick.core.data.networking.safeCall
import ru.missclick.core.domain.util.DataError
import ru.missclick.core.domain.util.EmptyResult
import ru.missclick.core.domain.util.Result
import ru.missclick.core.domain.util.map

class KtorChatParticipantService(
    private val httpClient: HttpClient
): ChatParticipantService {
    override suspend fun searchParticipant(query: String): Result<ChatParticipant, DataError.Remote> {
        return httpClient.get<ChatParticipantDto>(
            route = "/participants",
            queryParams = mapOf(
                "query" to query
            )
        ).map { it.toDomain() }
    }

    override suspend fun getLocalParticipant(): Result<ChatParticipant, DataError.Remote> {
        return httpClient.get<ChatParticipantDto>(
            route = "/participants",
        ).map { it.toDomain() }
    }

    override suspend fun getProfilePictureUploadUrl(mimeType: String): Result<ProfilePictureUploadUrls, DataError.Remote> {
        return httpClient.post<Unit, ProfilePictureUploadUrlsResponse>(
            route = "/participants/profile-picture-upload",
            queryParams = mapOf(
                "mimeType" to mimeType
            ),
            body = Unit,
        ).map { response ->
            ProfilePictureUploadUrls(
                uploadUrl = response.uploadUrl,
                publicUrl = response.publicUrl,
                headers = response.headers
            )
        }
    }

    override suspend fun uploadProfilePicture(
        uploadUrl: String,
        imageBytes: ByteArray,
        headers: Map<String, String>
    ): EmptyResult<DataError.Remote> {
        return safeCall {
            httpClient.put {
                url(uploadUrl)
                headers.forEach { (key, value) ->
                    header(key, value)
                }
                setBody(imageBytes)
            }
        }
    }

    override suspend fun confirmProfilePictureUpload(publicUrl: String): EmptyResult<DataError.Remote> {
        return httpClient.post<ConfirmProfilePictureRequest, Unit>(
            route = "/participants/confirm-profile-picture",
            body = ConfirmProfilePictureRequest(
                publicUrl = publicUrl
            )
        )
    }

    override suspend fun deleteProfilePicture(): EmptyResult<DataError.Remote> {
        return httpClient.delete(
            route = "/participants/profile-picture"
        )
    }
}