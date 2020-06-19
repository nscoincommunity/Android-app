package com.stocksexchange.android.di

import com.stocksexchange.android.utils.managers.AppLockManager
import com.stocksexchange.android.utils.managers.RealTimeDataManager
import com.stocksexchange.android.utils.managers.SessionManager
import com.stocksexchange.core.managers.KeyboardManager
import org.koin.android.ext.koin.androidApplication
import org.koin.dsl.module

val managersModule = module {

    single(createdAtStart = true) { SessionManager() }
    single(createdAtStart = true) { RealTimeDataManager(androidApplication()) }
    single(createdAtStart = true) { AppLockManager(get(), androidApplication(), get(), get()) }

    factory { KeyboardManager(androidApplication()) }

}