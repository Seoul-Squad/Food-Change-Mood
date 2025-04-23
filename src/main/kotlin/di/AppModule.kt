package org.seoulsquad.di

import di.csvModule
import di.repositoryModule
import di.uiModule
import di.useCaseModule
import org.koin.dsl.module

val appModule =
    module {
        includes(uiModule, csvModule, repositoryModule, useCaseModule)
    }
