package ru.missclick.chat.data.notification

import kotlinx.coroutines.flow.Flow
import ru.missclick.chat.domain.notification.PushNotificationService

expect class FirebasePushNotificationService: PushNotificationService {
    override fun observeDeviceToken(): Flow<String?>
}