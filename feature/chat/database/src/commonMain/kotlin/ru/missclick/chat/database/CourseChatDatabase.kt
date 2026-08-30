package ru.missclick.chat.database

import androidx.room.Database
import androidx.room.RoomDatabase
import ru.missclick.chat.database.dao.ChatDao
import ru.missclick.chat.database.dao.ChatMessageDao
import ru.missclick.chat.database.dao.ChatParticipantDao
import ru.missclick.chat.database.dao.ChatParticipantsCrossRefDao
import ru.missclick.chat.database.entities.ChatEntity
import ru.missclick.chat.database.entities.ChatMessageEntity
import ru.missclick.chat.database.entities.ChatParticipantCrossRef
import ru.missclick.chat.database.entities.ChatParticipantEntity
import ru.missclick.chat.database.view.LastMessageView

@Database(
    entities = [
        ChatEntity::class,
        ChatParticipantEntity::class,
        ChatMessageEntity::class,
        ChatParticipantCrossRef::class
    ],
    views = [
        LastMessageView::class
    ],
    version = 1
)
abstract class CourseChatDatabase: RoomDatabase() {
    abstract val chatDao: ChatDao
    abstract val chatParticipantDao: ChatParticipantDao
    abstract val chatMessageDao: ChatMessageDao
    abstract val chatParticipantsCrossRefDao: ChatParticipantsCrossRefDao

    companion object {
        const val DB_NAME = "course.db"
    }
}