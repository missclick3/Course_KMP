package ru.missclick.chat.data.di

import org.koin.dsl.module
import ru.missclick.chat.database.DatabaseFactory

actual val platformChatDataModule = module {
    single { DatabaseFactory() }
}