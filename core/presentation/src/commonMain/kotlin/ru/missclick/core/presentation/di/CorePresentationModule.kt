package ru.missclick.core.presentation.di

import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.module
import ru.missclick.core.presentation.util.ScopedStoreRegistryViewModel

val corePresentationModule = module {
    viewModelOf(::ScopedStoreRegistryViewModel)
}