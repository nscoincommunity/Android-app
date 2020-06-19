package com.stocksexchange.android.di

import com.stocksexchange.android.data.stores.alertprice.AlertPriceDataStore
import com.stocksexchange.android.data.stores.alertprice.AlertPriceDatabaseDataStore
import com.stocksexchange.android.data.stores.alertprice.AlertPriceServerDataStore
import com.stocksexchange.android.data.stores.news.NewsDataStore
import com.stocksexchange.android.data.stores.news.NewsServerDataStore
import com.stocksexchange.android.data.stores.candlesticks.CandleSticksDataStore
import com.stocksexchange.android.data.stores.candlesticks.CandleSticksDatabaseDataStore
import com.stocksexchange.android.data.stores.candlesticks.CandleSticksServerDataStore
import com.stocksexchange.android.data.stores.currencies.CurrenciesDataStore
import com.stocksexchange.android.data.stores.currencies.CurrenciesDatabaseDataStore
import com.stocksexchange.android.data.stores.currencies.CurrenciesServerDataStore
import com.stocksexchange.android.data.stores.currencypairgroups.CurrencyPairGroupsDataStore
import com.stocksexchange.android.data.stores.currencypairgroups.CurrencyPairGroupsDatabaseDataStore
import com.stocksexchange.android.data.stores.currencypairgroups.CurrencyPairGroupsServerDataStore
import com.stocksexchange.android.data.stores.currencypairs.CurrencyPairsDataStore
import com.stocksexchange.android.data.stores.currencypairs.CurrencyPairsDatabaseDataStore
import com.stocksexchange.android.data.stores.currencypairs.CurrencyPairsServerDataStore
import com.stocksexchange.android.data.stores.deposits.DepositsDataStore
import com.stocksexchange.android.data.stores.deposits.DepositsDatabaseDataStore
import com.stocksexchange.android.data.stores.deposits.DepositsServerDataStore
import com.stocksexchange.android.data.stores.favoritecurrencypairs.FavoriteCurrencyPairsCacheDataStore
import com.stocksexchange.android.data.stores.favoritecurrencypairs.FavoriteCurrencyPairsDataStore
import com.stocksexchange.android.data.stores.favoritecurrencypairs.FavoriteCurrencyPairsDatabaseDataStore
import com.stocksexchange.android.data.stores.inbox.InboxDataStore
import com.stocksexchange.android.data.stores.inbox.InboxDatabaseDataStore
import com.stocksexchange.android.data.stores.inbox.InboxServerDataStore
import com.stocksexchange.android.data.stores.notification.NotificationDataStore
import com.stocksexchange.android.data.stores.notification.NotificationServerDataStore
import com.stocksexchange.android.data.stores.orderbooks.OrderbooksDataStore
import com.stocksexchange.android.data.stores.orderbooks.OrderbooksDatabaseDataStore
import com.stocksexchange.android.data.stores.orderbooks.OrderbooksServerDataStore
import com.stocksexchange.android.data.stores.orders.OrdersDataStore
import com.stocksexchange.android.data.stores.orders.OrdersDatabaseDataStore
import com.stocksexchange.android.data.stores.orders.OrdersServerDataStore
import com.stocksexchange.android.data.stores.profileinfos.ProfileInfosCacheDataStore
import com.stocksexchange.android.data.stores.profileinfos.ProfileInfosDataStore
import com.stocksexchange.android.data.stores.profileinfos.ProfileInfosDatabaseDataStore
import com.stocksexchange.android.data.stores.profileinfos.ProfileInfosServerDataStore
import com.stocksexchange.android.data.stores.referrals.ReferralsDataStore
import com.stocksexchange.android.data.stores.referrals.ReferralsServerDataStore
import com.stocksexchange.android.data.stores.settings.SettingsCacheDataStore
import com.stocksexchange.android.data.stores.tickeritems.TickerItemsDataStore
import com.stocksexchange.android.data.stores.tickeritems.TickerItemsDatabaseDataStore
import com.stocksexchange.android.data.stores.tickeritems.TickerItemsServerDataStore
import com.stocksexchange.android.data.stores.tradehistory.TradeHistoryDataStore
import com.stocksexchange.android.data.stores.tradehistory.TradeHistoryDatabaseDataStore
import com.stocksexchange.android.data.stores.tradehistory.TradeHistoryServerDataStore
import com.stocksexchange.android.data.stores.useradmission.UserAdmissionDataStore
import com.stocksexchange.android.data.stores.useradmission.UserAdmissionServerDataStore
import com.stocksexchange.android.data.stores.wallets.WalletsDataStore
import com.stocksexchange.android.data.stores.wallets.WalletsDatabaseDataStore
import com.stocksexchange.android.data.stores.wallets.WalletsServerDataStore
import com.stocksexchange.android.data.stores.withdrawals.WithdrawalsDataStore
import com.stocksexchange.android.data.stores.withdrawals.WithdrawalsDatabaseDataStore
import com.stocksexchange.android.data.stores.withdrawals.WithdrawalsServerDataStore
import com.stocksexchange.android.data.stores.settings.SettingsDataStore
import com.stocksexchange.android.data.stores.settings.SettingsDatabaseDataStore
import com.stocksexchange.android.data.stores.tradingfees.TradingFeesDataStore
import com.stocksexchange.android.data.stores.tradingfees.TradingFeesDatabaseDataStore
import com.stocksexchange.android.data.stores.tradingfees.TradingFeesServerDataStore
import com.stocksexchange.android.data.stores.utilities.UtilitiesDataStore
import com.stocksexchange.android.data.stores.utilities.UtilitiesServerDataStore
import com.stocksexchange.android.data.stores.wallets.WalletsCacheDataStore
import com.stocksexchange.android.di.utils.single
import org.koin.dsl.module

const val SETTINGS_DATABASE_DATA_STORE = "settings_database_data_store"
const val SETTINGS_CACHE_DATA_STORE = "settings_cache_data_store"

const val CANDLE_STICKS_DATABASE_DATA_STORE = "candle_sticks_database_data_store"
const val CANDLE_STICKS_SERVER_DATA_STORE = "candle_sticks_server_data_store"

const val CURRENCIES_DATABASE_DATA_STORE = "currencies_database_data_store"
const val CURRENCIES_SERVER_DATA_STORE = "currencies_server_data_store"

const val CURRENCY_PAIRS_DATABASE_DATA_STORE = "currency_pairs_database_data_store"
const val CURRENCY_PAIRS_SERVER_DATA_STORE = "currency_pairs_server_data_store"

const val CURRENCY_PAIR_GROUPS_DATABASE_DATA_STORE = "currency_pair_groups_database_data_store"
const val CURRENCY_PAIR_GROUPS_SERVER_DATA_STORE = "currency_pair_groups_server_data_store"

const val DEPOSITS_DATABASE_DATA_STORE = "deposits_database_data_store"
const val DEPOSITS_SERVER_DATA_STORE = "deposits_server_data_store"

const val ORDERBOOKS_DATABASE_DATA_STORE = "orderbooks_database_data_store"
const val ORDERBOOKS_SERVER_DATA_STORE = "orderbooks_server_data_store"

const val TRADING_FEES_DATABASE_DATA_STORE = "trading_fees_database_data_store"
const val TRADING_FEES_SERVER_DATA_STORE = "trading_fees_server_data_store"

const val ORDERS_DATABASE_DATA_STORE = "orders_database_data_store"
const val ORDERS_SERVER_DATA_STORE = "orders_server_data_store"

const val PROFILE_INFOS_DATABASE_DATA_STORE = "profile_infos_database_data_store"
const val PROFILE_INFOS_SERVER_DATA_STORE = "profile_infos_server_data_store"
const val PROFILE_INFOS_CACHE_DATA_STORE = "profile_infos_cache_data_store"

const val TICKER_ITEMS_DATABASE_DATA_STORE = "ticker_items_database_data_store"
const val TICKER_ITEMS_SERVER_DATA_STORE = "ticker_items_server_data_store"

const val TRADE_HISTORY_DATABASE_DATA_STORE = "trade_history_database_data_store"
const val TRADE_HISTORY_SERVER_DATA_STORE = "trade_history_server_data_store"

const val USER_ADMISSION_SERVER_DATA_STORE = "user_admission_server_data_store"

const val WALLETS_DATABASE_DATA_STORE = "wallets_database_data_store"
const val WALLETS_SERVER_DATA_STORE = "wallets_server_data_store"
const val WALLETS_CACHE_DATA_STORE = "wallets_cache_data_store"

const val WITHDRAWALS_DATABASE_DATA_STORE = "withdrawals_database_data_store"
const val WITHDRAWALS_SERVER_DATA_STORE = "withdrawals_server_data_store"

const val REFERRALS_SERVER_DATA_STORE = "referrals_server_data_store"

const val FAVORITE_CURRENCY_PAIRS_DATABASE_DATA_STORE = "favorite_currency_pairs_database_data_store"
const val FAVORITE_CURRENCY_PAIRS_CACHE_DATA_STORE = "favorite_currency_pairs_cache_data_store"

const val NOTIFICATION_SERVER_DATA_STORE = "notifications_server_data_store"

const val INBOX_DATABASE_DATA_STORE = "inbox_database_data_store"
const val INBOX_SERVER_DATA_STORE = "inbox_server_data_store"
const val UTILITIES_SERVER_DATA_STORE = "utilities_server_data_store"

const val NEWS_SERVER_DATA_STORE = "news_server_data_store"

const val ALERT_PRICE_DATABASE_DATA_STORE = "alert_price_database_data_store"
const val ALERT_PRICE_SERVER_DATA_STORE = "alert_price_server_data_store"


@Suppress("USELESS_CAST")
val dataStoresModule = module {

    single<SettingsDataStore>(SETTINGS_DATABASE_DATA_STORE) { SettingsDatabaseDataStore(get()) }
    single<SettingsDataStore>(SETTINGS_CACHE_DATA_STORE) { SettingsCacheDataStore() }

    single<CandleSticksDataStore>(CANDLE_STICKS_DATABASE_DATA_STORE) { CandleSticksDatabaseDataStore(get()) }
    single<CandleSticksDataStore>(CANDLE_STICKS_SERVER_DATA_STORE) { CandleSticksServerDataStore(get()) }

    single<CurrenciesDataStore>(CURRENCIES_DATABASE_DATA_STORE) { CurrenciesDatabaseDataStore(get()) }
    single<CurrenciesDataStore>(CURRENCIES_SERVER_DATA_STORE) { CurrenciesServerDataStore(get()) }

    single<CurrencyPairsDataStore>(CURRENCY_PAIRS_DATABASE_DATA_STORE) { CurrencyPairsDatabaseDataStore(get()) }
    single<CurrencyPairsDataStore>(CURRENCY_PAIRS_SERVER_DATA_STORE) { CurrencyPairsServerDataStore(get()) }

    single<CurrencyPairGroupsDataStore>(CURRENCY_PAIR_GROUPS_DATABASE_DATA_STORE) { CurrencyPairGroupsDatabaseDataStore(get()) }
    single<CurrencyPairGroupsDataStore>(CURRENCY_PAIR_GROUPS_SERVER_DATA_STORE) { CurrencyPairGroupsServerDataStore(get()) }

    single<DepositsDataStore>(DEPOSITS_DATABASE_DATA_STORE) { DepositsDatabaseDataStore(get()) }
    single<DepositsDataStore>(DEPOSITS_SERVER_DATA_STORE) { DepositsServerDataStore(get()) }

    single<OrderbooksDataStore>(ORDERBOOKS_DATABASE_DATA_STORE) { OrderbooksDatabaseDataStore(get()) }
    single<OrderbooksDataStore>(ORDERBOOKS_SERVER_DATA_STORE) { OrderbooksServerDataStore(get()) }

    single<TradingFeesDataStore>(TRADING_FEES_DATABASE_DATA_STORE) { TradingFeesDatabaseDataStore(get()) }
    single<TradingFeesDataStore>(TRADING_FEES_SERVER_DATA_STORE) { TradingFeesServerDataStore(get()) }

    single<OrdersDataStore>(ORDERS_DATABASE_DATA_STORE) { OrdersDatabaseDataStore(get()) }
    single<OrdersDataStore>(ORDERS_SERVER_DATA_STORE) { OrdersServerDataStore(get()) }

    single<ProfileInfosDataStore>(PROFILE_INFOS_DATABASE_DATA_STORE) { ProfileInfosDatabaseDataStore(get()) }
    single<ProfileInfosDataStore>(PROFILE_INFOS_SERVER_DATA_STORE) { ProfileInfosServerDataStore(get()) }
    single<ProfileInfosDataStore>(PROFILE_INFOS_CACHE_DATA_STORE) { ProfileInfosCacheDataStore() }

    single<TickerItemsDataStore>(TICKER_ITEMS_DATABASE_DATA_STORE) { TickerItemsDatabaseDataStore(get()) }
    single<TickerItemsDataStore>(TICKER_ITEMS_SERVER_DATA_STORE) { TickerItemsServerDataStore(get()) }

    single<TradeHistoryDataStore>(TRADE_HISTORY_DATABASE_DATA_STORE) { TradeHistoryDatabaseDataStore(get()) }
    single<TradeHistoryDataStore>(TRADE_HISTORY_SERVER_DATA_STORE) { TradeHistoryServerDataStore(get()) }

    single<UserAdmissionDataStore>(USER_ADMISSION_SERVER_DATA_STORE) { UserAdmissionServerDataStore(get()) }

    single<WalletsDataStore>(WALLETS_DATABASE_DATA_STORE) { WalletsDatabaseDataStore(get()) }
    single<WalletsDataStore>(WALLETS_SERVER_DATA_STORE) { WalletsServerDataStore(get()) }
    single<WalletsDataStore>(WALLETS_CACHE_DATA_STORE) { WalletsCacheDataStore() }

    single<WithdrawalsDataStore>(WITHDRAWALS_DATABASE_DATA_STORE) { WithdrawalsDatabaseDataStore(get()) }
    single<WithdrawalsDataStore>(WITHDRAWALS_SERVER_DATA_STORE) { WithdrawalsServerDataStore(get()) }

    single<ReferralsDataStore>(REFERRALS_SERVER_DATA_STORE) { ReferralsServerDataStore(get()) }

    single<FavoriteCurrencyPairsDataStore>(FAVORITE_CURRENCY_PAIRS_DATABASE_DATA_STORE) { FavoriteCurrencyPairsDatabaseDataStore(get()) }
    single<FavoriteCurrencyPairsDataStore>(FAVORITE_CURRENCY_PAIRS_CACHE_DATA_STORE) { FavoriteCurrencyPairsCacheDataStore() }

    single<UtilitiesDataStore>(UTILITIES_SERVER_DATA_STORE) { UtilitiesServerDataStore(get()) }

    single<NotificationDataStore>(NOTIFICATION_SERVER_DATA_STORE) { NotificationServerDataStore(get()) }

    single<InboxDataStore>(INBOX_DATABASE_DATA_STORE) { InboxDatabaseDataStore(get()) }
    single<InboxDataStore>(INBOX_SERVER_DATA_STORE) { InboxServerDataStore(get()) }

    single<NewsDataStore>(NEWS_SERVER_DATA_STORE) { NewsServerDataStore(get(), get()) }

    single<AlertPriceDataStore>(ALERT_PRICE_DATABASE_DATA_STORE) { AlertPriceDatabaseDataStore(get()) }
    single<AlertPriceDataStore>(ALERT_PRICE_SERVER_DATA_STORE) { AlertPriceServerDataStore(get()) }
}