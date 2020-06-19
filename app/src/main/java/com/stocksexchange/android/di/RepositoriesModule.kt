package com.stocksexchange.android.di

import com.stocksexchange.android.data.repositories.alertprice.AlertPriceRepository
import com.stocksexchange.android.data.repositories.alertprice.AlertPriceRepositoryImpl
import com.stocksexchange.android.data.repositories.newsrepository.NewsRepository
import com.stocksexchange.android.data.repositories.newsrepository.NewsRepositoryImpl
import com.stocksexchange.android.data.repositories.candlesticks.CandleSticksRepository
import com.stocksexchange.android.data.repositories.candlesticks.CandleSticksRepositoryImpl
import com.stocksexchange.android.data.repositories.currencies.CurrenciesRepository
import com.stocksexchange.android.data.repositories.currencies.CurrenciesRepositoryImpl
import com.stocksexchange.android.data.repositories.currencymarkets.CurrencyMarketsRepository
import com.stocksexchange.android.data.repositories.currencymarkets.CurrencyMarketsRepositoryImpl
import com.stocksexchange.android.data.repositories.currencypairgroups.CurrencyPairGroupsRepository
import com.stocksexchange.android.data.repositories.currencypairgroups.CurrencyPairGroupsRepositoryImpl
import com.stocksexchange.android.data.repositories.currencypairs.CurrencyPairsRepository
import com.stocksexchange.android.data.repositories.currencypairs.CurrencyPairsRepositoryImpl
import com.stocksexchange.android.data.repositories.deposits.DepositsRepository
import com.stocksexchange.android.data.repositories.deposits.DepositsRepositoryImpl
import com.stocksexchange.android.data.repositories.favoritecurrencypairs.FavoriteCurrencyPairsRepository
import com.stocksexchange.android.data.repositories.favoritecurrencypairs.FavoriteCurrencyPairsRepositoryImpl
import com.stocksexchange.android.data.repositories.inbox.InboxRepository
import com.stocksexchange.android.data.repositories.inbox.InboxRepositoryImpl
import com.stocksexchange.android.data.repositories.notification.NotificationRepository
import com.stocksexchange.android.data.repositories.notification.NotificationRepositoryImpl
import com.stocksexchange.android.data.repositories.orderbooks.OrderbooksRepository
import com.stocksexchange.android.data.repositories.orderbooks.OrderbooksRepositoryImpl
import com.stocksexchange.android.data.repositories.orders.OrdersRepository
import com.stocksexchange.android.data.repositories.orders.OrdersRepositoryImpl
import com.stocksexchange.android.data.repositories.profileinfos.ProfileInfosRepository
import com.stocksexchange.android.data.repositories.profileinfos.ProfileInfosRepositoryImpl
import com.stocksexchange.android.data.repositories.referrals.ReferralsRepository
import com.stocksexchange.android.data.repositories.referrals.ReferralsRepositoryImpl
import com.stocksexchange.android.data.repositories.tickeritems.TickerItemsRepository
import com.stocksexchange.android.data.repositories.tickeritems.TickerItemsRepositoryImpl
import com.stocksexchange.android.data.repositories.tradehistory.TradeHistoryRepository
import com.stocksexchange.android.data.repositories.tradehistory.TradeHistoryRepositoryImpl
import com.stocksexchange.android.data.repositories.useradmission.UserAdmissionRepository
import com.stocksexchange.android.data.repositories.useradmission.UserAdmissionRepositoryImpl
import com.stocksexchange.android.data.repositories.wallets.WalletsRepository
import com.stocksexchange.android.data.repositories.wallets.WalletsRepositoryImpl
import com.stocksexchange.android.data.repositories.withdrawals.WithdrawalsRepository
import com.stocksexchange.android.data.repositories.withdrawals.WithdrawalsRepositoryImpl
import com.stocksexchange.android.data.repositories.settings.SettingsRepository
import com.stocksexchange.android.data.repositories.settings.SettingsRepositoryImpl
import com.stocksexchange.android.data.repositories.tradingfees.TradingFeesRepository
import com.stocksexchange.android.data.repositories.tradingfees.TradingFeesRepositoryImpl
import com.stocksexchange.android.data.repositories.utilities.UtilitiesRepository
import com.stocksexchange.android.data.repositories.utilities.UtilitiesRepositoryImpl
import com.stocksexchange.android.di.utils.get
import org.koin.dsl.module

@Suppress("USELESS_CAST")
val repositoriesModule = module {

    single<SettingsRepository> {
        SettingsRepositoryImpl(
            databaseDataStore = get(SETTINGS_DATABASE_DATA_STORE),
            cacheDataStore = get(SETTINGS_CACHE_DATA_STORE)
        )
    }

    single<CandleSticksRepository> {
        CandleSticksRepositoryImpl(
            serverDataStore = get(CANDLE_STICKS_SERVER_DATA_STORE),
            databaseDataStore = get(CANDLE_STICKS_DATABASE_DATA_STORE),
            freshDataHandler = get(),
            connectionProvider = get()
        )
    }

    single<CurrenciesRepository> {
        CurrenciesRepositoryImpl(
            serverDataStore = get(CURRENCIES_SERVER_DATA_STORE),
            databaseDataStore = get(CURRENCIES_DATABASE_DATA_STORE),
            freshDataHandler = get(),
            connectionProvider = get()
        )
    }

    single<CurrencyPairsRepository> {
        CurrencyPairsRepositoryImpl(
            serverDataStore = get(CURRENCY_PAIRS_SERVER_DATA_STORE),
            databaseDataStore = get(CURRENCY_PAIRS_DATABASE_DATA_STORE),
            freshDataHandler = get(),
            connectionProvider = get()
        )
    }

    single<CurrencyPairGroupsRepository> {
        CurrencyPairGroupsRepositoryImpl(
            serverDataStore = get(CURRENCY_PAIR_GROUPS_SERVER_DATA_STORE),
            databaseDataStore = get(CURRENCY_PAIR_GROUPS_DATABASE_DATA_STORE),
            freshDataHandler = get(),
            connectionProvider = get()
        )
    }

    single<DepositsRepository> {
        DepositsRepositoryImpl(
            serverDataStore = get(DEPOSITS_SERVER_DATA_STORE),
            databaseDataStore = get(DEPOSITS_DATABASE_DATA_STORE),
            freshDataHandler = get(),
            connectionProvider = get()
        )
    }

    single<OrderbooksRepository> {
        OrderbooksRepositoryImpl(
            serverDataStore = get(ORDERBOOKS_SERVER_DATA_STORE),
            databaseDataStore = get(ORDERBOOKS_DATABASE_DATA_STORE),
            freshDataHandler = get(),
            connectionProvider = get()
        )
    }

    single<TradingFeesRepository> {
        TradingFeesRepositoryImpl(
            serverDataStore = get(TRADING_FEES_SERVER_DATA_STORE),
            databaseDataStore = get(TRADING_FEES_DATABASE_DATA_STORE),
            freshDataHandler = get(),
            connectionProvider = get()
        )
    }

    single<OrdersRepository> {
        OrdersRepositoryImpl(
            serverDataStore = get(ORDERS_SERVER_DATA_STORE),
            databaseDataStore = get(ORDERS_DATABASE_DATA_STORE),
            currencyPairsRepository = get(),
            freshDataHandler = get(),
            connectionProvider = get()
        )
    }

    single<ProfileInfosRepository> {
        ProfileInfosRepositoryImpl(
            serverDataStore = get(PROFILE_INFOS_SERVER_DATA_STORE),
            databaseDataStore = get(PROFILE_INFOS_DATABASE_DATA_STORE),
            cacheDataStore = get(PROFILE_INFOS_CACHE_DATA_STORE),
            freshDataHandler = get(),
            connectionProvider = get()
        )
    }

    single<TickerItemsRepository> {
        TickerItemsRepositoryImpl(
            serverDataStore = get(TICKER_ITEMS_SERVER_DATA_STORE),
            databaseDataStore = get(TICKER_ITEMS_DATABASE_DATA_STORE),
            freshDataHandler = get(),
            connectionProvider = get()
        )
    }

    single<TradeHistoryRepository> {
        TradeHistoryRepositoryImpl(
            serverDataStore = get(TRADE_HISTORY_SERVER_DATA_STORE),
            databaseDataStore = get(TRADE_HISTORY_DATABASE_DATA_STORE),
            freshDataHandler = get(),
            connectionProvider = get()
        )
    }

    single<UserAdmissionRepository> {
        UserAdmissionRepositoryImpl(
            serverDataStore = get(USER_ADMISSION_SERVER_DATA_STORE),
            connectionProvider = get()
        )
    }

    single<WalletsRepository> {
        WalletsRepositoryImpl(
            serverDataStore = get(WALLETS_SERVER_DATA_STORE),
            databaseDataStore = get(WALLETS_DATABASE_DATA_STORE),
            cacheDataStore = get(WALLETS_CACHE_DATA_STORE),
            freshDataHandler = get(),
            connectionProvider = get()
        )
    }
    single<WithdrawalsRepository> {
        WithdrawalsRepositoryImpl(
            serverDataStore = get(WITHDRAWALS_SERVER_DATA_STORE),
            databaseDataStore = get(WITHDRAWALS_DATABASE_DATA_STORE),
            freshDataHandler = get(),
            connectionProvider = get()
        )
    }

    single<FavoriteCurrencyPairsRepository> {
        FavoriteCurrencyPairsRepositoryImpl(
            databaseDataStore = get(FAVORITE_CURRENCY_PAIRS_DATABASE_DATA_STORE),
            cacheDataStore = get(FAVORITE_CURRENCY_PAIRS_CACHE_DATA_STORE)
        )
    }

    single<ReferralsRepository> {
        ReferralsRepositoryImpl(
            serverDataStore = get(REFERRALS_SERVER_DATA_STORE),
            connectionProvider = get()
        )
    }

    single<CurrencyMarketsRepository> {
        CurrencyMarketsRepositoryImpl(
            currencyPairsRepository = get(),
            tickerItemsRepository = get(),
            favoriteCurrencyPairsRepository = get()
        )
    }

    single<UtilitiesRepository> {
        UtilitiesRepositoryImpl(
            serverDataStore = get(UTILITIES_SERVER_DATA_STORE),
            connectionProvider = get()
        )
    }

    single<NotificationRepository> {
        NotificationRepositoryImpl(
            serverDataStore = get(NOTIFICATION_SERVER_DATA_STORE),
            connectionProvider = get()
        )
    }

    single<InboxRepository> {
        InboxRepositoryImpl(
            serverDataStore = get(INBOX_SERVER_DATA_STORE),
            databaseDataStore = get(INBOX_DATABASE_DATA_STORE),
            freshDataHandler = get(),
            connectionProvider = get()
        )
    }

    single<NewsRepository> {
        NewsRepositoryImpl(
            serverDataStore = get(NEWS_SERVER_DATA_STORE),
            freshDataHandler = get(),
            connectionProvider = get()
        )
    }

    single<AlertPriceRepository> {
        AlertPriceRepositoryImpl(
            serverDataStore = get(ALERT_PRICE_SERVER_DATA_STORE),
            databaseDataStore = get(ALERT_PRICE_DATABASE_DATA_STORE),
            freshDataHandler = get(),
            connectionProvider = get()
        )
    }

}