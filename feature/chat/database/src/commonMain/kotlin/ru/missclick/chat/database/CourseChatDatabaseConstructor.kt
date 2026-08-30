package ru.missclick.chat.database

import androidx.room.RoomDatabaseConstructor

@Suppress("NO_ACTUAL_FOR_EXPECT")
expect object CourseChatDatabaseConstructor: RoomDatabaseConstructor<CourseChatDatabase> {
    override fun initialize(): CourseChatDatabase
}