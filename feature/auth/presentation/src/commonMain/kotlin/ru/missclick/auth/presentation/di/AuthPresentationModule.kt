package ru.missclick.auth.presentation.di

import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.module
import ru.missclick.auth.presentation.register.RegisterViewModel

val authPresentationModule = module {
    viewModelOf(::RegisterViewModel)
}