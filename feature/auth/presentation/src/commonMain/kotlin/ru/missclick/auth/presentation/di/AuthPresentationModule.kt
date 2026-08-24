package ru.missclick.auth.presentation.di

import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.module
import ru.missclick.auth.presentation.emailVerification.EmailVerificationViewModel
import ru.missclick.auth.presentation.register.RegisterViewModel
import ru.missclick.auth.presentation.registerSuccess.RegisterSuccessViewModel

val authPresentationModule = module {
    viewModelOf(::RegisterViewModel)
    viewModelOf(::RegisterSuccessViewModel)
    viewModelOf(::EmailVerificationViewModel)
}