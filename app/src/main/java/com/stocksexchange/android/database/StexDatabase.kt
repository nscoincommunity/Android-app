package com.stocksexchange.android.database

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.stocksexchange.android.database.StexDatabase.Companion.VERSION
import com.stocksexchange.android.database.converters.Converters
import com.stocksexchange.android.database.daos.*
import com.stocksexchange.android.database.daos.CurrencyDao
import com.stocksexchange.android.database.daos.DepositDao
import com.stocksexchange.android.database.daos.OrderDao
import com.stocksexchange.android.database.daos.OrderbookDao
import com.stocksexchange.android.database.daos.TradeHistoryDao
import com.stocksexchange.android.database.model.*
import com.stocksexchange.android.database.model.DatabaseCurrency
import com.stocksexchange.android.database.model.DatabaseDeposit
import com.stocksexchange.android.database.model.DatabaseOrder
import com.stocksexchange.android.database.model.DatabaseOrderbook
import com.stocksexchange.android.database.model.DatabaseTrade

/**
 * A Room database this application uses to store data.
 */
@Database(entities = [
    DatabaseSettings::class,
    DatabaseCandleStick::class,
    DatabaseCurrency::class,
    DatabaseCurrencyPair::class,
    DatabaseCurrencyPairGroup::class,
    DatabaseFavoriteCurrencyPair::class,
    DatabaseDeposit::class,
    DatabaseOrderbook::class,
    DatabaseTradingFees::class,
    DatabaseOrder::class,
    DatabaseProfileInfo::class,
    DatabaseTickerItem::class,
    DatabaseTrade::class,
    DatabaseWallet::class,
    DatabaseWithdrawal::class,
    DatabaseInbox::class,
    DatabaseAlertPrice::class
], version = VERSION)
@TypeConverters(Converters::class)
abstract class StexDatabase : RoomDatabase() {


    companion object {

        const val NAME = "stocksexchange_database.db"
        const val VERSION = 83

    }


    abstract val settingsDao: SettingsDao
    abstract val candleStickDao: CandleStickDao
    abstract val currencyDao: CurrencyDao
    abstract val currencyPairDao: CurrencyPairDao
    abstract val currencyPairGroupDao: CurrencyPairGroupDao
    abstract val favoriteCurrencyPairDao: FavoriteCurrencyPairDao
    abstract val depositDao: DepositDao
    abstract val orderbookDao: OrderbookDao
    abstract val tradingFeesDao: TradingFeesDao
    abstract val orderDao: OrderDao
    abstract val profileInfoDao: ProfileInfoDao
    abstract val tickerItemDao: TickerItemDao
    abstract val tradeHistoryDao: TradeHistoryDao
    abstract val walletDao: WalletDao
    abstract val withdrawalDao: WithdrawalDao
    abstract val inboxDao: InboxDao
    abstract val alertPriceDao: AlertPriceDao

}

