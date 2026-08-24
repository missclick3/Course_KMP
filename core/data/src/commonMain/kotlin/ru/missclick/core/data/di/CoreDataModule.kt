package ru.missclick.core.data.di

import org.koin.core.module.Module
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.bind
import org.koin.dsl.module
import ru.missclick.core.data.auth.KtorAuthService
import ru.missclick.core.data.logging.KermitLogger
import ru.missclick.core.data.networking.HttpClientFactory
import ru.missclick.core.domain.auth.AuthService
import ru.missclick.core.domain.logging.CourseLogger

expect val platformCoreDataModule: Module

val coreDataModule = module {
    includes(platformCoreDataModule)
    single<CourseLogger> { KermitLogger }
    single {
        HttpClientFactory(get()).create(get())
    }
    singleOf(::KtorAuthService) bind AuthService::class
}