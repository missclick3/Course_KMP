package ru.missclick.chat.database

import androidx.room.Room
import androidx.room.RoomDatabase
import kotlinx.cinterop.ExperimentalForeignApi
import platform.Foundation.NSDocumentDirectory
import platform.Foundation.NSFileManager
import platform.Foundation.NSUserDomainMask

actual class DatabaseFactory {
    actual fun create(): RoomDatabase.Builder<CourseChatDatabase> {
        val dbFile = documentDirectory() + "/${CourseChatDatabase.DB_NAME}"

        return Room.databaseBuilder(dbFile)
    }

    @OptIn(ExperimentalForeignApi::class)
    private fun documentDirectory(): String {
        val documentDirectory = NSFileManager.defaultManager.URLForDirectory(
            directory = NSDocumentDirectory,
            inDomain = NSUserDomainMask,
            create = false,
            error = null,
            appropriateForURL = null
        )

        return requireNotNull(documentDirectory?.path)
    }
}