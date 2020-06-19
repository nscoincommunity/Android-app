package com.stocksexchange.android.di

import com.stocksexchange.android.utils.providers.*
import com.stocksexchange.core.providers.ColorProvider
import com.stocksexchange.core.providers.ConnectionProvider
import com.stocksexchange.core.providers.DimensionProvider
import com.stocksexchange.core.providers.FingerprintProvider
import org.koin.android.ext.koin.androidApplication
import org.koin.dsl.module

val providersModule = module {

    single { StringProvider(androidApplication()) }
    single { ConnectionProvider(androidApplication()) }
    single { InfoViewIconProvider(androidApplication(), get()) }

    factory { ColorProvider(androidApplication()) }
    factory { CustomTabsProvider(androidApplication()) }
    factory { FingerprintProvider(androidApplication()) }
    factory { DimensionProvider(androidApplication()) }
    factory { InitialLanguageProvider(androidApplication()) }

}