package ru.missclick.course_kmp.di

import org.koin.core.context.startKoin
import org.koin.dsl.KoinAppDeclaration
import ru.missclick.auth.presentation.di.authPresentationModule
import ru.missclick.chat.data.di.chatDataModule
import ru.missclick.chat.presentation.di.chatPresentationModule
import ru.missclick.core.data.di.coreDataModule
import ru.missclick.core.presentation.di.corePresentationModule

fun initKoin(config: KoinAppDeclaration? = null) {
    startKoin {
        config?.invoke(this)
        modules(
            coreDataModule,
            authPresentationModule,
            appModule,
            chatPresentationModule,
            corePresentationModule,
            chatDataModule
        )
    }
}