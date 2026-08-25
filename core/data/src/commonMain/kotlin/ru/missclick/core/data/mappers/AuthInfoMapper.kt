package ru.missclick.core.data.mappers

import ru.missclick.core.data.dto.AuthInfoSerializable
import ru.missclick.core.data.dto.UserSerializable
import ru.missclick.core.domain.auth.AuthInfo
import ru.missclick.core.domain.auth.User

fun AuthInfoSerializable.toDomain() = AuthInfo(
    accessToken = accessToken,
    refreshToken = refreshToken,
    user = user.toDomain()
)

fun UserSerializable.toDomain() = User(
    id = id,
    email = email,
    username = username,
    hasVerifiedEmail = hasVerifiedEmail,
    profilePictureUrl = profilePictureUrl
)

fun AuthInfo.toSerializable() = AuthInfoSerializable(
    accessToken = accessToken,
    refreshToken = refreshToken,
    user = user.toSerializable()
)

fun User.toSerializable() = UserSerializable(
    id = id,
    email = email,
    username = username,
    hasVerifiedEmail = hasVerifiedEmail,
    profilePictureUrl = profilePictureUrl
)