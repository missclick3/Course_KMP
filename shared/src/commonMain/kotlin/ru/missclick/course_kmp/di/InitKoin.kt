package ru.missclick.course_kmp.di

import org.koin.core.context.startKoin
import org.koin.dsl.KoinAppDeclaration
import ru.missclick.auth.presentation.di.authPresentationModule
import ru.missclick.core.data.di.coreDataModule

fun initKoin(config: KoinAppDeclaration? = null) {
    startKoin {
        config?.invoke(this)
        modules(
            coreDataModule,
            authPresentationModule,
            appModule
        )
    }
}