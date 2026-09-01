package ru.missclick.course_kmp.di

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.module
import ru.missclick.course_kmp.MainViewModel

val appModule = module {
    viewModelOf(::MainViewModel)
    single {
        CoroutineScope(SupervisorJob() + Dispatchers.Default)
    }
}