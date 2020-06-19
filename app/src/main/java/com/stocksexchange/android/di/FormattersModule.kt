package com.stocksexchange.android.di

import com.stocksexchange.core.formatters.NumberFormatter
import com.stocksexchange.core.formatters.TimeFormatter
import org.koin.dsl.module

val formattersModule = module {

    single { NumberFormatter(get()) }
    single { TimeFormatter(get()) }

}