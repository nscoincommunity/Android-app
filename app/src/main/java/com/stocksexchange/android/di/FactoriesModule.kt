package com.stocksexchange.android.di

import com.stocksexchange.android.factories.SettingsFactory
import com.stocksexchange.android.theming.factories.*
import org.koin.dsl.module

val factoriesModule = module {

    factory { SettingsFactory(get(), get()) }

    // Theming
    single {
        ThemeFactory(
            generalThemeFactory = get(),
            buttonThemeFactory = get(),
            cardViewThemeFactory = get(),
            switchOptionsViewThemeFactory = get(),
            priceChartViewThemeFactory = get(),
            depthChartViewThemeFactory = get(),
            orderbookViewThemeFactory = get(),
            tradeHistoryViewThemeFactory = get(),
            dialogThemeFactory = get(),
            dottedMapViewThemeFactory = get(),
            sortPanelThemeFactory = get(),
            switchThemeFactory = get(),
            popupMenuThemeFactory = get(),
            pinEntryKeypadThemeFactory = get(),
            aboutThemeFactory = get(),
            colorProvider = get()
        )
    }

    factory { GeneralThemeFactory(get()) }
    factory { ButtonThemeFactory(get()) }
    factory { CardViewThemeFactory(get()) }
    factory { SwitchOptionsViewThemeFactory(get()) }
    factory { PriceChartViewThemeFactory(get()) }
    factory { DepthChartViewThemeFactory(get()) }
    factory { OrderbookViewThemeFactory(get()) }
    factory { TradeHistoryViewThemeFactory(get()) }
    factory { DialogThemeFactory(get()) }
    factory { DottedMapViewThemeFactory(get()) }
    factory { SortPanelThemeFactory(get()) }
    factory { SwitchThemeFactory(get()) }
    factory { PopupMenuThemeFactory(get()) }
    factory { PinEntryKeypadThemeFactory(get()) }
    factory { AboutThemeFactory(get()) }

}