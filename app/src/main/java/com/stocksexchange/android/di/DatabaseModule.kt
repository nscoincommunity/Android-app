package com.stocksexchange.android.di

import androidx.room.Room
import com.stocksexchange.android.database.StexDatabase
import com.stocksexchange.android.database.migrations.MIGRATIONS
import com.stocksexchange.android.database.tables.*
import com.stocksexchange.android.database.tables.CurrenciesTable
import com.stocksexchange.android.database.tables.DepositsTable
import com.stocksexchange.android.database.tables.TradeHistoryTable
import org.koin.dsl.module

val databaseModule = module {

    single {
        Room.databaseBuilder(
            get(),
            StexDatabase::class.java,
            StexDatabase.NAME
        ).addMigrations(*MIGRATIONS)
        .allowMainThreadQueries().build()
    }

    single { get<StexDatabase>().settingsDao }
    single { get<StexDatabase>().candleStickDao }
    single { get<StexDatabase>().currencyDao }
    single { get<StexDatabase>().currencyPairDao }
    single { get<StexDatabase>().currencyPairGroupDao }
    single { get<StexDatabase>().favoriteCurrencyPairDao }
    single { get<StexDatabase>().depositDao }
    single { get<StexDatabase>().orderbookDao }
    single { get<StexDatabase>().tradingFeesDao }
    single { get<StexDatabase>().orderDao }
    single { get<StexDatabase>().profileInfoDao }
    single { get<StexDatabase>().tickerItemDao }
    single { get<StexDatabase>().tradeHistoryDao }
    single { get<StexDatabase>().walletDao }
    single { get<StexDatabase>().withdrawalDao }
    single { get<StexDatabase>().inboxDao }
    single { get<StexDatabase>().alertPriceDao }

    single { SettingsTable(get()) }
    single { CandleSticksTable(get()) }
    single { CurrenciesTable(get()) }
    single { CurrencyPairsTable(get()) }
    single { CurrencyPairGroupsTable(get()) }
    single { FavoriteCurrencyPairsTable(get()) }
    single { DepositsTable(get()) }
    single { OrderbooksTable(get()) }
    single { TradingFeesTable(get()) }
    single { OrdersTable(get()) }
    single { ProfileInfosTable(get()) }
    single { TickerItemsTable(get()) }
    single { TradeHistoryTable(get()) }
    single { WalletsTable(get()) }
    single { WithdrawalsTable(get()) }
    single { InboxTable(get()) }
    single { AlertPriceTable(get()) }

}