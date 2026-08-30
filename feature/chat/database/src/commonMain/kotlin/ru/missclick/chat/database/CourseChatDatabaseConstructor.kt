package ru.missclick.chat.database

import androidx.room.RoomDatabaseConstructor

expect object CourseChatDatabaseConstructor: RoomDatabaseConstructor<CourseChatDatabase> {
    override fun initialize(): CourseChatDatabase
}