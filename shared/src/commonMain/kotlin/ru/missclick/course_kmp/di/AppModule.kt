package ru.missclick.course_kmp.di

import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.module
import ru.missclick.course_kmp.MainViewModel

val appModule = module {
    viewModelOf(::MainViewModel)
}