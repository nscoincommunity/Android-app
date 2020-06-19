package com.stocksexchange.android.di

import com.stocksexchange.android.utils.ReloadProvider
import com.stocksexchange.android.utils.handlers.*
import com.stocksexchange.api.utils.CredentialsHandler
import com.stocksexchange.core.handlers.ClipboardHandler
import com.stocksexchange.core.handlers.CoroutineHandler
import com.stocksexchange.core.handlers.PreferenceHandler
import com.stocksexchange.core.handlers.QrCodeHandler
import org.koin.android.ext.koin.androidApplication
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module

val handlersModule = module {

    factory { BrowserHandler(get(), get()) }
    factory { ClipboardHandler(androidApplication()) }
    factory { CredentialsHandler(get()) }
    factory { EmailHandler(get()) }
    factory { PreferenceHandler(androidContext()) }
    factory { ReloadProvider(androidContext()) }
    factory { UserDataClearingHandler(androidApplication(), get(), get(), get(), get(), get(), get(), get(), get(), get(), get(), get(), get()) }
    factory { SharingHandler(get()) }
    factory { ShortcutsHandler(androidApplication(), get()) }
    factory { FiatCurrencyPriceHandler(get()) }
    factory { IntercomHandler(get(), get()) }
    factory { QrCodeHandler() }
    factory { CoroutineHandler() }

}