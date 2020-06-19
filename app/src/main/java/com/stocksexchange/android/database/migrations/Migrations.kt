package com.stocksexchange.android.database.migrations

import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase
import com.stocksexchange.api.model.rest.FiatCurrency
import com.stocksexchange.android.model.Language
import com.stocksexchange.android.theming.model.Themes

private val MIGRATION_1_2: Migration = object : Migration(1, 2) {

    override fun migrate(database: SupportSQLiteDatabase) {
        with(database) {
            execSQL(
                "CREATE TABLE IF NOT EXISTS orders_new " +
                "(id INTEGER NOT NULL, market_name TEXT NOT NULL, currency TEXT NOT NULL, " +
                "market TEXT NOT NULL, type TEXT NOT NULL, trade_type TEXT NOT NULL, " +
                "amount REAL NOT NULL, buy_amount REAL NOT NULL, sell_amount REAL NOT NULL, " +
                "rate REAL NOT NULL, timestamp INTEGER NOT NULL, PRIMARY KEY(id))"
            )
            execSQL(
                "INSERT INTO orders_new " +
                "(id, market_name, currency, market, type, " +
                "trade_type, amount, buy_amount, sell_amount, rate, timestamp) " +
                "SELECT id, market_name, \"\", \"\", type, trade_type, amount, " +
                "buy_amount, sell_amount, rate, timestamp FROM orders"
            )
            execSQL("DROP TABLE orders")
            execSQL("ALTER TABLE orders_new RENAME TO orders")
        }
    }

}


private val MIGRATION_2_3: Migration = object : Migration(2, 3) {

    override fun migrate(database: SupportSQLiteDatabase) {
        with(database) {
            execSQL(
                "CREATE TABLE IF NOT EXISTS currencies_new " +
                "(name TEXT NOT NULL, long_name TEXT NOT NULL, minimum_withdrawal_amount REAL NOT NULL, " +
                "minimum_deposit_amount REAL NOT NULL, deposit_fee_currency TEXT NOT NULL, " +
                "deposit_fee REAL NOT NULL, withdrawal_fee_currency TEXT NOT NULL, withdrawal_fee REAL NOT NULL, " +
                "block_explorer_url TEXT NOT NULL, is_active INTEGER NOT NULL, PRIMARY KEY(name))"
            )
            execSQL(
                "INSERT INTO currencies_new " +
                "(name, long_name, minimum_withdrawal_amount, minimum_deposit_amount, deposit_fee_currency," +
                "deposit_fee, withdrawal_fee_currency, withdrawal_fee, block_explorer_url, is_active) " +
                "SELECT name, long_name, minimum_withdrawal_amount, minimum_deposit_amount, deposit_fee_currency," +
                "deposit_fee, withdrawal_fee_currency, withdrawal_fee, \"\", is_active FROM currencies"
            )
            execSQL("DROP TABLE currencies")
            execSQL("ALTER TABLE currencies_new RENAME TO currencies")
        }
    }

}


private val MIGRATION_3_4: Migration = object : Migration(3, 4) {

    override fun migrate(database: SupportSQLiteDatabase) {
        with(database) {
            execSQL(
                "CREATE TABLE IF NOT EXISTS orders_new " +
                "(id INTEGER NOT NULL, market_name TEXT NOT NULL, currency TEXT NOT NULL, " +
                "market TEXT NOT NULL, type TEXT NOT NULL, trade_type TEXT NOT NULL, " +
                "amount REAL NOT NULL, original_amount REAL NOT NULL, buy_amount REAL NOT NULL, " +
                "sell_amount REAL NOT NULL, rate REAL NOT NULL, rates_map TEXT NOT NULL, " +
                "timestamp INTEGER NOT NULL, PRIMARY KEY(id))"
            )
            execSQL(
                "INSERT INTO orders_new " +
                "(id, market_name, currency, market, type, trade_type, amount, " +
                "original_amount, buy_amount, sell_amount, rate, rates_map, timestamp) " +
                "SELECT id, market_name, currency, market, type, trade_type, amount, " +
                "0.0, buy_amount, sell_amount, rate, \"\", timestamp FROM orders"
            )
            execSQL("DROP TABLE orders")
            execSQL("ALTER TABLE orders_new RENAME TO orders")
        }
    }

}


private val MIGRATION_4_5: Migration = object : Migration(4, 5) {

    override fun migrate(database: SupportSQLiteDatabase) {
        with(database) {
            execSQL(
                "CREATE TABLE IF NOT EXISTS orders_new " +
                "(id INTEGER NOT NULL, market_name TEXT NOT NULL, currency TEXT NOT NULL, " +
                "market TEXT NOT NULL, type TEXT NOT NULL, trade_type TEXT NOT NULL, " +
                "amount REAL NOT NULL, original_amount REAL NOT NULL, buy_amount REAL NOT NULL, " +
                "sell_amount REAL NOT NULL, rate REAL NOT NULL, rates_map TEXT NOT NULL, " +
                "timestamp INTEGER NOT NULL, PRIMARY KEY(id, type))"
            )
            execSQL(
                "INSERT INTO orders_new " +
                "(id, market_name, currency, market, type, trade_type, amount, " +
                "original_amount, buy_amount, sell_amount, rate, rates_map, timestamp) " +
                "SELECT id, market_name, currency, market, type, trade_type, amount, " +
                "original_amount, buy_amount, sell_amount, rate, rates_map, timestamp FROM orders"
            )
            execSQL("DROP TABLE orders")
            execSQL("ALTER TABLE orders_new RENAME TO orders")
        }
    }

}


private val MIGRATION_5_6: Migration = object : Migration(5, 6) {

    override fun migrate(database: SupportSQLiteDatabase) {
        with(database) {
            execSQL(
                "CREATE TABLE IF NOT EXISTS transactions_new " +
                "(id INTEGER NOT NULL, transaction_id TEXT NOT NULL, type TEXT NOT NULL, " +
                "currency TEXT NOT NULL, status TEXT NOT NULL, amount REAL NOT NULL, " +
                "fee TEXT NOT NULL, address TEXT NOT NULL, timestamp INTEGER NOT NULL, " +
                "PRIMARY KEY(id))"
            )
            execSQL(
                "INSERT INTO transactions_new " +
                "(id, transaction_id, type, currency, status, amount, fee, address, timestamp) " +
                "SELECT id, IFNULL(transaction_id, \"\"), type, currency, status, amount, fee, " +
                "address, timestamp FROM transactions"
            )
            execSQL("DROP TABLE transactions")
            execSQL("ALTER TABLE transactions_new RENAME TO transactions")
        }
    }

}


private val MIGRATION_6_7: Migration = object : Migration(6, 7) {

    override fun migrate(database: SupportSQLiteDatabase) {
        database.execSQL(
            "CREATE TABLE IF NOT EXISTS coins " +
            "(id INTEGER NOT NULL, name TEXT NOT NULL, symbol TEXT NOT NULL, slug TEXT NOT NULL, " +
            "rank INTEGER NOT NULL, circulating_supply REAL NOT NULL, total_supply REAL NOT NULL, " +
            "max_supply REAL NOT NULL, quotes TEXT NOT NULL, PRIMARY KEY(id))"
        )
    }

}


private val MIGRATION_7_8: Migration = object : Migration(7, 8) {

    override fun migrate(database: SupportSQLiteDatabase) {
        with(database) {
            execSQL(
                "CREATE TABLE IF NOT EXISTS settings_new " +
                "(id INTEGER NOT NULL, is_sound_enabled INTEGER NOT NULL, " +
                "is_vibration_enabled INTEGER NOT NULL, is_phone_led_enabled INTEGER NOT NULL, " +
                "notification_ringtone TEXT NOT NULL, decimal_separator TEXT NOT NULL, " +
                "coins_pricing_currency TEXT NOT NULL, PRIMARY KEY(id))"
            )
            execSQL(
                "INSERT INTO settings_new " +
                "(id, is_sound_enabled, is_vibration_enabled, is_phone_led_enabled," +
                "notification_ringtone, decimal_separator, coins_pricing_currency) " +
                "SELECT id, is_sound_enabled, is_vibration_enabled, is_phone_led_enabled," +
                "notification_ringtone, decimal_separator, \"USD\" FROM settings"
            )
            execSQL("DROP TABLE settings")
            execSQL("ALTER TABLE settings_new RENAME TO settings")
        }
    }

}


private val MIGRATION_8_9: Migration = object : Migration(8, 9) {

    override fun migrate(database: SupportSQLiteDatabase) {
        database.execSQL(
            "CREATE TABLE IF NOT EXISTS alerts " +
            "(name TEXT NOT NULL, symbol TEXT NOT NULL, price REAL NOT NULL, " +
            "direction TEXT NOT NULL, is_enabled INTEGER NOT NULL, " +
            "creation_time INTEGER NOT NULL, PRIMARY KEY(name, price))"
        )
    }

}


private val MIGRATION_9_10: Migration = object : Migration(9, 10) {

    override fun migrate(database: SupportSQLiteDatabase) {
        with(database) {
            execSQL(
                "CREATE TABLE IF NOT EXISTS alerts_new " +
                "(coin_id INTEGER NOT NULL, coin_name TEXT NOT NULL, " +
                "coin_symbol TEXT NOT NULL, price REAL NOT NULL, " +
                "price_currency TEXT NOT NULL, direction TEXT NOT NULL, " +
                "is_enabled INTEGER NOT NULL, creation_time INTEGER NOT NULL, " +
                "PRIMARY KEY(coin_id, price))"
            )
            execSQL(
                "INSERT INTO alerts_new " +
                "(coin_id, coin_name, coin_symbol, price, price_currency, direction, " +
                "is_enabled, creation_time) SELECT -1, name, symbol, price, \"\", direction," +
                "is_enabled, creation_time FROM alerts"
            )
            execSQL("DROP TABLE alerts")
            execSQL("ALTER TABLE alerts_new RENAME TO alerts")
        }
    }

}


private val MIGRATION_10_11: Migration = object : Migration(10, 11) {

    override fun migrate(database: SupportSQLiteDatabase) {
        with(database) {
            execSQL(
                "CREATE TABLE IF NOT EXISTS settings_new " +
                "(id INTEGER NOT NULL, is_sound_enabled INTEGER NOT NULL, is_vibration_enabled INTEGER NOT NULL, " +
                "is_phone_led_enabled INTEGER NOT NULL, notification_ringtone TEXT NOT NULL, " +
                "decimal_separator TEXT NOT NULL, coins_pricing_currency TEXT NOT NULL, " +
                "background_sync_interval TEXT NOT NULL, PRIMARY KEY(id))"
            )
            execSQL(
                "INSERT INTO settings_new " +
                "(id, is_sound_enabled, is_vibration_enabled, is_phone_led_enabled, " +
                "notification_ringtone, decimal_separator, coins_pricing_currency, " +
                "background_sync_interval) SELECT id, is_sound_enabled, is_vibration_enabled, " +
                "is_phone_led_enabled, notification_ringtone, decimal_separator, coins_pricing_currency, " +
                "\"FIFTEEN_MINUTES\" FROM settings"
            )
            execSQL("DROP TABLE settings")
            execSQL("ALTER TABLE settings_new RENAME TO settings")
        }
    }

}


private val MIGRATION_11_12: Migration = object : Migration(11, 12) {

    override fun migrate(database: SupportSQLiteDatabase) {
        with(database) {
            execSQL(
                "CREATE TABLE IF NOT EXISTS settings_new " +
                "(id INTEGER NOT NULL, is_sound_enabled INTEGER NOT NULL, " +
                "is_vibration_enabled INTEGER NOT NULL, is_phone_led_enabled INTEGER NOT NULL, " +
                "is_grouping_enabled INTEGER NOT NULL, notification_ringtone TEXT NOT NULL, " +
                "decimal_separator TEXT NOT NULL, grouping_separator TEXT NOT NULL, " +
                "coins_pricing_currency TEXT NOT NULL, background_sync_interval TEXT NOT NULL, " +
                "PRIMARY KEY(id))"
            )
            execSQL(
                "INSERT INTO settings_new " +
                "(id, is_sound_enabled, is_vibration_enabled, is_phone_led_enabled, " +
                "is_grouping_enabled, notification_ringtone, decimal_separator, grouping_separator, " +
                "coins_pricing_currency, background_sync_interval) SELECT id, is_sound_enabled, " +
                "is_vibration_enabled, is_phone_led_enabled, 1, notification_ringtone, decimal_separator, " +
                "\"COMMA\", coins_pricing_currency, background_sync_interval FROM settings"
            )
            execSQL("DROP TABLE settings")
            execSQL("ALTER TABLE settings_new RENAME TO settings")
        }
    }

}


private val MIGRATION_12_13: Migration = object : Migration(12, 13) {

    override fun migrate(database: SupportSQLiteDatabase) {
        with(database) {
            execSQL(
                "CREATE TABLE IF NOT EXISTS settings_new " +
                "(id INTEGER NOT NULL, is_sound_enabled INTEGER NOT NULL, " +
                "is_vibration_enabled INTEGER NOT NULL, is_phone_led_enabled INTEGER NOT NULL, " +
                "is_grouping_enabled INTEGER NOT NULL, notification_ringtone TEXT NOT NULL, " +
                "decimal_separator TEXT NOT NULL, grouping_separator TEXT NOT NULL, " +
                "coins_pricing_currency TEXT NOT NULL, background_sync_interval TEXT NOT NULL, " +
                "theme_id INTEGER NOT NULL, PRIMARY KEY(id))"
            )
            execSQL(
                "INSERT INTO settings_new " +
                "(id, is_sound_enabled, is_vibration_enabled, is_phone_led_enabled, " +
                "is_grouping_enabled, notification_ringtone, decimal_separator, grouping_separator, " +
                "coins_pricing_currency, background_sync_interval, theme_id) SELECT id, is_sound_enabled, " +
                "is_vibration_enabled, is_phone_led_enabled, is_grouping_enabled, notification_ringtone, " +
                "decimal_separator, grouping_separator, coins_pricing_currency, background_sync_interval, 1 " +
                "FROM settings"
            )
            execSQL("DROP TABLE settings")
            execSQL("ALTER TABLE settings_new RENAME TO settings")
        }
    }

}


private val MIGRATION_13_14: Migration = object : Migration(13, 14) {

    override fun migrate(database: SupportSQLiteDatabase) {
        with(database) {
            execSQL(
                "CREATE TABLE IF NOT EXISTS settings_new " +
                "(id INTEGER NOT NULL, is_sound_enabled INTEGER NOT NULL, " +
                "is_vibration_enabled INTEGER NOT NULL, is_phone_led_enabled INTEGER NOT NULL, " +
                "is_grouping_enabled INTEGER NOT NULL, is_fingerprint_unlock_enabled INTEGER NOT NULL, " +
                "notification_ringtone TEXT NOT NULL, pin_code TEXT NOT NULL, " +
                "decimal_separator TEXT NOT NULL, grouping_separator TEXT NOT NULL, " +
                "coins_pricing_currency TEXT NOT NULL, background_sync_interval TEXT NOT NULL, " +
                "theme_id INTEGER NOT NULL, PRIMARY KEY(id))"
            )
            execSQL(
                "INSERT INTO settings_new " +
                "(id, is_sound_enabled, is_vibration_enabled, is_phone_led_enabled, " +
                "is_grouping_enabled, is_fingerprint_unlock_enabled, notification_ringtone, " +
                "pin_code, decimal_separator, grouping_separator, coins_pricing_currency, " +
                "background_sync_interval, theme_id) SELECT id, is_sound_enabled, " +
                "is_vibration_enabled, is_phone_led_enabled, is_grouping_enabled, 0, " +
                "notification_ringtone, \"\", decimal_separator, grouping_separator, " +
                "coins_pricing_currency, background_sync_interval, theme_id FROM settings"
            )
            execSQL("DROP TABLE settings")
            execSQL("ALTER TABLE settings_new RENAME TO settings")
        }
    }

}


private val MIGRATION_14_15: Migration = object : Migration(14, 15) {

    override fun migrate(database: SupportSQLiteDatabase) {
        with(database) {
            execSQL(
                "CREATE TABLE IF NOT EXISTS settings_new " +
                "(id INTEGER NOT NULL, is_sound_enabled INTEGER NOT NULL, " +
                "is_vibration_enabled INTEGER NOT NULL, is_phone_led_enabled INTEGER NOT NULL, " +
                "is_grouping_enabled INTEGER NOT NULL, is_fingerprint_unlock_enabled INTEGER NOT NULL, " +
                "is_force_authentication_on_app_startup_is_enabled INTEGER NOT NULL, " +
                "notification_ringtone TEXT NOT NULL, pin_code TEXT NOT NULL, " +
                "decimal_separator TEXT NOT NULL, grouping_separator TEXT NOT NULL, " +
                "coins_pricing_currency TEXT NOT NULL, background_sync_interval TEXT NOT NULL, " +
                "session_expiration_timeout TEXT NOT NULL, theme_id INTEGER NOT NULL, PRIMARY KEY(id))"
            )
            execSQL(
                "INSERT INTO settings_new " +
                "(id, is_sound_enabled, is_vibration_enabled, is_phone_led_enabled, " +
                "is_grouping_enabled, is_fingerprint_unlock_enabled, is_force_authentication_on_app_startup_is_enabled, " +
                "notification_ringtone, pin_code, decimal_separator, grouping_separator, coins_pricing_currency, " +
                "background_sync_interval, session_expiration_timeout, theme_id) SELECT id, is_sound_enabled, " +
                "is_vibration_enabled, is_phone_led_enabled, is_grouping_enabled, is_fingerprint_unlock_enabled, " +
                "0, notification_ringtone, pin_code, decimal_separator, grouping_separator, " +
                "coins_pricing_currency, background_sync_interval, \"FIVE_MINUTES\", theme_id " +
                "FROM settings"
            )
            execSQL("DROP TABLE settings")
            execSQL("ALTER TABLE settings_new RENAME TO settings")
        }
    }

}


private val MIGRATION_15_16: Migration = object : Migration(15, 16) {

    override fun migrate(database: SupportSQLiteDatabase) {
        with(database) {
            execSQL(
                "CREATE TABLE IF NOT EXISTS currency_markets_new " +
                "(id INTEGER NOT NULL, name TEXT NOT NULL, currency_symbol TEXT NOT NULL, " +
                "market_symbol TEXT NOT NULL, min_order_amount REAL NOT NULL, " +
                "daily_buy_max_price REAL NOT NULL, daily_sell_min_price REAL NOT NULL, " +
                "last_price REAL NOT NULL, last_price_day_ago REAL NOT NULL, " +
                "daily_volume REAL NOT NULL, spread REAL NOT NULL, " +
                "buy_fee_percent REAL NOT NULL, sell_fee_percent REAL NOT NULL, PRIMARY KEY(id))"
            )
            execSQL(
                "INSERT INTO currency_markets_new " +
                "(id, name, currency_symbol, market_symbol, min_order_amount, daily_buy_max_price, " +
                "daily_sell_min_price, last_price, last_price_day_ago, daily_volume, spread, " +
                "buy_fee_percent, sell_fee_percent) SELECT id, name, currency, market, min_order_amount, " +
                "daily_buy_max_price, daily_sell_min_price, last_price, last_price_day_ago, daily_volume, " +
                "spread, buy_fee_percent, sell_fee_percent FROM currency_markets"
            )
            execSQL("DROP TABLE currency_markets")
            execSQL("ALTER TABLE currency_markets_new RENAME TO currency_markets")

            execSQL(
                "CREATE TABLE IF NOT EXISTS orders_new " +
                "(id INTEGER NOT NULL, market_name TEXT NOT NULL, currency_symbol TEXT NOT NULL, " +
                "market_symbol TEXT NOT NULL, type TEXT NOT NULL, trade_type TEXT NOT NULL, " +
                "amount REAL NOT NULL, original_amount REAL NOT NULL, buy_amount REAL NOT NULL, " +
                "sell_amount REAL NOT NULL, rate REAL NOT NULL, rates_map TEXT NOT NULL, " +
                "timestamp INTEGER NOT NULL, PRIMARY KEY(id, type))"
            )
            execSQL(
                "INSERT INTO orders_new " +
                "(id, market_name, currency_symbol, market_symbol, type, trade_type, amount, original_amount, " +
                "buy_amount, sell_amount, rate, rates_map, timestamp) SELECT id, market_name, currency, " +
                "market, type, trade_type, amount, original_amount, buy_amount, sell_amount, rate, rates_map, " +
                "timestamp FROM orders"
            )
            execSQL("DROP TABLE orders")
            execSQL("ALTER TABLE orders_new RENAME TO orders")
        }
    }

}


private val MIGRATION_16_17: Migration = object : Migration(16, 17) {

    override fun migrate(database: SupportSQLiteDatabase) {
        with(database) {
            execSQL(
                "CREATE TABLE IF NOT EXISTS currencies_new " +
                "(name TEXT NOT NULL, long_name TEXT NOT NULL, minimum_withdrawal_amount REAL NOT NULL, " +
                "minimum_deposit_amount REAL NOT NULL, deposit_fee_currency TEXT NOT NULL, " +
                "deposit_fee REAL NOT NULL, withdrawal_fee_currency TEXT NOT NULL, " +
                "withdrawal_fee REAL NOT NULL, block_explorer_url TEXT NOT NULL, " +
                "is_active INTEGER NOT NULL, is_depositing_disabled INTEGER NOT NULL, PRIMARY KEY(name))"
            )
            execSQL(
                "INSERT INTO currencies_new " +
                "(name, long_name, minimum_withdrawal_amount, minimum_deposit_amount, deposit_fee_currency, " +
                "deposit_fee, withdrawal_fee_currency, withdrawal_fee, block_explorer_url, is_active, is_depositing_disabled) " +
                "SELECT name, long_name, minimum_withdrawal_amount, minimum_deposit_amount, deposit_fee_currency, " +
                "deposit_fee, withdrawal_fee_currency, withdrawal_fee, block_explorer_url, is_active, 0 FROM " +
                "currencies"
            )
            execSQL("DROP TABLE currencies")
            execSQL("ALTER TABLE currencies_new RENAME TO currencies")
        }
    }

}


private val MIGRATION_17_18: Migration = object : Migration(17, 18) {

    override fun migrate(database: SupportSQLiteDatabase) {
        with(database) {
            execSQL(
                "CREATE TABLE IF NOT EXISTS currency_markets_new " +
                "(id INTEGER NOT NULL, name TEXT NOT NULL, currency_symbol TEXT NOT NULL, " +
                "market_symbol TEXT NOT NULL, min_order_amount REAL NOT NULL, " +
                "daily_buy_max_price REAL NOT NULL, daily_sell_min_price REAL NOT NULL, " +
                "last_price REAL NOT NULL, last_price_day_ago REAL NOT NULL, " +
                "daily_volume REAL NOT NULL, spread REAL NOT NULL, buy_fee_percent REAL NOT NULL, " +
                "sell_fee_percent REAL NOT NULL, is_active INTEGER NOT NULL, PRIMARY KEY(id))"
            )
            execSQL(
                "INSERT INTO currency_markets_new " +
                "(id, name, currency_symbol, market_symbol, min_order_amount, daily_buy_max_price, " +
                "daily_sell_min_price, last_price, last_price_day_ago, daily_volume, spread, " +
                "buy_fee_percent, sell_fee_percent, is_active) " +
                "SELECT id, name, currency_symbol, market_symbol, min_order_amount, daily_buy_max_price, " +
                "daily_sell_min_price, last_price, last_price_day_ago, daily_volume, spread, " +
                "buy_fee_percent, sell_fee_percent, 1 FROM currency_markets"
            )
            execSQL("DROP TABLE currency_markets")
            execSQL("ALTER TABLE currency_markets_new RENAME TO currency_markets")
        }
    }

}


private val MIGRATION_18_19: Migration = object : Migration(18, 19) {

    override fun migrate(database: SupportSQLiteDatabase) {
        with(database) {
            execSQL(
                "CREATE TABLE IF NOT EXISTS deposits_new " +
                "(currency TEXT NOT NULL, address TEXT NOT NULL, public_key TEXT NOT NULL, " +
                "payment_id TEXT NOT NULL, destination_tag TEXT NOT NULL, PRIMARY KEY(currency))"
            )
            execSQL(
                "INSERT INTO deposits_new " +
                "(currency, address, public_key, payment_id, destination_tag) " +
                "SELECT currency, address, public_key, payment_id, \"\" FROM deposits"
            )
            execSQL("DROP TABLE deposits")
            execSQL("ALTER TABLE deposits_new RENAME TO deposits")
        }
    }

}


private val MIGRATION_19_20: Migration = object : Migration(19, 20) {

    override fun migrate(database: SupportSQLiteDatabase) {
        database.execSQL(
            "CREATE TABLE IF NOT EXISTS orderbook " +
            "(market_name TEXT NOT NULL, buy_orders TEXT NOT NULL, " +
            "sell_orders TEXT NOT NULL, PRIMARY KEY(market_name))"
        )
    }

}


private val MIGRATION_20_21: Migration = object : Migration(20, 21) {

    override fun migrate(database: SupportSQLiteDatabase) {
        database.execSQL("ALTER TABLE charts_data RENAME TO price_chart_data")
    }

}


private val MIGRATION_21_22: Migration = object : Migration(21, 22) {

    override fun migrate(database: SupportSQLiteDatabase) {
        with(database) {
            execSQL("DROP TABLE IF EXISTS alerts")
            execSQL("DROP TABLE IF EXISTS coins")
            execSQL("DROP TABLE IF EXISTS tickets")
            execSQL("DROP TABLE IF EXISTS currency_market_summaries")

            execSQL(
                "CREATE TABLE IF NOT EXISTS settings_new " +
                "(id INTEGER NOT NULL, is_sound_enabled INTEGER NOT NULL, is_vibration_enabled INTEGER NOT NULL, " +
                "is_phone_led_enabled INTEGER NOT NULL, is_grouping_enabled INTEGER NOT NULL, " +
                "is_fingerprint_unlock_enabled INTEGER NOT NULL, " +
                "is_force_authentication_on_app_startup_is_enabled INTEGER NOT NULL, notification_ringtone TEXT NOT NULL, " +
                "pin_code TEXT NOT NULL, decimal_separator TEXT NOT NULL, grouping_separator TEXT NOT NULL, " +
                "session_expiration_timeout TEXT NOT NULL, theme_id INTEGER NOT NULL, PRIMARY KEY(id))"
            )
            execSQL(
                "INSERT INTO settings_new " +
                "(id, is_sound_enabled, is_vibration_enabled, is_phone_led_enabled, is_grouping_enabled, " +
                "is_fingerprint_unlock_enabled, is_force_authentication_on_app_startup_is_enabled, notification_ringtone, " +
                "pin_code, decimal_separator, grouping_separator, session_expiration_timeout, theme_id) " +
                "SELECT id, is_sound_enabled, is_vibration_enabled, is_phone_led_enabled, is_grouping_enabled, " +
                "is_fingerprint_unlock_enabled, is_force_authentication_on_app_startup_is_enabled, notification_ringtone, " +
                "pin_code, decimal_separator, grouping_separator, session_expiration_timeout, theme_id FROM settings"
            )
            execSQL("DROP TABLE settings")
            execSQL("ALTER TABLE settings_new RENAME TO settings")
        }
    }

}


private val MIGRATION_22_23: Migration = object : Migration(22, 23) {

    override fun migrate(database: SupportSQLiteDatabase) {
        with(database) {
            execSQL(
                "CREATE TABLE IF NOT EXISTS currency_markets_new " +
                "(id INTEGER NOT NULL, name TEXT NOT NULL, currency_name TEXT NOT NULL, " +
                "market_name TEXT NOT NULL, currency_symbol TEXT NOT NULL, market_symbol TEXT NOT NULL, " +
                "min_order_amount REAL NOT NULL, daily_buy_max_price REAL NOT NULL, " +
                "daily_sell_min_price REAL NOT NULL, last_price REAL NOT NULL, " +
                "last_price_day_ago REAL NOT NULL, daily_volume REAL NOT NULL, spread REAL NOT NULL, " +
                "buy_fee_percent REAL NOT NULL, sell_fee_percent REAL NOT NULL, is_active INTEGER NOT NULL, " +
                "PRIMARY KEY(id))"
            )
            execSQL(
                "INSERT INTO currency_markets_new " +
                "(id, name, currency_name, market_name, currency_symbol, market_symbol, min_order_amount, " +
                "daily_buy_max_price, daily_sell_min_price, last_price, last_price_day_ago, daily_volume, spread, " +
                "buy_fee_percent, sell_fee_percent, is_active) SELECT id, name, \"\", \"\", currency_symbol, " +
                "market_symbol, min_order_amount, daily_buy_max_price, daily_sell_min_price, last_price, " +
                "last_price_day_ago, daily_volume, spread, buy_fee_percent, sell_fee_percent, is_active FROM currency_markets"
            )
            execSQL("DROP TABLE currency_markets")
            execSQL("ALTER TABLE currency_markets_new RENAME TO currency_markets")
        }
    }

}


private val MIGRATION_23_24: Migration = object : Migration(23, 24) {

    override fun migrate(database: SupportSQLiteDatabase) {
        with(database) {
            execSQL(
                "CREATE TABLE IF NOT EXISTS currency_markets_new " +
                "(id INTEGER NOT NULL, name TEXT NOT NULL, base_currency_name TEXT NOT NULL, " +
                "quote_currency_name TEXT NOT NULL, base_currency_symbol TEXT NOT NULL, quote_currency_symbol TEXT NOT NULL, " +
                "min_order_amount REAL NOT NULL, daily_buy_max_price REAL NOT NULL, " +
                "daily_sell_min_price REAL NOT NULL, last_price REAL NOT NULL, " +
                "last_price_day_ago REAL NOT NULL, daily_volume REAL NOT NULL, spread REAL NOT NULL, " +
                "buy_fee_percent REAL NOT NULL, sell_fee_percent REAL NOT NULL, is_active INTEGER NOT NULL, " +
                "PRIMARY KEY(id))"
            )
            execSQL(
                "INSERT INTO currency_markets_new " +
                "(id, name, base_currency_name, quote_currency_name, base_currency_symbol, quote_currency_symbol, min_order_amount, " +
                "daily_buy_max_price, daily_sell_min_price, last_price, last_price_day_ago, daily_volume, spread, " +
                "buy_fee_percent, sell_fee_percent, is_active) SELECT id, name, currency_name, market_name, currency_symbol, " +
                "market_symbol, min_order_amount, daily_buy_max_price, daily_sell_min_price, last_price, " +
                "last_price_day_ago, daily_volume, spread, buy_fee_percent, sell_fee_percent, is_active FROM currency_markets"
            )
            execSQL("DROP TABLE currency_markets")
            execSQL("ALTER TABLE currency_markets_new RENAME TO currency_markets")
        }
    }

}


private val MIGRATION_24_25: Migration = object : Migration(24, 25) {

    override fun migrate(database: SupportSQLiteDatabase) {
        with(database) {
            execSQL(
                "CREATE TABLE IF NOT EXISTS orders_new " +
                "(id INTEGER NOT NULL, market_name TEXT NOT NULL, base_currency_symbol TEXT NOT NULL, " +
                "quote_currency_symbol TEXT NOT NULL, type TEXT NOT NULL, trade_type TEXT NOT NULL, " +
                "amount REAL NOT NULL, original_amount REAL NOT NULL, buy_amount REAL NOT NULL, " +
                "sell_amount REAL NOT NULL, rate REAL NOT NULL, rates_map TEXT NOT NULL, timestamp INTEGER NOT NULL, " +
                "PRIMARY KEY(id, type))"
            )
            execSQL(
                "INSERT INTO orders_new " +
                "(id, market_name, base_currency_symbol, quote_currency_symbol, type, trade_type, amount, " +
                "original_amount, buy_amount, sell_amount, rate, rates_map, timestamp) SELECT " +
                "id, market_name, currency_symbol, market_symbol, type, trade_type, amount, " +
                "original_amount, buy_amount, sell_amount, rate, rates_map, timestamp FROM orders"
            )
            execSQL("DROP TABLE orders")
            execSQL("ALTER TABLE orders_new RENAME TO orders")
        }
    }

}


private val MIGRATION_25_26: Migration = object : Migration(25, 26) {

    override fun migrate(database: SupportSQLiteDatabase) {
        with(database) {
            execSQL(
                "CREATE TABLE IF NOT EXISTS currency_markets_new " +
                "(id INTEGER NOT NULL, name TEXT NOT NULL, base_currency_name TEXT NOT NULL, " +
                "quote_currency_name TEXT NOT NULL, base_currency_symbol TEXT NOT NULL, " +
                "quote_currency_symbol TEXT NOT NULL, min_order_amount REAL NOT NULL, " +
                "daily_min_price REAL NOT NULL, daily_max_price REAL NOT NULL, last_price REAL NOT NULL, " +
                "last_price_day_ago REAL NOT NULL, daily_volume REAL NOT NULL, spread REAL NOT NULL, " +
                "buy_fee_percent REAL NOT NULL, sell_fee_percent REAL NOT NULL, is_active INTEGER NOT NULL, " +
                "PRIMARY KEY(id))"
            )
            execSQL(
                "INSERT INTO currency_markets_new " +
                "(id, name, base_currency_name, quote_currency_name, base_currency_symbol, " +
                "quote_currency_symbol, min_order_amount, daily_min_price, daily_max_price, last_price, " +
                "last_price_day_ago, daily_volume, spread, buy_fee_percent, sell_fee_percent, is_active) " +
                "SELECT id, name, base_currency_name, quote_currency_name, base_currency_symbol, " +
                "quote_currency_symbol, min_order_amount, daily_sell_min_price, daily_buy_max_price, last_price, " +
                "last_price_day_ago, daily_volume, spread, buy_fee_percent, sell_fee_percent, is_active FROM currency_markets"
            )
            execSQL("DROP TABLE currency_markets")
            execSQL("ALTER TABLE currency_markets_new RENAME TO currency_markets")
        }
    }

}


private val MIGRATION_26_27: Migration = object : Migration(26, 27) {

    override fun migrate(database: SupportSQLiteDatabase) {
        with(database) {
            execSQL(
                "CREATE TABLE IF NOT EXISTS currency_markets_new " +
                "(id INTEGER NOT NULL, name TEXT NOT NULL, base_currency_name TEXT NOT NULL, " +
                "quote_currency_name TEXT NOT NULL, base_currency_symbol TEXT NOT NULL, " +
                "quote_currency_symbol TEXT NOT NULL, min_order_amount REAL NOT NULL, " +
                "daily_min_price REAL NOT NULL, daily_max_price REAL NOT NULL, last_price REAL NOT NULL, " +
                "last_price_day_ago REAL NOT NULL, daily_price_change REAL NOT NULL, daily_volume REAL NOT NULL, " +
                "volume REAL NOT NULL, buy_fee_percent REAL NOT NULL, sell_fee_percent REAL NOT NULL, " +
                "is_active INTEGER NOT NULL, PRIMARY KEY(id))"
            )
            execSQL(
                "INSERT INTO currency_markets_new " +
                "(id, name, base_currency_name, quote_currency_name, base_currency_symbol, " +
                "quote_currency_symbol, min_order_amount, daily_min_price, daily_max_price, last_price, " +
                "last_price_day_ago, daily_price_change, daily_volume, volume, buy_fee_percent, sell_fee_percent, " +
                "is_active) SELECT id, name, base_currency_name, quote_currency_name, base_currency_symbol, " +
                "quote_currency_symbol, min_order_amount, daily_min_price, daily_max_price, last_price, " +
                "last_price_day_ago, spread, daily_volume, 0.0, buy_fee_percent, sell_fee_percent, is_active " +
                "FROM currency_markets"
            )
            execSQL("DROP TABLE currency_markets")
            execSQL("ALTER TABLE currency_markets_new RENAME TO currency_markets")
        }
    }

}


private val MIGRATION_27_28: Migration = object : Migration(27, 28) {

    override fun migrate(database: SupportSQLiteDatabase) {
        database.execSQL(
            "CREATE TABLE IF NOT EXISTS trades " +
            "(id INTEGER NOT NULL, market_name TEXT NOT NULL, type TEXT NOT NULL, " +
            "timestamp INTEGER NOT NULL, price REAL NOT NULL, amount REAL NOT NULL, " +
            "PRIMARY KEY(id))"
        )
    }

}


private val MIGRATION_28_29: Migration = object : Migration(28, 29) {

    override fun migrate(database: SupportSQLiteDatabase) {
        with(database) {
            execSQL(
                "CREATE TABLE IF NOT EXISTS settings_new " +
                "(id INTEGER NOT NULL, is_sound_enabled INTEGER NOT NULL, is_vibration_enabled INTEGER NOT NULL, " +
                "is_phone_led_enabled INTEGER NOT NULL, is_grouping_enabled INTEGER NOT NULL, " +
                "is_fingerprint_unlock_enabled INTEGER NOT NULL, " +
                "is_force_authentication_on_app_startup_is_enabled INTEGER NOT NULL, " +
                "is_real_time_data_streaming_enabled INTEGER NOT NULL, " +
                "is_orderbook_real_time_updates_highlighting_enabled INTEGER NOT NULL, " +
                "is_new_trades_real_time_addition_highlighting_enabled INTEGER NOT NULL, " +
                "notification_ringtone TEXT NOT NULL, pin_code TEXT NOT NULL, decimal_separator TEXT NOT NULL, " +
                "grouping_separator TEXT NOT NULL, session_expiration_timeout TEXT NOT NULL, " +
                "theme_id INTEGER NOT NULL, PRIMARY KEY(id))"
            )
            execSQL(
                "INSERT INTO settings_new " +
                "(id, is_sound_enabled, is_vibration_enabled, is_phone_led_enabled, is_grouping_enabled, " +
                "is_fingerprint_unlock_enabled, is_force_authentication_on_app_startup_is_enabled, " +
                "is_real_time_data_streaming_enabled, is_orderbook_real_time_updates_highlighting_enabled, " +
                "is_new_trades_real_time_addition_highlighting_enabled, notification_ringtone, pin_code, " +
                "decimal_separator, grouping_separator, session_expiration_timeout, theme_id) SELECT id, " +
                "is_sound_enabled, is_vibration_enabled, is_phone_led_enabled, is_grouping_enabled, " +
                "is_fingerprint_unlock_enabled, is_force_authentication_on_app_startup_is_enabled, 1, 1, 1, " +
                "notification_ringtone, pin_code, decimal_separator, grouping_separator, session_expiration_timeout, " +
                "theme_id FROM settings"
            )
            execSQL("DROP TABLE settings")
            execSQL("ALTER TABLE settings_new RENAME TO settings")
        }
    }

}


private val MIGRATION_29_30: Migration = object : Migration(29, 30) {

    override fun migrate(database: SupportSQLiteDatabase) {
        with(database) {
            execSQL(
                "CREATE TABLE IF NOT EXISTS settings_new " +
                "(id INTEGER NOT NULL, is_sound_enabled INTEGER NOT NULL, is_vibration_enabled INTEGER NOT NULL, " +
                "is_phone_led_enabled INTEGER NOT NULL, is_grouping_enabled INTEGER NOT NULL, " +
                "is_fingerprint_unlock_enabled INTEGER NOT NULL, " +
                "is_force_authentication_on_app_startup_is_enabled INTEGER NOT NULL, " +
                "is_real_time_data_streaming_enabled INTEGER NOT NULL, " +
                "is_orderbook_real_time_updates_highlighting_enabled INTEGER NOT NULL, " +
                "is_new_trades_real_time_addition_highlighting_enabled INTEGER NOT NULL, " +
                "should_animate_charts INTEGER NOT NULL, notification_ringtone TEXT NOT NULL, " +
                "pin_code TEXT NOT NULL, bullish_candle_stick_style TEXT NOT NULL, " +
                "bearish_candle_stick_style TEXT NOT NULL, depth_chart_line_style TEXT NOT NULL, " +
                "decimal_separator TEXT NOT NULL, grouping_separator TEXT NOT NULL, " +
                "session_expiration_timeout TEXT NOT NULL, theme_id INTEGER NOT NULL, PRIMARY KEY(id))"
            )
            execSQL(
                "INSERT INTO settings_new " +
                "(id, is_sound_enabled, is_vibration_enabled, is_phone_led_enabled, is_grouping_enabled, " +
                "is_fingerprint_unlock_enabled, is_force_authentication_on_app_startup_is_enabled, " +
                "is_real_time_data_streaming_enabled, is_orderbook_real_time_updates_highlighting_enabled, " +
                "is_new_trades_real_time_addition_highlighting_enabled, should_animate_charts, " +
                "notification_ringtone, pin_code, bullish_candle_stick_style, bearish_candle_stick_style," +
                "depth_chart_line_style, decimal_separator, grouping_separator, session_expiration_timeout, " +
                "theme_id) SELECT id, is_sound_enabled, is_vibration_enabled, is_phone_led_enabled, is_grouping_enabled, " +
                "is_fingerprint_unlock_enabled, is_force_authentication_on_app_startup_is_enabled, " +
                "is_real_time_data_streaming_enabled, is_orderbook_real_time_updates_highlighting_enabled, " +
                "is_new_trades_real_time_addition_highlighting_enabled, 1, notification_ringtone, pin_code, " +
                "\"SOLID\", \"SOLID\", \"LINEAR\", decimal_separator, grouping_separator, session_expiration_timeout, " +
                "theme_id FROM settings"
            )
            execSQL("DROP TABLE settings")
            execSQL("ALTER TABLE settings_new RENAME TO settings")
        }
    }

}


private val MIGRATION_30_31: Migration = object : Migration(30, 31) {

    override fun migrate(database: SupportSQLiteDatabase) {
        with(database) {
            execSQL(
                "CREATE TABLE IF NOT EXISTS settings_new " +
                "(id INTEGER NOT NULL, is_sound_enabled INTEGER NOT NULL, is_vibration_enabled INTEGER NOT NULL, " +
                "is_phone_led_enabled INTEGER NOT NULL, is_grouping_enabled INTEGER NOT NULL, " +
                "is_fingerprint_unlock_enabled INTEGER NOT NULL, " +
                "is_force_authentication_on_app_startup_is_enabled INTEGER NOT NULL, " +
                "is_real_time_data_streaming_enabled INTEGER NOT NULL, " +
                "is_orderbook_real_time_updates_highlighting_enabled INTEGER NOT NULL, " +
                "is_new_trades_real_time_addition_highlighting_enabled INTEGER NOT NULL, " +
                "should_animate_charts INTEGER NOT NULL, notification_ringtone TEXT NOT NULL, " +
                "pin_code TEXT NOT NULL, bullish_candle_stick_style TEXT NOT NULL, " +
                "bearish_candle_stick_style TEXT NOT NULL, depth_chart_line_style TEXT NOT NULL, " +
                "decimal_separator TEXT NOT NULL, grouping_separator TEXT NOT NULL, " +
                "session_expiration_timeout TEXT NOT NULL, theme_id INTEGER NOT NULL, PRIMARY KEY(id))"
            )
            execSQL(
                "INSERT INTO settings_new " +
                "(id, is_sound_enabled, is_vibration_enabled, is_phone_led_enabled, is_grouping_enabled, " +
                "is_fingerprint_unlock_enabled, is_force_authentication_on_app_startup_is_enabled, " +
                "is_real_time_data_streaming_enabled, is_orderbook_real_time_updates_highlighting_enabled, " +
                "is_new_trades_real_time_addition_highlighting_enabled, should_animate_charts, " +
                "notification_ringtone, pin_code, bullish_candle_stick_style, bearish_candle_stick_style, " +
                "depth_chart_line_style, decimal_separator, grouping_separator, session_expiration_timeout, " +
                "theme_id) SELECT id, is_sound_enabled, is_vibration_enabled, is_phone_led_enabled, is_grouping_enabled, " +
                "is_fingerprint_unlock_enabled, is_force_authentication_on_app_startup_is_enabled, " +
                "is_real_time_data_streaming_enabled, is_orderbook_real_time_updates_highlighting_enabled, " +
                "is_new_trades_real_time_addition_highlighting_enabled, should_animate_charts, notification_ringtone, pin_code, " +
                "bullish_candle_stick_style, bearish_candle_stick_style, depth_chart_line_style, decimal_separator, " +
                "CASE WHEN grouping_separator=\"UNDERSCORE\" THEN \"SPACE\" ELSE grouping_separator END, " +
                "session_expiration_timeout, theme_id FROM settings"
            )
            execSQL("DROP TABLE settings")
            execSQL("ALTER TABLE settings_new RENAME TO settings")
        }
    }

}


private val MIGRATION_31_32: Migration = object : Migration(31, 32) {

    override fun migrate(database: SupportSQLiteDatabase) {
        with(database) {
            execSQL(
                "CREATE TABLE IF NOT EXISTS settings_new " +
                "(id INTEGER NOT NULL, is_sound_enabled INTEGER NOT NULL, is_vibration_enabled INTEGER NOT NULL, " +
                "is_phone_led_enabled INTEGER NOT NULL, is_grouping_enabled INTEGER NOT NULL, " +
                "is_fingerprint_unlock_enabled INTEGER NOT NULL, " +
                "is_force_authentication_on_app_startup_is_enabled INTEGER NOT NULL, " +
                "is_real_time_data_streaming_enabled INTEGER NOT NULL, " +
                "is_orderbook_real_time_updates_highlighting_enabled INTEGER NOT NULL, " +
                "is_new_trades_real_time_addition_highlighting_enabled INTEGER NOT NULL, " +
                "should_animate_charts INTEGER NOT NULL, notification_ringtone TEXT NOT NULL, " +
                "pin_code TEXT NOT NULL, bullish_candle_stick_style TEXT NOT NULL, " +
                "bearish_candle_stick_style TEXT NOT NULL, depth_chart_line_style TEXT NOT NULL, " +
                "decimal_separator TEXT NOT NULL, grouping_separator TEXT NOT NULL, " +
                "authentication_session_duration TEXT NOT NULL, theme_id INTEGER NOT NULL, PRIMARY KEY(id))"
            )
            execSQL(
                "INSERT INTO settings_new " +
                "(id, is_sound_enabled, is_vibration_enabled, is_phone_led_enabled, is_grouping_enabled, " +
                "is_fingerprint_unlock_enabled, is_force_authentication_on_app_startup_is_enabled, " +
                "is_real_time_data_streaming_enabled, is_orderbook_real_time_updates_highlighting_enabled, " +
                "is_new_trades_real_time_addition_highlighting_enabled, should_animate_charts, " +
                "notification_ringtone, pin_code, bullish_candle_stick_style, bearish_candle_stick_style, " +
                "depth_chart_line_style, decimal_separator, grouping_separator, authentication_session_duration, " +
                "theme_id) SELECT id, is_sound_enabled, is_vibration_enabled, is_phone_led_enabled, is_grouping_enabled, " +
                "is_fingerprint_unlock_enabled, is_force_authentication_on_app_startup_is_enabled, " +
                "is_real_time_data_streaming_enabled, is_orderbook_real_time_updates_highlighting_enabled, " +
                "is_new_trades_real_time_addition_highlighting_enabled, should_animate_charts, notification_ringtone, pin_code, " +
                "bullish_candle_stick_style, bearish_candle_stick_style, depth_chart_line_style, decimal_separator, " +
                "grouping_separator, session_expiration_timeout, theme_id FROM settings"
            )
            execSQL("DROP TABLE settings")
            execSQL("ALTER TABLE settings_new RENAME TO settings")
        }
    }

}


private val MIGRATION_32_33: Migration = object : Migration(32, 33) {

    override fun migrate(database: SupportSQLiteDatabase) {
        with(database) {
            execSQL(
                "CREATE TABLE IF NOT EXISTS settings_new " +
                "(id INTEGER NOT NULL, is_sound_enabled INTEGER NOT NULL, is_vibration_enabled INTEGER NOT NULL, " +
                "is_phone_led_enabled INTEGER NOT NULL, is_grouping_enabled INTEGER NOT NULL, " +
                "is_fingerprint_unlock_enabled INTEGER NOT NULL, " +
                "is_force_authentication_on_app_startup_is_enabled INTEGER NOT NULL, " +
                "is_real_time_data_streaming_enabled INTEGER NOT NULL, " +
                "is_orderbook_real_time_updates_highlighting_enabled INTEGER NOT NULL, " +
                "is_new_trades_real_time_addition_highlighting_enabled INTEGER NOT NULL, " +
                "is_price_chart_zoom_in_enabled INTEGER NOT NULL, should_animate_charts INTEGER NOT NULL, " +
                "notification_ringtone TEXT NOT NULL, pin_code TEXT NOT NULL, " +
                "bullish_candle_stick_style TEXT NOT NULL, bearish_candle_stick_style TEXT NOT NULL, " +
                "depth_chart_line_style TEXT NOT NULL, decimal_separator TEXT NOT NULL, " +
                "grouping_separator TEXT NOT NULL, authentication_session_duration TEXT NOT NULL, " +
                "theme_id INTEGER NOT NULL, PRIMARY KEY(id))"
            )
            execSQL(
                "INSERT INTO settings_new " +
                "(id, is_sound_enabled, is_vibration_enabled, is_phone_led_enabled, is_grouping_enabled, " +
                "is_fingerprint_unlock_enabled, is_force_authentication_on_app_startup_is_enabled, " +
                "is_real_time_data_streaming_enabled, is_orderbook_real_time_updates_highlighting_enabled, " +
                "is_new_trades_real_time_addition_highlighting_enabled, is_price_chart_zoom_in_enabled, " +
                "should_animate_charts, notification_ringtone, pin_code, bullish_candle_stick_style, " +
                "bearish_candle_stick_style, depth_chart_line_style, decimal_separator, grouping_separator, " +
                "authentication_session_duration, theme_id) SELECT id, is_sound_enabled, is_vibration_enabled, " +
                "is_phone_led_enabled, is_grouping_enabled, is_fingerprint_unlock_enabled, " +
                "is_force_authentication_on_app_startup_is_enabled, is_real_time_data_streaming_enabled, " +
                "is_orderbook_real_time_updates_highlighting_enabled, " +
                "is_new_trades_real_time_addition_highlighting_enabled, 0, should_animate_charts, notification_ringtone, " +
                "pin_code, bullish_candle_stick_style, bearish_candle_stick_style, depth_chart_line_style, " +
                "decimal_separator, grouping_separator, authentication_session_duration, theme_id FROM settings"
            )
            execSQL("DROP TABLE settings")
            execSQL("ALTER TABLE settings_new RENAME TO settings")
        }
    }

}


private val MIGRATION_33_34: Migration = object : Migration(33, 34) {

    override fun migrate(database: SupportSQLiteDatabase) {
        with(database) {
            // Candle sticks
            execSQL(
                "CREATE TABLE IF NOT EXISTS candle_sticks " +
                "(currency_pair_id INTEGER NOT NULL, interval TEXT NOT NULL, open_price REAL NOT NULL, " +
                "high_price REAL NOT NULL, low_price REAL NOT NULL, close_price REAL NOT NULL, volume REAL NOT NULL, " +
                "timestamp INTEGER NOT NULL, PRIMARY KEY(currency_pair_id, interval, timestamp))"
            )

            // New currencies
            execSQL(
                "CREATE TABLE IF NOT EXISTS new_currencies " +
                "(id INTEGER NOT NULL, code TEXT NOT NULL, name TEXT NOT NULL, is_active INTEGER NOT NULL, " +
                "is_delisted INTEGER NOT NULL, precision INTEGER NOT NULL, minimum_withdrawal_amount REAL NOT NULL, " +
                "minimum_deposit_amount REAL NOT NULL, deposit_fee_currency_id INTEGER NOT NULL, deposit_fee REAL NOT NULL, " +
                "deposit_fee_in_percentage REAL NOT NULL, withdrawal_fee_currency_id INTEGER NOT NULL, withdrawal_fee REAL NOT NULL, " +
                "withdrawal_fee_in_percentage REAL NOT NULL, block_explorer_url TEXT NOT NULL, PRIMARY KEY(id))"
            )

            // Currency pairs
            execSQL(
                "CREATE TABLE IF NOT EXISTS currency_pairs " +
                "(id INTEGER NOT NULL, base_currency_id INTEGER NOT NULL, base_currency_code TEXT NOT NULL, " +
                "base_currency_name TEXT NOT NULL, quote_currency_id INTEGER NOT NULL, quote_currency_code TEXT NOT NULL, " +
                "quote_currency_name TEXT NOT NULL, symbol TEXT NOT NULL, group_name TEXT NOT NULL, group_id INTEGER NOT NULL, " +
                "min_order_amount REAL NOT NULL, min_buy_price REAL NOT NULL, min_sell_price REAL NOT NULL, " +
                "buy_fee_in_percentage REAL NOT NULL, sell_fee_in_percentage REAL NOT NULL, is_active INTEGER NOT NULL, " +
                "is_delisted INTEGER NOT NULL, message TEXT, base_currency_precision INTEGER NOT NULL, " +
                "quote_currency_precision INTEGER NOT NULL, PRIMARY KEY(id))"
            )

            // New deposits
            execSQL(
                "CREATE TABLE IF NOT EXISTS new_deposits " +
                "(id INTEGER NOT NULL, currency_id INTEGER NOT NULL, currency_code TEXT NOT NULL, amount REAL NOT NULL, " +
                "fee REAL NOT NULL, status_str TEXT NOT NULL, timestamp INTEGER NOT NULL, transaction_id TEXT, " +
                "confirmations TEXT NOT NULL, PRIMARY KEY(id))"
            )

            // Orderbooks
            execSQL(
                "CREATE TABLE IF NOT EXISTS orderbooks " +
                "(currency_pair_id INTEGER NOT NULL, buy_orders TEXT NOT NULL, sell_orders TEXT NOT NULL, PRIMARY KEY(currency_pair_id))"
            )

            // New orders
            execSQL(
                "CREATE TABLE IF NOT EXISTS new_orders " +
                "(id INTEGER NOT NULL, currency_pair_id INTEGER NOT NULL, price REAL NOT NULL, trigger_price REAL NOT NULL, " +
                "initial_amount REAL NOT NULL, processed_amount REAL NOT NULL, type_str TEXT NOT NULL, original_type_str TEXT NOT NULL, " +
                "timestamp INTEGER NOT NULL, status_str TEXT NOT NULL, trades TEXT NOT NULL, fees TEXT NOT NULL, PRIMARY KEY(id))"
            )

            // Profile infos
            execSQL(
                "CREATE TABLE IF NOT EXISTS profile_infos (email TEXT NOT NULL, user_name TEXT NOT NULL, PRIMARY KEY(email))"
            )

            // Ticker items
            execSQL(
                "CREATE TABLE IF NOT EXISTS ticker_items " +
                "(id INTEGER NOT NULL, base_currency_code TEXT NOT NULL, base_currency_name TEXT NOT NULL, quote_currency_code TEXT NOT NULL, " +
                "quote_currency_name TEXT NOT NULL, symbol TEXT NOT NULL, ask_price REAL NOT NULL, bid_price REAL NOT NULL, last_price REAL, " +
                "open_price REAL, low_price REAL, high_price REAL, volume REAL, volume_quote REAL, timestamp INTEGER NOT NULL, PRIMARY KEY(id))"
            )

            // New trades
            execSQL(
                "CREATE TABLE IF NOT EXISTS new_trades " +
                "(id INTEGER NOT NULL, currency_pair_id INTEGER NOT NULL, price REAL NOT NULL, amount REAL NOT NULL, type_str TEXT NOT NULL, " +
                "timestamp INTEGER NOT NULL, PRIMARY KEY(id))"
            )

            // Wallets
            execSQL(
                "CREATE TABLE IF NOT EXISTS wallets " +
                "(id INTEGER, currency_id INTEGER NOT NULL, is_delisted INTEGER NOT NULL, is_disabled INTEGER NOT NULL, " +
                "is_depositing_disabled INTEGER NOT NULL, currency_name TEXT NOT NULL, currency_code TEXT NOT NULL, " +
                "current_balance REAL NOT NULL, frozen_balance REAL NOT NULL, bonus_balance REAL NOT NULL, total_balance REAL NOT NULL, " +
                "deposit_address TEXT, PRIMARY KEY(currency_id))"
            )

            // Withdrawals
            execSQL(
                "CREATE TABLE IF NOT EXISTS withdrawals " +
                "(id INTEGER NOT NULL, currency_id INTEGER NOT NULL, currency_code TEXT NOT NULL, amount REAL NOT NULL, fee REAL NOT NULL, " +
                "fee_currency_id INTEGER NOT NULL, fee_currency_code TEXT NOT NULL, status_str TEXT NOT NULL, creation_timestamp INTEGER NOT NULL, " +
                "update_timestamp INTEGER NOT NULL, transaction_id TEXT, address TEXT, PRIMARY KEY(id))"
            )
        }
    }

}


private val MIGRATION_34_35: Migration = object : Migration(34, 35) {

    override fun migrate(database: SupportSQLiteDatabase) {
        with(database) {
            execSQL(
                "CREATE TABLE IF NOT EXISTS profile_infos_new " +
                "(email TEXT NOT NULL, user_name TEXT NOT NULL, verifications TEXT NOT NULL, PRIMARY KEY(email))"
            )
            execSQL(
                "INSERT INTO profile_infos_new (email, user_name, verifications) SELECT email, user_name, \"\" " +
                "FROM profile_infos"
            )
            execSQL("DROP TABLE profile_infos")
            execSQL("ALTER TABLE profile_infos_new RENAME TO profile_infos")
        }
    }

}


private val MIGRATION_35_36: Migration = object : Migration(35, 36) {

    override fun migrate(database: SupportSQLiteDatabase) {
        with(database) {
            execSQL("CREATE TABLE IF NOT EXISTS favorite_currency_pairs (id INTEGER NOT NULL, PRIMARY KEY(id))")
        }
    }

}


private val MIGRATION_36_37: Migration = object : Migration(36, 37) {

    override fun migrate(database: SupportSQLiteDatabase) {
        with(database) {
            execSQL("DROP TABLE IF EXISTS currency_markets")
            execSQL("DROP TABLE IF EXISTS favorite_currency_markets")
        }
    }

}


private val MIGRATION_37_38: Migration = object : Migration(37, 38) {

    override fun migrate(database: SupportSQLiteDatabase) {
        with(database) {
            // Currency pairs
            execSQL(
                "CREATE TABLE IF NOT EXISTS currency_pairs_new " +
                "(id INTEGER NOT NULL, base_currency_id INTEGER NOT NULL, base_currency_symbol TEXT NOT NULL, " +
                "base_currency_name TEXT NOT NULL, quote_currency_id INTEGER NOT NULL, quote_currency_symbol TEXT NOT NULL, " +
                "quote_currency_name TEXT NOT NULL, name TEXT NOT NULL, group_name TEXT NOT NULL, group_id INTEGER NOT NULL, " +
                "min_order_amount REAL NOT NULL, min_buy_price REAL NOT NULL, min_sell_price REAL NOT NULL, " +
                "buy_fee_in_percentage REAL NOT NULL, sell_fee_in_percentage REAL NOT NULL, is_active INTEGER NOT NULL, " +
                "is_delisted INTEGER NOT NULL, message TEXT, base_currency_precision INTEGER NOT NULL, " +
                "quote_currency_precision INTEGER NOT NULL, PRIMARY KEY(id))"
            )
            execSQL(
                "INSERT INTO currency_pairs_new (id, base_currency_id, base_currency_symbol, base_currency_name, " +
                "quote_currency_id, quote_currency_symbol, quote_currency_name, name, group_name, group_id, min_order_amount, " +
                "min_buy_price, min_sell_price, buy_fee_in_percentage, sell_fee_in_percentage, is_active, is_delisted, message, " +
                "base_currency_precision, quote_currency_precision) SELECT id, base_currency_id, base_currency_code, base_currency_name, " +
                "quote_currency_id, quote_currency_code, quote_currency_name, symbol, group_name, group_id, min_order_amount, min_buy_price, " +
                "min_sell_price, buy_fee_in_percentage, sell_fee_in_percentage, is_active, is_delisted, message, base_currency_precision, " +
                "quote_currency_precision FROM currency_pairs"
            )
            execSQL("DROP TABLE currency_pairs")
            execSQL("ALTER TABLE currency_pairs_new RENAME TO currency_pairs")

            // Ticker items
            execSQL(
                "CREATE TABLE IF NOT EXISTS ticker_items_new (id INTEGER NOT NULL, base_currency_symbol TEXT NOT NULL, " +
                "base_currency_name TEXT NOT NULL, quote_currency_symbol TEXT NOT NULL, quote_currency_name TEXT NOT NULL, name TEXT NOT NULL, " +
                "ask_price REAL NOT NULL, bid_price REAL NOT NULL, last_price REAL, open_price REAL, low_price REAL, high_price REAL, volume REAL, " +
                "volume_quote REAL, timestamp INTEGER NOT NULL, PRIMARY KEY(id))"
            )
            execSQL(
                "INSERT INTO ticker_items_new (id, base_currency_symbol, base_currency_name, quote_currency_symbol, quote_currency_name, " +
                "name, ask_price, bid_price, last_price, open_price, low_price, high_price, volume, volume_quote, timestamp) SELECT " +
                "id, base_currency_code, base_currency_name, quote_currency_code, quote_currency_name, symbol, ask_price, bid_price, last_price, " +
                "open_price, low_price, high_price, volume, volume_quote, timestamp FROM ticker_items"
            )
            execSQL("DROP TABLE ticker_items")
            execSQL("ALTER TABLE ticker_items_new RENAME TO ticker_items")
        }
    }

}


private val MIGRATION_38_39: Migration = object : Migration(38, 39) {

    override fun migrate(database: SupportSQLiteDatabase) {
        with(database) {
            execSQL("DROP TABLE IF EXISTS trades")
        }
    }

}


private val MIGRATION_39_40: Migration = object : Migration(39, 40) {

    override fun migrate(database: SupportSQLiteDatabase) {
        with(database) {
            execSQL("DROP TABLE IF EXISTS orderbook")
        }
    }

}


private val MIGRATION_40_41: Migration = object : Migration(40, 41) {

    override fun migrate(database: SupportSQLiteDatabase) {
        with(database) {
            execSQL("DROP TABLE IF EXISTS price_chart_data")
        }
    }

}


private val MIGRATION_41_42: Migration = object : Migration(41, 42) {

    override fun migrate(database: SupportSQLiteDatabase) {
        with(database) {
            execSQL(
                "CREATE TABLE IF NOT EXISTS candle_sticks_new (currency_pair_id INTEGER NOT NULL, interval_name TEXT NOT NULL, open_price REAL NOT NULL, " +
                "high_price REAL NOT NULL, low_price REAL NOT NULL, close_price REAL NOT NULL, volume REAL NOT NULL, timestamp INTEGER NOT NULL, " +
                "PRIMARY KEY(currency_pair_id, interval_name, timestamp))"
            )
            execSQL(
                "INSERT INTO candle_sticks_new (currency_pair_id, interval_name, open_price, high_price, low_price, close_price, volume, timestamp) " +
                "SELECT currency_pair_id, interval, open_price, high_price, low_price, close_price, volume, timestamp FROM candle_sticks"
            )
            execSQL("DROP TABLE candle_sticks")
            execSQL("ALTER TABLE candle_sticks_new RENAME TO candle_sticks")
        }
    }

}


private val MIGRATION_42_43: Migration = object : Migration(42, 43) {

    override fun migrate(database: SupportSQLiteDatabase) {
        with(database) {
            execSQL("DROP TABLE IF EXISTS orders")

            // Orders
            execSQL(
                "CREATE TABLE IF NOT EXISTS orders_new (id INTEGER NOT NULL, currency_pair_id INTEGER NOT NULL, " +
                "price REAL NOT NULL, trigger_price REAL NOT NULL, initial_amount REAL NOT NULL, processed_amount REAL NOT NULL, " +
                "type_str TEXT NOT NULL, original_type_str TEXT NOT NULL, timestamp INTEGER NOT NULL, status_str TEXT NOT NULL, " +
                "trades TEXT NOT NULL, fees TEXT NOT NULL, PRIMARY KEY(id))"
            )
            execSQL(
                "INSERT INTO orders_new (id, currency_pair_id, price, trigger_price, initial_amount, processed_amount, " +
                "type_str, original_type_str, timestamp, status_str, trades, fees) SELECT id, currency_pair_id, price, " +
                "trigger_price, initial_amount, processed_amount, type_str, original_type_str, timestamp, status_str, trades, fees " +
                "FROM new_orders"
            )
            execSQL("DROP TABLE IF EXISTS new_orders")
            execSQL("ALTER TABLE orders_new RENAME TO orders")

            // Trades
            execSQL(
                "CREATE TABLE IF NOT EXISTS trades_new (id INTEGER NOT NULL, currency_pair_id INTEGER NOT NULL, price REAL NOT NULL, " +
                "amount REAL NOT NULL, type_str TEXT NOT NULL, timestamp INTEGER NOT NULL, PRIMARY KEY(id))"
            )
            execSQL(
                "INSERT INTO trades_new (id, currency_pair_id, price, amount, type_str, timestamp) SELECT id, currency_pair_id, price, " +
                "amount, type_str, timestamp FROM new_trades"
            )
            execSQL("DROP TABLE IF EXISTS new_trades")
            execSQL("ALTER TABLE trades_new RENAME TO trades")
        }
    }

}


private val MIGRATION_43_44: Migration = object : Migration(43, 44) {

    override fun migrate(database: SupportSQLiteDatabase) {
        with(database) {
            execSQL("DROP TABLE IF EXISTS users")
        }
    }

}


private val MIGRATION_44_45: Migration = object : Migration(44, 45) {

    override fun migrate(database: SupportSQLiteDatabase) {
        with(database) {
            execSQL(
                "CREATE TABLE IF NOT EXISTS wallets_new (id INTEGER, currency_id INTEGER NOT NULL, is_delisted INTEGER NOT NULL, " +
                "is_disabled INTEGER NOT NULL, is_depositing_disabled INTEGER NOT NULL, currency_name TEXT NOT NULL, " +
                "currency_symbol TEXT NOT NULL, current_balance REAL NOT NULL, frozen_balance REAL NOT NULL, bonus_balance REAL NOT NULL, " +
                "total_balance REAL NOT NULL, deposit_address TEXT, PRIMARY KEY(currency_id))"
            )
            execSQL(
                "INSERT INTO wallets_new (id, currency_id, is_delisted, is_disabled, is_depositing_disabled, currency_name, currency_symbol, " +
                "current_balance, frozen_balance, bonus_balance, total_balance, deposit_address) SELECT id, currency_id, is_delisted, is_disabled, " +
                "is_depositing_disabled, currency_name, currency_code, current_balance, frozen_balance, bonus_balance, total_balance, deposit_address " +
                "FROM wallets"
            )
            execSQL("DROP TABLE IF EXISTS wallets")
            execSQL("ALTER TABLE wallets_new RENAME TO wallets")
        }
    }

}


private val MIGRATION_45_46: Migration = object : Migration(45, 46) {

    override fun migrate(database: SupportSQLiteDatabase) {
        with(database) {
            // Deposits
            execSQL(
                "CREATE TABLE IF NOT EXISTS deposits_new (id INTEGER NOT NULL, currency_id INTEGER NOT NULL, " +
                "currency_symbol TEXT NOT NULL, amount REAL NOT NULL, fee REAL NOT NULL, status_str TEXT NOT NULL, " +
                "timestamp INTEGER NOT NULL, transaction_id TEXT, confirmations TEXT NOT NULL, PRIMARY KEY(id))"
            )
            execSQL(
                "INSERT INTO deposits_new (id, currency_id, currency_symbol, amount, fee, status_str, timestamp, " +
                "transaction_id, confirmations) SELECT id, currency_id, currency_code, amount, fee, status_str, timestamp, " +
                "transaction_id, confirmations FROM new_deposits"
            )
            execSQL("DROP TABLE IF EXISTS new_deposits")
            execSQL("ALTER TABLE deposits_new RENAME TO new_deposits")

            // Withdrawals
            execSQL(
                "CREATE TABLE IF NOT EXISTS new_withdrawals (id INTEGER NOT NULL, currency_id INTEGER NOT NULL, " +
                "currency_symbol TEXT NOT NULL, amount REAL NOT NULL, fee REAL NOT NULL, fee_currency_id INTEGER NOT NULL, " +
                "fee_currency_symbol TEXT NOT NULL, status_str TEXT NOT NULL, creation_timestamp INTEGER NOT NULL, " +
                "update_timestamp INTEGER NOT NULL, transaction_id TEXT, address TEXT, PRIMARY KEY(id))"
            )
            execSQL(
                "INSERT INTO new_withdrawals (id, currency_id, currency_symbol, amount, fee, fee_currency_id, " +
                "fee_currency_symbol, status_str, creation_timestamp, update_timestamp, transaction_id, address) " +
                "SELECT id, currency_id, currency_code, amount, fee, fee_currency_id, fee_currency_code, status_str, " +
                "creation_timestamp, update_timestamp, transaction_id, address FROM withdrawals"
            )
            execSQL("DROP TABLE IF EXISTS withdrawals")
            execSQL("ALTER TABLE new_withdrawals RENAME TO withdrawals")
        }
    }

}


private val MIGRATION_46_47: Migration = object : Migration(46, 47) {

    override fun migrate(database: SupportSQLiteDatabase) {
        with(database) {
            // Wallets
            execSQL(
                "CREATE TABLE IF NOT EXISTS wallets_new (id INTEGER, currency_id INTEGER NOT NULL, is_delisted INTEGER NOT NULL, " +
                "is_disabled INTEGER NOT NULL, is_depositing_disabled INTEGER NOT NULL, currency_name TEXT NOT NULL, " +
                "currency_symbol TEXT NOT NULL, current_balance REAL NOT NULL, frozen_balance REAL NOT NULL, bonus_balance REAL NOT NULL, " +
                "total_balance REAL NOT NULL, deposit_address_data TEXT, PRIMARY KEY(currency_id))"
            )
            execSQL(
                "INSERT INTO wallets_new (id, currency_id, is_delisted, is_disabled, is_depositing_disabled, currency_name, currency_symbol, " +
                "current_balance, frozen_balance, bonus_balance, total_balance, deposit_address_data) SELECT id, currency_id, is_delisted, " +
                "is_disabled, is_depositing_disabled, currency_name, currency_symbol, current_balance, frozen_balance, bonus_balance, total_balance, " +
                "deposit_address FROM wallets"
            )
            execSQL("DROP TABLE IF EXISTS wallets")
            execSQL("ALTER TABLE wallets_new RENAME TO wallets")

            // Currencies
            execSQL(
                "CREATE TABLE IF NOT EXISTS currencies_new (id INTEGER NOT NULL, symbol TEXT NOT NULL, name TEXT NOT NULL, " +
                "is_active INTEGER NOT NULL, is_delisted INTEGER NOT NULL, precision INTEGER NOT NULL, " +
                "minimum_withdrawal_amount REAL NOT NULL, minimum_deposit_amount REAL NOT NULL, " +
                "deposit_fee_currency_id INTEGER NOT NULL, deposit_fee REAL NOT NULL, deposit_fee_in_percentage REAL NOT NULL, " +
                "withdrawal_fee_currency_id INTEGER NOT NULL, withdrawal_fee REAL NOT NULL, withdrawal_fee_in_percentage REAL NOT NULL, " +
                "block_explorer_url TEXT NOT NULL, PRIMARY KEY(id))"
            )
            execSQL(
                "INSERT INTO currencies_new (id, symbol, name, is_active, is_delisted, precision, minimum_withdrawal_amount, " +
                "minimum_deposit_amount, deposit_fee_currency_id, deposit_fee, deposit_fee_in_percentage, " +
                "withdrawal_fee_currency_id, withdrawal_fee, withdrawal_fee_in_percentage, block_explorer_url) SELECT " +
                "id, code, name, is_active, is_delisted, precision, minimum_withdrawal_amount, minimum_deposit_amount, " +
                "deposit_fee_currency_id, deposit_fee, deposit_fee_in_percentage, withdrawal_fee_currency_id, " +
                "withdrawal_fee, withdrawal_fee_in_percentage, block_explorer_url FROM new_currencies"
            )
            execSQL("DROP TABLE IF EXISTS new_currencies")
            execSQL("ALTER TABLE currencies_new RENAME TO new_currencies")

            // Withdrawals
            execSQL(
                "CREATE TABLE IF NOT EXISTS new_withdrawals (id INTEGER NOT NULL, currency_id INTEGER NOT NULL, " +
                "currency_symbol TEXT NOT NULL, amount REAL NOT NULL, fee REAL NOT NULL, fee_currency_id INTEGER NOT NULL, " +
                "fee_currency_symbol TEXT NOT NULL, status_str TEXT NOT NULL, creation_timestamp INTEGER NOT NULL, " +
                "update_timestamp INTEGER NOT NULL, transaction_id TEXT, address_data TEXT NOT NULL, PRIMARY KEY(id))"
            )
            execSQL(
                "INSERT INTO new_withdrawals (id, currency_id, currency_symbol, amount, fee, fee_currency_id, " +
                "fee_currency_symbol, status_str, creation_timestamp, update_timestamp, transaction_id, address_data) " +
                "SELECT id, currency_id, currency_symbol, amount, fee, fee_currency_id, fee_currency_symbol, status_str, " +
                "creation_timestamp, update_timestamp, transaction_id, address FROM withdrawals"
            )
            execSQL("DROP TABLE IF EXISTS withdrawals")
            execSQL("ALTER TABLE new_withdrawals RENAME TO withdrawals")
        }
    }

}


private val MIGRATION_47_48: Migration = object : Migration(47, 48) {

    override fun migrate(database: SupportSQLiteDatabase) {
        with(database) {
            execSQL("DROP TABLE IF EXISTS transactions")
        }
    }

}


private val MIGRATION_48_49: Migration = object : Migration(48, 49) {

    override fun migrate(database: SupportSQLiteDatabase) {
        with(database) {
            execSQL("DROP TABLE IF EXISTS currencies")
            execSQL(
                "CREATE TABLE IF NOT EXISTS currencies (id INTEGER NOT NULL, symbol TEXT NOT NULL, name TEXT NOT NULL, " +
                "is_active INTEGER NOT NULL, is_delisted INTEGER NOT NULL, precision INTEGER NOT NULL, minimum_withdrawal_amount REAL NOT NULL, " +
                "minimum_deposit_amount REAL NOT NULL, deposit_fee_currency_id INTEGER NOT NULL, deposit_fee REAL NOT NULL, " +
                "deposit_fee_in_percentage REAL NOT NULL, withdrawal_fee_currency_id INTEGER NOT NULL, withdrawal_fee REAL NOT NULL, " +
                "withdrawal_fee_in_percentage REAL NOT NULL, block_explorer_url TEXT NOT NULL, PRIMARY KEY(id))"
            )
            execSQL(
                "INSERT INTO currencies (id, symbol, name, is_active, is_delisted, precision, minimum_withdrawal_amount, " +
                "minimum_deposit_amount, deposit_fee_currency_id, deposit_fee, deposit_fee_in_percentage, withdrawal_fee_currency_id, " +
                "withdrawal_fee, withdrawal_fee_in_percentage, block_explorer_url) SELECT id, symbol, name, is_active, " +
                "is_delisted, precision, minimum_withdrawal_amount, minimum_deposit_amount, deposit_fee_currency_id, deposit_fee, " +
                "deposit_fee_in_percentage, withdrawal_fee_currency_id, withdrawal_fee, withdrawal_fee_in_percentage, block_explorer_url " +
                "FROM new_currencies"
            )
            execSQL("DROP TABLE IF EXISTS new_currencies")
        }
    }

}


private val MIGRATION_49_50: Migration = object : Migration(49, 50) {

    override fun migrate(database: SupportSQLiteDatabase) {
        with(database) {
            execSQL(
                "CREATE TABLE IF NOT EXISTS deposits_new (id INTEGER NOT NULL, currency_id INTEGER NOT NULL, " +
                "currency_symbol TEXT NOT NULL, amount REAL NOT NULL, fee REAL NOT NULL, fee_currency_id INTEGER NOT NULL, " +
                "fee_currency_symbol TEXT NOT NULL, status_str TEXT NOT NULL, timestamp INTEGER NOT NULL, transaction_id TEXT, " +
                "confirmations TEXT NOT NULL, PRIMARY KEY(id))"
            )
            execSQL(
                "INSERT INTO deposits_new (id, currency_id, currency_symbol, amount, fee, fee_currency_id, fee_currency_symbol, " +
                "status_str, timestamp, transaction_id, confirmations) SELECT id, currency_id, currency_symbol, amount, fee, 0, " +
                "\"\", status_str, timestamp, transaction_id, confirmations FROM new_deposits"
            )
            execSQL("DROP TABLE IF EXISTS new_deposits")
            execSQL("ALTER TABLE deposits_new RENAME TO new_deposits")
        }
    }

}


private val MIGRATION_50_51: Migration = object : Migration(50, 51) {

    override fun migrate(database: SupportSQLiteDatabase) {
        with(database) {
            execSQL(
                "CREATE TABLE IF NOT EXISTS new_currencies (id INTEGER NOT NULL, symbol TEXT NOT NULL, name TEXT NOT NULL, " +
                "is_active INTEGER NOT NULL, is_delisted INTEGER NOT NULL, precision INTEGER NOT NULL, minimum_withdrawal_amount REAL NOT NULL, " +
                "minimum_deposit_amount REAL NOT NULL, deposit_fee_currency_id INTEGER NOT NULL, deposit_fee_currency_symbol TEXT NOT NULL, " +
                "deposit_fee REAL NOT NULL, deposit_fee_in_percentage REAL NOT NULL, withdrawal_fee_currency_id INTEGER NOT NULL, " +
                "withdrawal_fee_currency_symbol TEXT NOT NULL, withdrawal_fee REAL NOT NULL, withdrawal_fee_in_percentage REAL NOT NULL, " +
                "block_explorer_url TEXT NOT NULL, PRIMARY KEY(id))"
            )
            execSQL(
                "INSERT INTO new_currencies (id, symbol, name, is_active, is_delisted, precision, minimum_withdrawal_amount, " +
                "minimum_deposit_amount, deposit_fee_currency_id, deposit_fee_currency_symbol, deposit_fee, deposit_fee_in_percentage, " +
                "withdrawal_fee_currency_id, withdrawal_fee_currency_symbol, withdrawal_fee, withdrawal_fee_in_percentage, block_explorer_url) " +
                "SELECT id, symbol, name, is_active, is_delisted, precision, minimum_withdrawal_amount, minimum_deposit_amount, " +
                "deposit_fee_currency_id, \"\", deposit_fee, deposit_fee_in_percentage, withdrawal_fee_currency_id, \"\", " +
                "withdrawal_fee, withdrawal_fee_in_percentage, block_explorer_url FROM currencies"
            )
            execSQL("DROP TABLE IF EXISTS currencies")
            execSQL("ALTER TABLE new_currencies RENAME TO currencies")
        }
    }

}


private val MIGRATION_51_52: Migration = object : Migration(51, 52) {

    override fun migrate(database: SupportSQLiteDatabase) {
        with(database) {
            execSQL("DROP TABLE IF EXISTS deposits")
            execSQL(
                "CREATE TABLE IF NOT EXISTS deposits_new (id INTEGER NOT NULL, currency_id INTEGER NOT NULL, " +
                "currency_symbol TEXT NOT NULL, amount REAL NOT NULL, fee REAL NOT NULL, fee_currency_id INTEGER NOT NULL, " +
                "fee_currency_symbol TEXT NOT NULL, status_str TEXT NOT NULL, timestamp INTEGER NOT NULL, transaction_id TEXT, " +
                "confirmations TEXT NOT NULL, PRIMARY KEY(id))"
            )
            execSQL(
                "INSERT INTO deposits_new (id, currency_id, currency_symbol, amount, fee, fee_currency_id, fee_currency_symbol, " +
                "status_str, timestamp, transaction_id, confirmations) SELECT id, currency_id, currency_symbol, amount, fee, fee_currency_id, " +
                "fee_currency_symbol, status_str, timestamp, transaction_id, confirmations FROM new_deposits"
            )
            execSQL("DROP TABLE IF EXISTS new_deposits")
            execSQL("ALTER TABLE deposits_new RENAME TO deposits")
        }
    }

}


private val MIGRATION_52_53: Migration = object : Migration(52, 53) {

    override fun migrate(database: SupportSQLiteDatabase) {
        with(database) {
            execSQL(
                "CREATE TABLE IF NOT EXISTS new_ticker_items (id INTEGER NOT NULL, base_currency_symbol TEXT NOT NULL, " +
                "base_currency_name TEXT NOT NULL, quote_currency_symbol TEXT NOT NULL, quote_currency_name TEXT NOT NULL, " +
                "name TEXT NOT NULL, ask_price REAL NOT NULL, bid_price REAL NOT NULL, last_price REAL, open_price REAL, low_price REAL, " +
                "high_price REAL, daily_volume_in_base_currency REAL, daily_volume_in_quote_currency REAL, timestamp INTEGER NOT NULL, " +
                "PRIMARY KEY(id))"
            )
            execSQL(
                "INSERT INTO new_ticker_items (id, base_currency_symbol, base_currency_name, quote_currency_symbol, quote_currency_name, " +
                "name, ask_price, bid_price, last_price, open_price, low_price, high_price, daily_volume_in_base_currency, " +
                "daily_volume_in_quote_currency, timestamp) SELECT id, base_currency_symbol, base_currency_name, quote_currency_symbol, " +
                "quote_currency_name, name, ask_price, bid_price, last_price, open_price, low_price, high_price, volume, volume_quote, " +
                "timestamp FROM ticker_items"
            )
            execSQL("DROP TABLE IF EXISTS ticker_items")
            execSQL("ALTER TABLE new_ticker_items RENAME TO ticker_items")
        }
    }

}


private val MIGRATION_53_54: Migration = object : Migration(53, 54) {

    override fun migrate(database: SupportSQLiteDatabase) {
        with(database) {
            execSQL(
                "CREATE TABLE IF NOT EXISTS settings_new " +
                "(id INTEGER NOT NULL, is_sound_enabled INTEGER NOT NULL, is_vibration_enabled INTEGER NOT NULL, " +
                "is_phone_led_enabled INTEGER NOT NULL, is_grouping_enabled INTEGER NOT NULL, " +
                "is_fingerprint_unlock_enabled INTEGER NOT NULL, " +
                "is_force_authentication_on_app_startup_is_enabled INTEGER NOT NULL, " +
                "is_real_time_data_streaming_enabled INTEGER NOT NULL, " +
                "is_orderbook_real_time_updates_highlighting_enabled INTEGER NOT NULL, " +
                "is_new_trades_real_time_addition_highlighting_enabled INTEGER NOT NULL, " +
                "is_price_chart_zoom_in_enabled INTEGER NOT NULL, should_animate_charts INTEGER NOT NULL, " +
                "should_keep_screen_on INTEGER NOT NULL, notification_ringtone TEXT NOT NULL, pin_code TEXT NOT NULL, " +
                "bullish_candle_stick_style TEXT NOT NULL, bearish_candle_stick_style TEXT NOT NULL, " +
                "depth_chart_line_style TEXT NOT NULL, decimal_separator TEXT NOT NULL, " +
                "grouping_separator TEXT NOT NULL, authentication_session_duration TEXT NOT NULL, " +
                "theme_id INTEGER NOT NULL, PRIMARY KEY(id))"
            )
            execSQL(
                "INSERT INTO settings_new " +
                "(id, is_sound_enabled, is_vibration_enabled, is_phone_led_enabled, is_grouping_enabled, " +
                "is_fingerprint_unlock_enabled, is_force_authentication_on_app_startup_is_enabled, " +
                "is_real_time_data_streaming_enabled, is_orderbook_real_time_updates_highlighting_enabled, " +
                "is_new_trades_real_time_addition_highlighting_enabled, is_price_chart_zoom_in_enabled, " +
                "should_animate_charts, should_keep_screen_on, notification_ringtone, pin_code, bullish_candle_stick_style, " +
                "bearish_candle_stick_style, depth_chart_line_style, decimal_separator, grouping_separator, " +
                "authentication_session_duration, theme_id) SELECT id, is_sound_enabled, is_vibration_enabled, " +
                "is_phone_led_enabled, is_grouping_enabled, is_fingerprint_unlock_enabled, " +
                "is_force_authentication_on_app_startup_is_enabled, is_real_time_data_streaming_enabled, " +
                "is_orderbook_real_time_updates_highlighting_enabled, " +
                "is_new_trades_real_time_addition_highlighting_enabled, 0, should_animate_charts, 0, notification_ringtone, " +
                "pin_code, bullish_candle_stick_style, bearish_candle_stick_style, depth_chart_line_style, " +
                "decimal_separator, grouping_separator, authentication_session_duration, theme_id FROM settings"
            )
            execSQL("DROP TABLE settings")
            execSQL("ALTER TABLE settings_new RENAME TO settings")
        }
    }

}


private val MIGRATION_54_55: Migration = object : Migration(54, 55) {

    override fun migrate(database: SupportSQLiteDatabase) {
        with(database) {
            execSQL(
                "CREATE TABLE IF NOT EXISTS new_wallets (id INTEGER, currency_id INTEGER NOT NULL, is_delisted INTEGER NOT NULL, " +
                "is_disabled INTEGER NOT NULL, is_depositing_disabled INTEGER NOT NULL, currency_name TEXT NOT NULL, " +
                "currency_symbol TEXT NOT NULL, current_balance REAL NOT NULL, frozen_balance REAL NOT NULL, " +
                "bonus_balance REAL NOT NULL, total_balance REAL NOT NULL, deposit_address_data TEXT, " +
                "additional_withdrawal_parameter_str TEXT, PRIMARY KEY(currency_id))"
            )
            execSQL(
                "INSERT INTO new_wallets (id, currency_id, is_delisted, is_disabled, is_depositing_disabled, currency_name, " +
                "currency_symbol, current_balance, frozen_balance, bonus_balance, total_balance, deposit_address_data, " +
                "additional_withdrawal_parameter_str) SELECT id, currency_id, is_delisted, is_disabled, is_depositing_disabled, " +
                "currency_name, currency_symbol, current_balance, frozen_balance, bonus_balance, total_balance, deposit_address_data, " +
                "\"\" FROM wallets"
            )
            execSQL("DROP TABLE wallets")
            execSQL("ALTER TABLE new_wallets RENAME TO wallets")
        }
    }

}


private val MIGRATION_55_56: Migration = object : Migration(55, 56) {

    override fun migrate(database: SupportSQLiteDatabase) {
        with(database) {
            execSQL(
                "CREATE TABLE IF NOT EXISTS new_profile_infos (id INTEGER NOT NULL, email TEXT NOT NULL, user_name TEXT NOT NULL, " +
                "are_withdrawals_allowed INTEGER NOT NULL, verifications TEXT NOT NULL, trading_fee_levels TEXT NOT NULL, " +
                "PRIMARY KEY(id))"
            )
            execSQL(
                "INSERT INTO new_profile_infos (id, email, user_name, are_withdrawals_allowed, verifications, trading_fee_levels) " +
                "SELECT 1, email, user_name, 0, verifications, \"\" FROM profile_infos"
            )
            execSQL("DROP TABLE profile_infos")
            execSQL("ALTER TABLE new_profile_infos RENAME TO profile_infos")
        }
    }

}


private val MIGRATION_56_57: Migration = object : Migration(56, 57) {

    override fun migrate(database: SupportSQLiteDatabase) {
        with(database) {
            execSQL(
                "CREATE TABLE IF NOT EXISTS new_wallets (id INTEGER, currency_id INTEGER NOT NULL, is_delisted INTEGER NOT NULL, " +
                "is_disabled INTEGER NOT NULL, is_depositing_disabled INTEGER NOT NULL, currency_name TEXT NOT NULL, currency_symbol TEXT NOT NULL, " +
                "current_balance REAL NOT NULL, frozen_balance REAL NOT NULL, bonus_balance REAL NOT NULL, total_balance REAL NOT NULL, " +
                "deposit_address_data TEXT, additional_withdrawal_parameter_name TEXT, PRIMARY KEY(currency_id))"
            )
            execSQL(
                "INSERT INTO new_wallets (id, currency_id, is_delisted, is_disabled, is_depositing_disabled, currency_name, currency_symbol, " +
                "current_balance, frozen_balance, bonus_balance, total_balance, deposit_address_data, additional_withdrawal_parameter_name) " +
                "SELECT id, currency_id, is_delisted, is_disabled, is_depositing_disabled, currency_name, currency_symbol, current_balance, " +
                "frozen_balance, bonus_balance, total_balance, deposit_address_data, additional_withdrawal_parameter_str FROM wallets"
            )
            execSQL("DROP TABLE wallets")
            execSQL("ALTER TABLE new_wallets RENAME TO wallets")
        }
    }

}


private val MIGRATION_57_58: Migration = object : Migration(57, 58) {

    override fun migrate(database: SupportSQLiteDatabase) {
        with(database) {
            execSQL(
                "CREATE TABLE IF NOT EXISTS new_ticker_items (id INTEGER NOT NULL, base_currency_symbol TEXT NOT NULL, " +
                "base_currency_name TEXT NOT NULL, quote_currency_symbol TEXT NOT NULL, quote_currency_name TEXT NOT NULL, " +
                "name TEXT NOT NULL, ask_price REAL NOT NULL, bid_price REAL NOT NULL, last_price REAL, open_price REAL, " +
                "low_price REAL, high_price REAL, daily_volume_in_base_currency REAL, daily_volume_in_quote_currency REAL, " +
                "fiat_currency_rates TEXT NOT NULL, timestamp INTEGER NOT NULL, PRIMARY KEY(id))"
            )
            execSQL(
                "INSERT INTO new_ticker_items (id, base_currency_symbol, base_currency_name, quote_currency_symbol, " +
                "quote_currency_name, name, ask_price, bid_price, last_price, open_price, low_price, high_price, " +
                "daily_volume_in_base_currency, daily_volume_in_quote_currency, fiat_currency_rates, timestamp) SELECT id, " +
                "base_currency_symbol, base_currency_name, quote_currency_symbol, quote_currency_name, name, ask_price, bid_price, " +
                "last_price, open_price, low_price, high_price, daily_volume_in_base_currency, daily_volume_in_quote_currency, " +
                "\"\", timestamp FROM ticker_items"
            )
            execSQL("DROP TABLE ticker_items")
            execSQL("ALTER TABLE new_ticker_items RENAME TO ticker_items")
        }
    }

}


private val MIGRATION_58_59: Migration = object : Migration(58, 59) {

    override fun migrate(database: SupportSQLiteDatabase) {
        with(database) {
            execSQL(
                "CREATE TABLE IF NOT EXISTS new_settings (id INTEGER NOT NULL, is_sound_enabled INTEGER NOT NULL, " +
                "is_vibration_enabled INTEGER NOT NULL, is_phone_led_enabled INTEGER NOT NULL, is_grouping_enabled INTEGER NOT NULL, " +
                "is_fingerprint_unlock_enabled INTEGER NOT NULL, is_force_authentication_on_app_startup_is_enabled INTEGER NOT NULL, " +
                "is_real_time_data_streaming_enabled INTEGER NOT NULL, is_orderbook_real_time_updates_highlighting_enabled INTEGER NOT NULL, " +
                "is_new_trades_real_time_addition_highlighting_enabled INTEGER NOT NULL, is_price_chart_zoom_in_enabled INTEGER NOT NULL, " +
                "should_animate_charts INTEGER NOT NULL, should_keep_screen_on INTEGER NOT NULL, notification_ringtone TEXT NOT NULL, " +
                "pin_code TEXT NOT NULL, bullish_candle_stick_style TEXT NOT NULL, bearish_candle_stick_style TEXT NOT NULL, " +
                "depth_chart_line_style TEXT NOT NULL, decimal_separator TEXT NOT NULL, grouping_separator TEXT NOT NULL, " +
                "authentication_session_duration TEXT NOT NULL, fiat_currency TEXT NOT NULL, theme_id INTEGER NOT NULL, PRIMARY KEY(id))"
            )
            execSQL(
                "INSERT INTO new_settings (id, is_sound_enabled, is_vibration_enabled, is_phone_led_enabled, is_grouping_enabled, " +
                "is_fingerprint_unlock_enabled, is_force_authentication_on_app_startup_is_enabled, is_real_time_data_streaming_enabled, " +
                "is_orderbook_real_time_updates_highlighting_enabled, is_new_trades_real_time_addition_highlighting_enabled, " +
                "is_price_chart_zoom_in_enabled, should_animate_charts, should_keep_screen_on, notification_ringtone, pin_code, " +
                "bullish_candle_stick_style, bearish_candle_stick_style, depth_chart_line_style, decimal_separator, grouping_separator, " +
                "authentication_session_duration, fiat_currency, theme_id) SELECT id, is_sound_enabled, is_vibration_enabled, " +
                "is_phone_led_enabled, is_grouping_enabled, is_fingerprint_unlock_enabled, is_force_authentication_on_app_startup_is_enabled, " +
                "is_real_time_data_streaming_enabled, is_orderbook_real_time_updates_highlighting_enabled, " +
                "is_new_trades_real_time_addition_highlighting_enabled, is_price_chart_zoom_in_enabled, should_animate_charts, " +
                "should_keep_screen_on, notification_ringtone, pin_code, bullish_candle_stick_style, bearish_candle_stick_style, " +
                "depth_chart_line_style, decimal_separator, grouping_separator, authentication_session_duration, " +
                "\"${FiatCurrency.USD.name}\", theme_id FROM settings"
            )
            execSQL("DROP TABLE settings")
            execSQL("ALTER TABLE new_settings RENAME TO settings")
        }
    }

}


private val MIGRATION_59_60: Migration = object : Migration(59, 60) {

    override fun migrate(database: SupportSQLiteDatabase) {
        with(database) {
            execSQL(
                "CREATE TABLE IF NOT EXISTS currency_pair_groups (id INTEGER NOT NULL, name TEXT NOT NULL, position INTEGER NOT NULL, " +
                "PRIMARY KEY(id))"
            )
        }
    }

}


private val MIGRATION_60_61: Migration = object : Migration(60, 61) {

    override fun migrate(database: SupportSQLiteDatabase) {
        with(database) {
            execSQL(
                "CREATE TABLE IF NOT EXISTS new_orders (id INTEGER NOT NULL, currency_pair_id INTEGER NOT NULL, price REAL NOT NULL, " +
                "trigger_price REAL NOT NULL, initial_amount REAL NOT NULL, filled_amount REAL NOT NULL, type_str TEXT NOT NULL, " +
                "original_type_str TEXT NOT NULL, timestamp INTEGER NOT NULL, status_str TEXT NOT NULL, trades TEXT NOT NULL, " +
                "fees TEXT NOT NULL, PRIMARY KEY(id))"
            )
            execSQL(
                "INSERT INTO new_orders (id, currency_pair_id, price, trigger_price, initial_amount, filled_amount, type_str, " +
                "original_type_str, timestamp, status_str, trades, fees) SELECT id, currency_pair_id, price, trigger_price, " +
                "initial_amount, processed_amount, type_str, original_type_str, timestamp, status_str, trades, fees FROM orders"
            )
            execSQL("DROP TABLE orders")
            execSQL("ALTER TABLE new_orders RENAME TO orders")
        }
    }

}


private val MIGRATION_61_62: Migration = object : Migration(61, 62) {

    override fun migrate(database: SupportSQLiteDatabase) {
        with(database) {
            execSQL(
                "CREATE TABLE IF NOT EXISTS trading_fees (currency_pair_id INTEGER NOT NULL, buy_fee REAL NOT NULL, " +
                "sell_fee REAL NOT NULL, PRIMARY KEY(currency_pair_id))"
            )
        }
    }

}


private val MIGRATION_62_63: Migration = object : Migration(62, 63) {

    override fun migrate(database: SupportSQLiteDatabase) {
        with(database) {
            // Renaming ask_price and bid_price columns of the ticker_items table
            execSQL(
                "CREATE TABLE IF NOT EXISTS new_ticker_items (id INTEGER NOT NULL, base_currency_symbol TEXT NOT NULL, " +
                "base_currency_name TEXT NOT NULL, quote_currency_symbol TEXT NOT NULL, quote_currency_name TEXT NOT NULL, " +
                "name TEXT NOT NULL, best_ask_price REAL NOT NULL, best_bid_price REAL NOT NULL, last_price REAL, open_price REAL, " +
                "low_price REAL, high_price REAL, daily_volume_in_base_currency REAL, daily_volume_in_quote_currency REAL, " +
                "fiat_currency_rates TEXT NOT NULL, timestamp INTEGER NOT NULL, PRIMARY KEY(id))"
            )
            execSQL(
                "INSERT INTO new_ticker_items (id, base_currency_symbol, base_currency_name, quote_currency_symbol, " +
                "quote_currency_name, name, best_ask_price, best_bid_price, last_price, open_price, low_price, high_price, " +
                "daily_volume_in_base_currency, daily_volume_in_quote_currency, fiat_currency_rates, timestamp) SELECT id, " +
                "base_currency_symbol, base_currency_name, quote_currency_symbol, quote_currency_name, name, ask_price, " +
                "bid_price, last_price, open_price, low_price, high_price, daily_volume_in_base_currency, " +
                "daily_volume_in_quote_currency, fiat_currency_rates, timestamp FROM ticker_items"
            )
            execSQL("DROP TABLE ticker_items")
            execSQL("ALTER TABLE new_ticker_items RENAME TO ticker_items")

            // Renaming transaction_id column of the deposits table
            execSQL(
                "CREATE TABLE IF NOT EXISTS new_deposits (id INTEGER NOT NULL, currency_id INTEGER NOT NULL, " +
                "currency_symbol TEXT NOT NULL, amount REAL NOT NULL, fee REAL NOT NULL, fee_currency_id INTEGER NOT NULL, " +
                "fee_currency_symbol TEXT NOT NULL, status_str TEXT NOT NULL, timestamp INTEGER NOT NULL, " +
                "transaction_explorer_id TEXT, confirmations TEXT NOT NULL, PRIMARY KEY(id))"
            )
            execSQL(
                "INSERT INTO new_deposits (id, currency_id, currency_symbol, amount, fee, fee_currency_id, fee_currency_symbol, " +
                "status_str, timestamp, transaction_explorer_id, confirmations) SELECT id, currency_id, currency_symbol, amount, fee, " +
                "fee_currency_id, fee_currency_symbol, status_str, timestamp, transaction_id, confirmations FROM deposits"
            )
            execSQL("DROP TABLE deposits")
            execSQL("ALTER TABLE new_deposits RENAME TO deposits")

            // Renaming transaction_id column of the withdrawals table
            execSQL(
                "CREATE TABLE IF NOT EXISTS new_withdrawals (id INTEGER NOT NULL, currency_id INTEGER NOT NULL, " +
                "currency_symbol TEXT NOT NULL, amount REAL NOT NULL, fee REAL NOT NULL, fee_currency_id INTEGER NOT NULL, " +
                "fee_currency_symbol TEXT NOT NULL, status_str TEXT NOT NULL, creation_timestamp INTEGER NOT NULL, " +
                "update_timestamp INTEGER NOT NULL, transaction_explorer_id TEXT, address_data TEXT NOT NULL, PRIMARY KEY(id))"
            )
            execSQL(
                "INSERT INTO new_withdrawals (id, currency_id, currency_symbol, amount, fee, fee_currency_id, fee_currency_symbol, " +
                "status_str, creation_timestamp, update_timestamp, transaction_explorer_id, address_data) SELECT id, currency_id, " +
                "currency_symbol, amount, fee, fee_currency_id, fee_currency_symbol, status_str, creation_timestamp, update_timestamp, " +
                "transaction_id, address_data FROM withdrawals"
            )
            execSQL("DROP TABLE withdrawals")
            execSQL("ALTER TABLE new_withdrawals RENAME TO withdrawals")
        }
    }

}


private val MIGRATION_63_64: Migration = object : Migration(63, 64) {

    override fun migrate(database: SupportSQLiteDatabase) {
        with(database) {
            execSQL(
                "CREATE TABLE IF NOT EXISTS new_deposits (id INTEGER NOT NULL, currency_id INTEGER NOT NULL, " +
                "currency_symbol TEXT NOT NULL, amount REAL NOT NULL, fee REAL NOT NULL, " +
                "fee_currency_id INTEGER NOT NULL, fee_currency_symbol TEXT NOT NULL, " +
                "status_str TEXT NOT NULL, timestamp INTEGER NOT NULL, transaction_explorer_id TEXT, " +
                "confirmations_str TEXT NOT NULL, PRIMARY KEY(id))"
            )
            execSQL(
                "INSERT INTO new_deposits (id, currency_id, currency_symbol, amount, fee, fee_currency_id, " +
                "fee_currency_symbol, status_str, timestamp, transaction_explorer_id, confirmations_str) SELECT id, " +
                "currency_id, currency_symbol, amount, fee, fee_currency_id, fee_currency_symbol, status_str, " +
                "timestamp, transaction_explorer_id, confirmations FROM deposits"
            )
            execSQL("DROP TABLE deposits")
            execSQL("ALTER TABLE new_deposits RENAME TO deposits")
        }
    }

}


private val MIGRATION_64_65: Migration = object : Migration(64, 65) {

    override fun migrate(database: SupportSQLiteDatabase) {
        with(database) {
            execSQL(
                "CREATE TABLE IF NOT EXISTS new_ticker_items (id INTEGER NOT NULL, base_currency_symbol TEXT NOT NULL, " +
                "base_currency_name TEXT NOT NULL, quote_currency_symbol TEXT NOT NULL, quote_currency_name TEXT NOT NULL, " +
                "name TEXT NOT NULL, best_ask_price REAL, best_bid_price REAL, last_price REAL, open_price REAL, " +
                "low_price REAL, high_price REAL, daily_volume_in_base_currency REAL, daily_volume_in_quote_currency REAL, " +
                "fiat_currency_rates TEXT NOT NULL, timestamp INTEGER NOT NULL, PRIMARY KEY(id))"
            )
            execSQL(
                "INSERT INTO new_ticker_items (id, base_currency_symbol, base_currency_name, quote_currency_symbol, " +
                "quote_currency_name, name, best_ask_price, best_bid_price, last_price, open_price, low_price, high_price, " +
                "daily_volume_in_base_currency, daily_volume_in_quote_currency, fiat_currency_rates, timestamp) SELECT id, " +
                "base_currency_symbol, base_currency_name, quote_currency_symbol, quote_currency_name, name, best_ask_price, " +
                "best_bid_price, last_price, open_price, low_price, high_price, daily_volume_in_base_currency, " +
                "daily_volume_in_quote_currency, fiat_currency_rates, timestamp FROM ticker_items"
            )
            execSQL("DROP TABLE ticker_items")
            execSQL("ALTER TABLE new_ticker_items RENAME TO ticker_items")
        }
    }

}


private val MIGRATION_65_66: Migration = object : Migration(65, 66) {

    override fun migrate(database: SupportSQLiteDatabase) {
        with(database) {
            execSQL(
                "CREATE TABLE IF NOT EXISTS new_settings (id INTEGER NOT NULL, is_sound_enabled INTEGER NOT NULL, " +
                "is_vibration_enabled INTEGER NOT NULL, is_phone_led_enabled INTEGER NOT NULL, " +
                "is_grouping_enabled INTEGER NOT NULL, is_fingerprint_unlock_enabled INTEGER NOT NULL, " +
                "is_force_authentication_on_app_startup_is_enabled INTEGER NOT NULL, " +
                "is_real_time_data_streaming_enabled INTEGER NOT NULL, " +
                "is_orderbook_real_time_updates_highlighting_enabled INTEGER NOT NULL, " +
                "is_new_trades_real_time_addition_highlighting_enabled INTEGER NOT NULL, " +
                "is_price_chart_zoom_in_enabled INTEGER NOT NULL, should_animate_charts INTEGER NOT NULL, " +
                "should_keep_screen_on INTEGER NOT NULL, notification_ringtone TEXT NOT NULL, pin_code TEXT NOT NULL, " +
                "bullish_candle_stick_style TEXT NOT NULL, bearish_candle_stick_style TEXT NOT NULL, " +
                "depth_chart_line_style TEXT NOT NULL, decimal_separator TEXT NOT NULL, grouping_separator TEXT NOT NULL, " +
                "authentication_session_duration TEXT NOT NULL, language TEXT NOT NULL, fiat_currency TEXT NOT NULL, " +
                "theme_id INTEGER NOT NULL, PRIMARY KEY(id))"
            )
            execSQL(
                "INSERT INTO new_settings (id, is_sound_enabled, is_vibration_enabled, is_phone_led_enabled, " +
                "is_grouping_enabled, is_fingerprint_unlock_enabled, is_force_authentication_on_app_startup_is_enabled, " +
                "is_real_time_data_streaming_enabled, is_orderbook_real_time_updates_highlighting_enabled, " +
                "is_new_trades_real_time_addition_highlighting_enabled, is_price_chart_zoom_in_enabled, " +
                "should_animate_charts, should_keep_screen_on, notification_ringtone, pin_code, bullish_candle_stick_style, " +
                "bearish_candle_stick_style, depth_chart_line_style, decimal_separator, grouping_separator, " +
                "authentication_session_duration, language, fiat_currency, theme_id) SELECT id, is_sound_enabled, " +
                "is_vibration_enabled, is_phone_led_enabled, is_grouping_enabled, is_fingerprint_unlock_enabled, " +
                "is_force_authentication_on_app_startup_is_enabled, is_real_time_data_streaming_enabled, " +
                "is_orderbook_real_time_updates_highlighting_enabled, is_new_trades_real_time_addition_highlighting_enabled, " +
                "is_price_chart_zoom_in_enabled, should_animate_charts, should_keep_screen_on, notification_ringtone, pin_code, " +
                "bullish_candle_stick_style, bearish_candle_stick_style, depth_chart_line_style, decimal_separator, " +
                "grouping_separator, authentication_session_duration, \"${Language.ENGLISH.name}\", fiat_currency, theme_id " +
                "FROM settings"
            )
            execSQL("DROP TABLE settings")
            execSQL("ALTER TABLE new_settings RENAME TO settings")
        }
    }

}


private val MIGRATION_66_67: Migration = object : Migration(66, 67) {

    override fun migrate(database: SupportSQLiteDatabase) {
        with(database) {
            execSQL(
                "CREATE TABLE IF NOT EXISTS new_settings (id INTEGER NOT NULL, is_sound_enabled INTEGER NOT NULL, " +
                "is_vibration_enabled INTEGER NOT NULL, is_phone_led_enabled INTEGER NOT NULL, " +
                "is_grouping_enabled INTEGER NOT NULL, is_fingerprint_unlock_enabled INTEGER NOT NULL, " +
                "is_force_authentication_on_app_startup_is_enabled INTEGER NOT NULL, " +
                "is_orderbook_real_time_updates_highlighting_enabled INTEGER NOT NULL, " +
                "is_new_trades_real_time_addition_highlighting_enabled INTEGER NOT NULL, " +
                "is_price_chart_zoom_in_enabled INTEGER NOT NULL, should_animate_charts INTEGER NOT NULL, " +
                "should_keep_screen_on INTEGER NOT NULL, notification_ringtone TEXT NOT NULL, pin_code TEXT NOT NULL, " +
                "bullish_candle_stick_style TEXT NOT NULL, bearish_candle_stick_style TEXT NOT NULL, " +
                "depth_chart_line_style TEXT NOT NULL, decimal_separator TEXT NOT NULL, grouping_separator TEXT NOT NULL, " +
                "authentication_session_duration TEXT NOT NULL, language TEXT NOT NULL, fiat_currency TEXT NOT NULL, " +
                "theme_id INTEGER NOT NULL, PRIMARY KEY(id))"
            )
            execSQL(
                "INSERT INTO new_settings (id, is_sound_enabled, is_vibration_enabled, is_phone_led_enabled, is_grouping_enabled, " +
                "is_fingerprint_unlock_enabled, is_force_authentication_on_app_startup_is_enabled, " +
                "is_orderbook_real_time_updates_highlighting_enabled, is_new_trades_real_time_addition_highlighting_enabled, " +
                "is_price_chart_zoom_in_enabled, should_animate_charts, should_keep_screen_on, notification_ringtone, " +
                "pin_code, bullish_candle_stick_style, bearish_candle_stick_style, depth_chart_line_style, decimal_separator, " +
                "grouping_separator, authentication_session_duration, language, fiat_currency, theme_id) SELECT id, is_sound_enabled, " +
                "is_vibration_enabled, is_phone_led_enabled, is_grouping_enabled, is_fingerprint_unlock_enabled, " +
                "is_force_authentication_on_app_startup_is_enabled, is_orderbook_real_time_updates_highlighting_enabled, " +
                "is_new_trades_real_time_addition_highlighting_enabled, is_price_chart_zoom_in_enabled, should_animate_charts, " +
                "should_keep_screen_on, notification_ringtone, pin_code, bullish_candle_stick_style, bearish_candle_stick_style, " +
                "depth_chart_line_style, decimal_separator, grouping_separator, authentication_session_duration, language, " +
                "fiat_currency, theme_id FROM settings"
            )
            execSQL("DROP TABLE settings")
            execSQL("ALTER TABLE new_settings RENAME TO settings")
        }
    }

}


private val MIGRATION_67_68: Migration = object : Migration(67, 68) {

    override fun migrate(database: SupportSQLiteDatabase) {
        with(database) {
            execSQL(
                "CREATE TABLE IF NOT EXISTS new_settings (id INTEGER NOT NULL, is_sound_enabled INTEGER NOT NULL, " +
                "is_vibration_enabled INTEGER NOT NULL, is_phone_led_enabled INTEGER NOT NULL, " +
                "is_grouping_enabled INTEGER NOT NULL, is_fingerprint_unlock_enabled INTEGER NOT NULL, " +
                "is_force_authentication_on_app_startup_is_enabled INTEGER NOT NULL, " +
                "is_orderbook_real_time_updates_highlighting_enabled INTEGER NOT NULL, " +
                "is_new_trades_real_time_addition_highlighting_enabled INTEGER NOT NULL, " +
                "is_price_chart_zoom_in_enabled INTEGER NOT NULL, are_market_preview_charts_visible INTEGER NOT NULL, " +
                "should_animate_charts INTEGER NOT NULL, should_keep_screen_on INTEGER NOT NULL, " +
                "notification_ringtone TEXT NOT NULL, pin_code TEXT NOT NULL, bullish_candle_stick_style TEXT NOT NULL, " +
                "bearish_candle_stick_style TEXT NOT NULL, depth_chart_line_style TEXT NOT NULL, decimal_separator TEXT NOT NULL, " +
                "grouping_separator TEXT NOT NULL, authentication_session_duration TEXT NOT NULL, language TEXT NOT NULL, " +
                "fiat_currency TEXT NOT NULL, theme_id INTEGER NOT NULL, PRIMARY KEY(id))"
            )
            execSQL(
                "INSERT INTO new_settings (id, is_sound_enabled, is_vibration_enabled, is_phone_led_enabled, is_grouping_enabled, " +
                "is_fingerprint_unlock_enabled, is_force_authentication_on_app_startup_is_enabled, " +
                "is_orderbook_real_time_updates_highlighting_enabled, is_new_trades_real_time_addition_highlighting_enabled, " +
                "is_price_chart_zoom_in_enabled, are_market_preview_charts_visible, should_animate_charts, should_keep_screen_on, " +
                "notification_ringtone, pin_code, bullish_candle_stick_style, bearish_candle_stick_style, depth_chart_line_style, " +
                "decimal_separator, grouping_separator, authentication_session_duration, language, fiat_currency, theme_id) SELECT id, " +
                "is_sound_enabled, is_vibration_enabled, is_phone_led_enabled, is_grouping_enabled, is_fingerprint_unlock_enabled, " +
                "is_force_authentication_on_app_startup_is_enabled, is_orderbook_real_time_updates_highlighting_enabled, " +
                "is_new_trades_real_time_addition_highlighting_enabled, is_price_chart_zoom_in_enabled, 1, should_animate_charts, " +
                "should_keep_screen_on, notification_ringtone, pin_code, bullish_candle_stick_style, bearish_candle_stick_style, " +
                "depth_chart_line_style, decimal_separator, grouping_separator, authentication_session_duration, language, " +
                "fiat_currency, theme_id FROM settings"
            )
            execSQL("DROP TABLE settings")
            execSQL("ALTER TABLE new_settings RENAME TO settings")
        }
    }

}


private val MIGRATION_68_69: Migration = object : Migration(68, 69) {

    override fun migrate(database: SupportSQLiteDatabase) {
        with(database) {
            // Deposits
            execSQL(
                "CREATE TABLE IF NOT EXISTS new_deposits (id INTEGER NOT NULL, currency_id INTEGER NOT NULL, " +
                "currency_symbol TEXT NOT NULL, amount REAL NOT NULL, fee REAL NOT NULL, fee_currency_id INTEGER NOT NULL, " +
                "fee_currency_symbol TEXT NOT NULL, status_id INTEGER NOT NULL, status TEXT NOT NULL, status_color TEXT NOT NULL, " +
                "timestamp INTEGER NOT NULL, transaction_explorer_id TEXT, confirmations_str TEXT NOT NULL, PRIMARY KEY(id))"
            )
            execSQL(
                "INSERT INTO new_deposits (id, currency_id, currency_symbol, amount, fee, fee_currency_id, fee_currency_symbol, " +
                "status_id, status, status_color, timestamp, transaction_explorer_id, confirmations_str) SELECT id, currency_id, " +
                "currency_symbol, amount, fee, fee_currency_id, fee_currency_symbol, -1, status_str, \"\", timestamp, " +
                "transaction_explorer_id, confirmations_str FROM deposits"
            )
            execSQL("DROP TABLE deposits")
            execSQL("ALTER TABLE new_deposits RENAME TO deposits")

            // Withdrawals
            execSQL(
                "CREATE TABLE IF NOT EXISTS new_withdrawals (id INTEGER NOT NULL, currency_id INTEGER NOT NULL, " +
                "currency_symbol TEXT NOT NULL, amount REAL NOT NULL, fee REAL NOT NULL, fee_currency_id INTEGER NOT NULL, " +
                "fee_currency_symbol TEXT NOT NULL, status_id INTEGER NOT NULL, status TEXT NOT NULL, status_color TEXT NOT NULL, " +
                "creation_timestamp INTEGER NOT NULL, update_timestamp INTEGER NOT NULL, transaction_explorer_id TEXT, " +
                "address_data TEXT NOT NULL, PRIMARY KEY(id))"
            )
            execSQL(
                "INSERT INTO new_withdrawals (id, currency_id, currency_symbol, amount, fee, fee_currency_id, fee_currency_symbol, " +
                "status_id, status, status_color, creation_timestamp, update_timestamp, transaction_explorer_id, address_data) SELECT id, " +
                "currency_id, currency_symbol, amount, fee, fee_currency_id, fee_currency_symbol, -1, status_str, \"\", creation_timestamp, " +
                "update_timestamp, transaction_explorer_id, address_data FROM withdrawals"
            )
            execSQL("DROP TABLE withdrawals")
            execSQL("ALTER TABLE new_withdrawals RENAME TO withdrawals")
        }
    }

}


private val MIGRATION_69_70: Migration = object : Migration(69, 70) {

    override fun migrate(database: SupportSQLiteDatabase) {
        with(database) {
            execSQL(
                "CREATE TABLE IF NOT EXISTS new_profile_infos (id INTEGER NOT NULL, email TEXT NOT NULL, user_name TEXT NOT NULL, " +
                "are_withdrawals_allowed INTEGER NOT NULL, verifications TEXT NOT NULL, trading_fee_levels TEXT NOT NULL, " +
                "referral_program TEXT NOT NULL, PRIMARY KEY(id))"
            )
            execSQL(
                "INSERT INTO new_profile_infos (id, email, user_name, are_withdrawals_allowed, verifications, trading_fee_levels, " +
                "referral_program) SELECT id, email, user_name, are_withdrawals_allowed, verifications, trading_fee_levels, " +
                "\"\" FROM profile_infos"
            )
            execSQL("DROP TABLE profile_infos")
            execSQL("ALTER TABLE new_profile_infos RENAME TO profile_infos")
        }
    }

}


private val MIGRATION_70_71: Migration = object : Migration(70, 71) {

    override fun migrate(database: SupportSQLiteDatabase) {
        with(database) {
            execSQL(
                "CREATE TABLE IF NOT EXISTS new_settings (id INTEGER NOT NULL, is_sound_enabled INTEGER NOT NULL, " +
                "is_vibration_enabled INTEGER NOT NULL, is_phone_led_enabled INTEGER NOT NULL, is_grouping_enabled INTEGER NOT NULL, " +
                "is_fingerprint_unlock_enabled INTEGER NOT NULL, is_force_authentication_on_app_startup_is_enabled INTEGER NOT NULL, " +
                "is_orderbook_real_time_updates_highlighting_enabled INTEGER NOT NULL, " +
                "is_new_trades_real_time_addition_highlighting_enabled INTEGER NOT NULL, is_price_chart_zoom_in_enabled INTEGER NOT NULL, " +
                "are_market_preview_charts_visible INTEGER NOT NULL, are_analytics_toasts_enabled INTEGER NOT NULL, " +
                "should_animate_charts INTEGER NOT NULL, should_keep_screen_on INTEGER NOT NULL, notification_ringtone TEXT NOT NULL, " +
                "pin_code TEXT NOT NULL, bullish_candle_stick_style TEXT NOT NULL, bearish_candle_stick_style TEXT NOT NULL, " +
                "depth_chart_line_style TEXT NOT NULL, decimal_separator TEXT NOT NULL, grouping_separator TEXT NOT NULL, " +
                "authentication_session_duration TEXT NOT NULL, language TEXT NOT NULL, fiat_currency TEXT NOT NULL, theme_id INTEGER NOT NULL, " +
                "PRIMARY KEY(id))"
            )
            execSQL(
                "INSERT INTO new_settings (id, is_sound_enabled, is_vibration_enabled, is_phone_led_enabled, is_grouping_enabled, " +
                "is_fingerprint_unlock_enabled, is_force_authentication_on_app_startup_is_enabled, " +
                "is_orderbook_real_time_updates_highlighting_enabled, is_new_trades_real_time_addition_highlighting_enabled, " +
                "is_price_chart_zoom_in_enabled, are_market_preview_charts_visible, are_analytics_toasts_enabled, should_animate_charts, " +
                "should_keep_screen_on, notification_ringtone, pin_code, bullish_candle_stick_style, bearish_candle_stick_style, " +
                "depth_chart_line_style, decimal_separator, grouping_separator, authentication_session_duration, language, fiat_currency, " +
                "theme_id) SELECT id, is_sound_enabled, is_vibration_enabled, is_phone_led_enabled, is_grouping_enabled, is_fingerprint_unlock_enabled, " +
                "is_force_authentication_on_app_startup_is_enabled, is_orderbook_real_time_updates_highlighting_enabled, " +
                "is_new_trades_real_time_addition_highlighting_enabled, is_price_chart_zoom_in_enabled, are_market_preview_charts_visible, " +
                "0, should_animate_charts, should_keep_screen_on, notification_ringtone, pin_code, bullish_candle_stick_style, " +
                "bearish_candle_stick_style, depth_chart_line_style, decimal_separator, grouping_separator, authentication_session_duration, " +
                "language, fiat_currency, theme_id FROM settings"
            )
            execSQL("DROP TABLE settings")
            execSQL("ALTER TABLE new_settings RENAME TO settings")
        }
    }

}


private val MIGRATION_71_72: Migration = object : Migration(71, 72) {

    override fun migrate(database: SupportSQLiteDatabase) {
        with(database) {
            execSQL(
                """
                CREATE TABLE IF NOT EXISTS new_wallets (id INTEGER, currency_id INTEGER NOT NULL, currency_type_id INTEGER NOT NULL, 
                is_delisted INTEGER NOT NULL, is_disabled INTEGER NOT NULL, is_depositing_disabled INTEGER NOT NULL, currency_name TEXT NOT NULL, 
                currency_symbol TEXT NOT NULL, current_balance REAL NOT NULL, frozen_balance REAL NOT NULL, bonus_balance REAL NOT NULL, 
                total_balance REAL NOT NULL, deposit_address_data TEXT, additional_withdrawal_parameter_name TEXT, PRIMARY KEY(currency_id))
                """
            )
            execSQL(
                """
                INSERT INTO new_wallets (id, currency_id, currency_type_id, is_delisted, is_disabled, is_depositing_disabled, currency_name, 
                currency_symbol, current_balance, frozen_balance, bonus_balance, total_balance, deposit_address_data, additional_withdrawal_parameter_name) 
                SELECT id, currency_id, 0, is_delisted, is_disabled, is_depositing_disabled, currency_name, currency_symbol, current_balance, frozen_balance, 
                bonus_balance, total_balance, deposit_address_data, additional_withdrawal_parameter_name FROM wallets
                """
            )
            execSQL("DROP TABLE wallets")
            execSQL("ALTER TABLE new_wallets RENAME TO wallets")
        }
    }

}


private val MIGRATION_72_73: Migration = object : Migration(72, 73) {

    override fun migrate(database: SupportSQLiteDatabase) {
        with(database) {
            execSQL(
                """
                CREATE TABLE IF NOT EXISTS new_wallets (id INTEGER, currency_id INTEGER NOT NULL, currency_type_id INTEGER NOT NULL, 
                is_delisted INTEGER NOT NULL, is_disabled INTEGER NOT NULL, is_depositing_disabled INTEGER NOT NULL, currency_name TEXT NOT NULL, 
                currency_symbol TEXT NOT NULL, current_balance REAL NOT NULL, frozen_balance REAL NOT NULL, bonus_balance REAL NOT NULL, 
                total_balance REAL NOT NULL, deposit_address_data TEXT, multi_deposit_addresses TEXT NOT NULL, additional_withdrawal_parameter_name TEXT, 
                PRIMARY KEY(currency_id))
                """
            )
            execSQL(
                """
                INSERT INTO new_wallets (id, currency_id, currency_type_id, is_delisted, is_disabled, is_depositing_disabled, currency_name, 
                currency_symbol, current_balance, frozen_balance, bonus_balance, total_balance, deposit_address_data, multi_deposit_addresses, 
                additional_withdrawal_parameter_name) SELECT id, currency_id, currency_type_id, is_delisted, is_disabled, is_depositing_disabled, 
                currency_name, currency_symbol, current_balance, frozen_balance, bonus_balance, total_balance, deposit_address_data, "", 
                additional_withdrawal_parameter_name FROM wallets
                """
            )
            execSQL("DROP TABLE wallets")
            execSQL("ALTER TABLE new_wallets RENAME TO wallets")
        }
    }

}


private val MIGRATION_73_74: Migration = object : Migration(73, 74) {

    override fun migrate(database: SupportSQLiteDatabase) {
        with(database) {
            execSQL(
                """
                CREATE TABLE IF NOT EXISTS new_wallets (id INTEGER, currency_id INTEGER NOT NULL, currency_type_id INTEGER NOT NULL, 
                is_delisted INTEGER NOT NULL, is_disabled INTEGER NOT NULL, is_depositing_disabled INTEGER NOT NULL, currency_name TEXT NOT NULL, 
                currency_symbol TEXT NOT NULL, current_balance REAL NOT NULL, frozen_balance REAL NOT NULL, bonus_balance REAL NOT NULL, 
                total_balance REAL NOT NULL, deposit_address_data TEXT, protocols TEXT NOT NULL, multi_deposit_addresses TEXT NOT NULL, 
                additional_withdrawal_parameter_name TEXT, PRIMARY KEY(currency_id))
                """
            )
            execSQL(
                """
                INSERT INTO new_wallets (id, currency_id, currency_type_id, is_delisted, is_disabled, is_depositing_disabled, currency_name, 
                currency_symbol, current_balance, frozen_balance, bonus_balance, total_balance, deposit_address_data, protocols, multi_deposit_addresses, 
                additional_withdrawal_parameter_name) SELECT id, currency_id, currency_type_id, is_delisted, is_disabled, is_depositing_disabled, 
                currency_name, currency_symbol, current_balance, frozen_balance, bonus_balance, total_balance, deposit_address_data, "", 
                multi_deposit_addresses, additional_withdrawal_parameter_name FROM wallets
                """
            )
            execSQL("DROP TABLE wallets")
            execSQL("ALTER TABLE new_wallets RENAME TO wallets")
        }
    }

}


private val MIGRATION_74_75: Migration = object : Migration(74, 75) {

    override fun migrate(database: SupportSQLiteDatabase) {
        with(database) {
            execSQL(
                """
                CREATE TABLE IF NOT EXISTS new_orders (id INTEGER NOT NULL, currency_pair_id INTEGER NOT NULL, currency_pair_name TEXT NOT NULL, 
                price REAL NOT NULL, trigger_price REAL NOT NULL, initial_amount REAL NOT NULL, filled_amount REAL NOT NULL, type_str TEXT NOT NULL, 
                original_type_str TEXT NOT NULL, timestamp INTEGER NOT NULL, status_str TEXT NOT NULL, trades TEXT NOT NULL, 
                fees TEXT NOT NULL, PRIMARY KEY(id))
                """
            )
            execSQL(
                """
                INSERT INTO new_orders (id, currency_pair_id, currency_pair_name, price, trigger_price, initial_amount, filled_amount, type_str, 
                original_type_str, timestamp, status_str, trades, fees) SELECT id, currency_pair_id, "", price, trigger_price, initial_amount, 
                filled_amount, type_str, original_type_str, timestamp, status_str, trades, fees FROM orders
                """
            )
            execSQL("DROP TABLE orders")
            execSQL("ALTER TABLE new_orders RENAME TO orders")
        }
    }

}


private val MIGRATION_75_76: Migration = object : Migration(75, 76) {

    override fun migrate(database: SupportSQLiteDatabase) {
        with(database) {
            // Currencies
            execSQL(
                """
                CREATE TABLE IF NOT EXISTS new_currencies (id INTEGER NOT NULL, symbol TEXT NOT NULL, name TEXT NOT NULL, is_active INTEGER NOT NULL, 
                is_delisted INTEGER NOT NULL, precision INTEGER NOT NULL, minimum_withdrawal_amount REAL NOT NULL, minimum_deposit_amount REAL NOT NULL, 
                deposit_fee_currency_id INTEGER NOT NULL, deposit_fee_currency_symbol TEXT NOT NULL, deposit_fee REAL NOT NULL, 
                deposit_fee_in_percentage REAL NOT NULL, withdrawal_fee_currency_id INTEGER NOT NULL, withdrawal_fee_currency_symbol TEXT NOT NULL, 
                withdrawal_fee REAL NOT NULL, withdrawal_fee_in_percentage REAL NOT NULL, block_explorer_url TEXT NOT NULL, protocols TEXT NOT NULL, 
                PRIMARY KEY(id))
                """
            )
            execSQL(
                """
                INSERT INTO new_currencies (id, symbol, name, is_active, is_delisted, precision, minimum_withdrawal_amount, minimum_deposit_amount, 
                deposit_fee_currency_id, deposit_fee_currency_symbol, deposit_fee, deposit_fee_in_percentage, withdrawal_fee_currency_id, 
                withdrawal_fee_currency_symbol, withdrawal_fee, withdrawal_fee_in_percentage, block_explorer_url, protocols) SELECT id, symbol, name, 
                is_active, is_delisted, precision, minimum_withdrawal_amount, minimum_deposit_amount, deposit_fee_currency_id, deposit_fee_currency_symbol, 
                deposit_fee, deposit_fee_in_percentage, withdrawal_fee_currency_id, withdrawal_fee_currency_symbol, withdrawal_fee, withdrawal_fee_in_percentage, 
                block_explorer_url, "" FROM currencies
                """
            )
            execSQL("DROP TABLE currencies")
            execSQL("ALTER TABLE new_currencies RENAME TO currencies")

            // Deposits
            execSQL(
                """
                CREATE TABLE IF NOT EXISTS new_deposits (id INTEGER NOT NULL, currency_id INTEGER NOT NULL, currency_symbol TEXT NOT NULL, 
                amount REAL NOT NULL, fee REAL NOT NULL, fee_currency_id INTEGER NOT NULL, fee_currency_symbol TEXT NOT NULL, 
                status_id INTEGER NOT NULL, status TEXT NOT NULL, status_color TEXT NOT NULL, protocol_id INTEGER NOT NULL, 
                timestamp INTEGER NOT NULL, transaction_explorer_id TEXT, confirmations_str TEXT NOT NULL, PRIMARY KEY(id))
                """
            )
            execSQL(
                """
                INSERT INTO new_deposits (id, currency_id, currency_symbol, amount, fee, fee_currency_id, fee_currency_symbol, status_id, 
                status, status_color, protocol_id, timestamp, transaction_explorer_id, confirmations_str) SELECT id, currency_id, currency_symbol, 
                amount, fee, fee_currency_id, fee_currency_symbol, status_id, status, status_color, 0, timestamp, transaction_explorer_id, 
                confirmations_str FROM deposits
                """
            )
            execSQL("DROP TABLE deposits")
            execSQL("ALTER TABLE new_deposits RENAME TO deposits")
        }
    }

}


private val MIGRATION_76_77: Migration = object : Migration(76, 77) {

    override fun migrate(database: SupportSQLiteDatabase) {
        with(database) {
            // ProfileInfos
            execSQL(
                """
                CREATE TABLE IF NOT EXISTS new_profile_infos (id INTEGER NOT NULL, email TEXT NOT NULL, user_name TEXT NOT NULL, 
                are_withdrawals_allowed INTEGER NOT NULL, verifications TEXT NOT NULL, trading_fee_levels TEXT NOT NULL, 
                referral_program TEXT NOT NULL, settings TEXT NOT NULL, PRIMARY KEY(id))
                """
            )
            execSQL(
                """
                INSERT INTO new_profile_infos (id, email, user_name, are_withdrawals_allowed, verifications, trading_fee_levels, 
                referral_program, settings) SELECT id, email, user_name, are_withdrawals_allowed, verifications, trading_fee_levels,
                referral_program, "" FROM profile_infos
                """
            )
            execSQL("DROP TABLE profile_infos")
            execSQL("ALTER TABLE new_profile_infos RENAME TO profile_infos")

            // Settings
            execSQL(
                """
                CREATE TABLE IF NOT EXISTS new_settings (id INTEGER NOT NULL, is_grouping_enabled INTEGER NOT NULL, 
                is_fingerprint_unlock_enabled INTEGER NOT NULL, is_notification_enabled INTEGER NOT NULL, 
                is_force_authentication_on_app_startup_is_enabled INTEGER NOT NULL, is_orderbook_real_time_updates_highlighting_enabled INTEGER NOT NULL, 
                is_new_trades_real_time_addition_highlighting_enabled INTEGER NOT NULL, is_price_chart_zoom_in_enabled INTEGER NOT NULL, 
                are_market_preview_charts_visible INTEGER NOT NULL, are_analytics_toasts_enabled INTEGER NOT NULL, 
                should_animate_charts INTEGER NOT NULL, should_keep_screen_on INTEGER NOT NULL, pin_code TEXT NOT NULL, 
                bullish_candle_stick_style TEXT NOT NULL, bearish_candle_stick_style TEXT NOT NULL, 
                depth_chart_line_style TEXT NOT NULL, decimal_separator TEXT NOT NULL, grouping_separator TEXT NOT NULL, 
                authentication_session_duration TEXT NOT NULL, language TEXT NOT NULL, fiat_currency TEXT NOT NULL, theme_id INTEGER NOT NULL, 
                PRIMARY KEY(id))
                """
            )
            execSQL(
                """
                INSERT INTO new_settings (id, is_grouping_enabled, is_fingerprint_unlock_enabled, is_notification_enabled, 
                is_force_authentication_on_app_startup_is_enabled, is_orderbook_real_time_updates_highlighting_enabled, 
                is_new_trades_real_time_addition_highlighting_enabled, is_price_chart_zoom_in_enabled, are_market_preview_charts_visible, 
                are_analytics_toasts_enabled, should_animate_charts, should_keep_screen_on, pin_code, bullish_candle_stick_style, bearish_candle_stick_style, 
                depth_chart_line_style, decimal_separator, grouping_separator, authentication_session_duration, language, fiat_currency, 
                theme_id) SELECT id, is_grouping_enabled, is_fingerprint_unlock_enabled, 1, is_force_authentication_on_app_startup_is_enabled, 
                is_orderbook_real_time_updates_highlighting_enabled, is_new_trades_real_time_addition_highlighting_enabled, is_price_chart_zoom_in_enabled, 
                are_market_preview_charts_visible, are_analytics_toasts_enabled, should_animate_charts, should_keep_screen_on, pin_code, 
                bullish_candle_stick_style, bearish_candle_stick_style, depth_chart_line_style, decimal_separator, grouping_separator, 
                authentication_session_duration, language, fiat_currency, theme_id FROM settings
                """
            )
            execSQL("DROP TABLE settings")
            execSQL("ALTER TABLE new_settings RENAME TO settings")

            // Inbox
            execSQL(
                """
                CREATE TABLE IF NOT EXISTS inbox (id TEXT NOT NULL, title TEXT NOT NULL, `desc` TEXT NOT NULL, date TEXT NOT NULL, 
                channel TEXT, readAt TEXT, coin TEXT, coin_full_name TEXT, amount TEXT, order_id TEXT, currency_pair TEXT, type TEXT, 
                order_amount TEXT, price TEXT, expected_amount TEXT, fee TEXT, in_orders TEXT, ip TEXT, location TEXT, browser TEXT, 
                browser_version TEXT, device TEXT, platform TEXT, PRIMARY KEY(id)) 
                """
            )
        }
    }

}


private val MIGRATION_77_78 = object : Migration(77, 78) {

    override fun migrate(database: SupportSQLiteDatabase) {
        with(database) {
            execSQL(
                """
                UPDATE settings 
                SET is_force_authentication_on_app_startup_is_enabled = 1
                WHERE id = 1
                """
            )
        }
    }

}


private val MIGRATION_78_79 = object : Migration(78, 79) {

    override fun migrate(database: SupportSQLiteDatabase) {
        with(database) {
            // AlertPrice
            execSQL(
                """
                CREATE TABLE IF NOT EXISTS alertprice (id INTEGER NOT NULL, currency_pair_id INTEGER NOT NULL, 
                currency_pair_name TEXT NOT NULL, comparison_type TEXT NOT NULL, price REAL NOT NULL, 
                active INTEGER NOT NULL, PRIMARY KEY(id)) 
                """
            )
        }
    }


}


private val MIGRATION_79_80 = object : Migration(79, 80) {

    override fun migrate(database: SupportSQLiteDatabase) {
        with(database) {
            // Settings
            execSQL(" UPDATE settings SET theme_id = ${Themes.DEEP_TEAL.id}")
        }
    }

}


private val MIGRATION_80_81 = object : Migration(80, 81) {

    override fun migrate(database: SupportSQLiteDatabase) {
        with(database) {
            execSQL(
                """
                CREATE TABLE IF NOT EXISTS new_settings (id INTEGER NOT NULL, is_grouping_enabled INTEGER NOT NULL, 
                is_fingerprint_unlock_enabled INTEGER NOT NULL, is_notification_enabled INTEGER NOT NULL, 
                is_force_authentication_on_app_startup_is_enabled INTEGER NOT NULL, is_orderbook_real_time_updates_highlighting_enabled INTEGER NOT NULL, 
                is_new_trades_real_time_addition_highlighting_enabled INTEGER NOT NULL, is_price_chart_zoom_in_enabled INTEGER NOT NULL, 
                are_market_preview_charts_visible INTEGER NOT NULL, are_analytics_toasts_enabled INTEGER NOT NULL, are_empty_wallets_hidden INTEGER NOT NULL, 
                should_animate_charts INTEGER NOT NULL, should_keep_screen_on INTEGER NOT NULL, pin_code TEXT NOT NULL, bullish_candle_stick_style TEXT NOT NULL, 
                bearish_candle_stick_style TEXT NOT NULL, depth_chart_line_style TEXT NOT NULL, decimal_separator TEXT NOT NULL, 
                grouping_separator TEXT NOT NULL, authentication_session_duration TEXT NOT NULL, language TEXT NOT NULL, fiat_currency TEXT NOT NULL, 
                theme_id INTEGER NOT NULL, PRIMARY KEY(id))
                """
            )
            execSQL(
                """
                INSERT INTO new_settings (id, is_grouping_enabled, is_fingerprint_unlock_enabled, is_notification_enabled, 
                is_force_authentication_on_app_startup_is_enabled, is_orderbook_real_time_updates_highlighting_enabled, 
                is_new_trades_real_time_addition_highlighting_enabled, is_price_chart_zoom_in_enabled, are_market_preview_charts_visible, 
                are_analytics_toasts_enabled, are_empty_wallets_hidden, should_animate_charts, should_keep_screen_on, pin_code, 
                bullish_candle_stick_style, bearish_candle_stick_style, depth_chart_line_style, decimal_separator, grouping_separator, 
                authentication_session_duration, language, fiat_currency, theme_id) SELECT id, is_grouping_enabled, is_fingerprint_unlock_enabled, 
                is_notification_enabled, is_force_authentication_on_app_startup_is_enabled, is_orderbook_real_time_updates_highlighting_enabled, 
                is_new_trades_real_time_addition_highlighting_enabled, is_price_chart_zoom_in_enabled, are_market_preview_charts_visible, 
                are_analytics_toasts_enabled, 0, should_animate_charts, should_keep_screen_on, pin_code, bullish_candle_stick_style, 
                bearish_candle_stick_style, depth_chart_line_style, decimal_separator, grouping_separator, authentication_session_duration, 
                language, fiat_currency, theme_id FROM settings
                """
            )
            execSQL("DROP TABLE settings")
            execSQL("ALTER TABLE new_settings RENAME TO settings")
        }
    }

}


private val MIGRATION_81_82 = object : Migration(81, 82) {
    override fun migrate(database: SupportSQLiteDatabase) {
        with(database) {
            execSQL(
                """
                 CREATE TABLE IF NOT EXISTS new_settings (id INTEGER NOT NULL, is_fingerprint_unlock_enabled INTEGER NOT NULL,
                 is_notification_enabled INTEGER NOT NULL, is_force_authentication_on_app_startup_is_enabled INTEGER NOT NULL,
                 is_orderbook_real_time_updates_highlighting_enabled INTEGER NOT NULL, is_new_trades_real_time_addition_highlighting_enabled INTEGER NOT NULL,
                 is_price_chart_zoom_in_enabled INTEGER NOT NULL, are_market_preview_charts_visible INTEGER NOT NULL, 
                 are_analytics_toasts_enabled INTEGER NOT NULL, are_empty_wallets_hidden INTEGER NOT NULL, should_animate_charts INTEGER NOT NULL,
                 should_keep_screen_on INTEGER NOT NULL, pin_code TEXT NOT NULL, bullish_candle_stick_style TEXT NOT NULL,
                 bearish_candle_stick_style TEXT NOT NULL, depth_chart_line_style TEXT NOT NULL, authentication_session_duration TEXT NOT NULL,
                 language TEXT NOT NULL, fiat_currency TEXT NOT NULL, theme_id INTEGER NOT NULL, PRIMARY KEY(id))
                 """
            )
            execSQL(
                """
                INSERT INTO new_settings (id, is_fingerprint_unlock_enabled, is_notification_enabled, is_force_authentication_on_app_startup_is_enabled,
                is_orderbook_real_time_updates_highlighting_enabled, is_new_trades_real_time_addition_highlighting_enabled, is_price_chart_zoom_in_enabled, 
                are_market_preview_charts_visible, are_analytics_toasts_enabled, are_empty_wallets_hidden, should_animate_charts, 
                should_keep_screen_on, pin_code, bullish_candle_stick_style, bearish_candle_stick_style, depth_chart_line_style,
                authentication_session_duration, language, fiat_currency, theme_id) SELECT id, is_fingerprint_unlock_enabled, 
                is_notification_enabled, is_force_authentication_on_app_startup_is_enabled, is_orderbook_real_time_updates_highlighting_enabled, 
                is_new_trades_real_time_addition_highlighting_enabled, is_price_chart_zoom_in_enabled, are_market_preview_charts_visible, 
                are_analytics_toasts_enabled, are_empty_wallets_hidden, should_animate_charts, should_keep_screen_on, 
                pin_code, bullish_candle_stick_style, bearish_candle_stick_style, depth_chart_line_style, authentication_session_duration, 
                language, fiat_currency, theme_id FROM settings
                """
            )
            execSQL("DROP TABLE settings")
            execSQL("ALTER TABLE new_settings RENAME TO settings")
        }
    }
}


private val MIGRATION_82_83 = object : Migration(82, 83) {
    override fun migrate(database: SupportSQLiteDatabase) {
        with(database) {
            // Currencies
            execSQL(
                """
                CREATE TABLE IF NOT EXISTS new_currencies (id INTEGER NOT NULL, symbol TEXT NOT NULL, name TEXT NOT NULL, is_active INTEGER NOT NULL, 
                is_delisted INTEGER NOT NULL, precision INTEGER NOT NULL, minimum_withdrawal_amount REAL NOT NULL, minimum_deposit_amount REAL NOT NULL, 
                deposit_fee_currency_id INTEGER NOT NULL, deposit_fee_currency_symbol TEXT NOT NULL, deposit_fee REAL NOT NULL, 
                deposit_fee_in_percentage REAL NOT NULL, withdrawal_fee_currency_id INTEGER NOT NULL, withdrawal_fee_currency_symbol TEXT NOT NULL, 
                withdrawal_fee REAL NOT NULL, withdrawal_fee_in_percentage REAL NOT NULL, block_explorer_url TEXT NOT NULL, protocols TEXT NOT NULL, 
                withdrawal_limit REAL NOT NULL, PRIMARY KEY(id))
                """
            )
            execSQL(
                """
                INSERT INTO new_currencies (id, symbol, name, is_active, is_delisted, precision, minimum_withdrawal_amount, minimum_deposit_amount, 
                deposit_fee_currency_id, deposit_fee_currency_symbol, deposit_fee, deposit_fee_in_percentage, withdrawal_fee_currency_id, 
                withdrawal_fee_currency_symbol, withdrawal_fee, withdrawal_fee_in_percentage, block_explorer_url, protocols, withdrawal_limit) SELECT id, symbol, name, 
                is_active, is_delisted, precision, minimum_withdrawal_amount, minimum_deposit_amount, deposit_fee_currency_id, deposit_fee_currency_symbol, 
                deposit_fee, deposit_fee_in_percentage, withdrawal_fee_currency_id, withdrawal_fee_currency_symbol, withdrawal_fee, withdrawal_fee_in_percentage, 
                block_explorer_url, protocols, 0 FROM currencies
                """
            )
            execSQL("DROP TABLE currencies")
            execSQL("ALTER TABLE new_currencies RENAME TO currencies")

            // Wallets
            execSQL(
                """
                CREATE TABLE IF NOT EXISTS new_wallets (id INTEGER, currency_id INTEGER NOT NULL, currency_type_id INTEGER NOT NULL, 
                is_delisted INTEGER NOT NULL, is_disabled INTEGER NOT NULL, is_depositing_disabled INTEGER NOT NULL, currency_name TEXT NOT NULL, 
                currency_symbol TEXT NOT NULL, current_balance REAL NOT NULL, frozen_balance REAL NOT NULL, bonus_balance REAL NOT NULL, 
                total_balance REAL NOT NULL, deposit_address_data TEXT, protocols TEXT NOT NULL, multi_deposit_addresses TEXT NOT NULL, 
                additional_withdrawal_parameter_name TEXT, withdrawal_limit REAL NOT NULL, PRIMARY KEY(currency_id))
                """
            )
            execSQL(
                """
                INSERT INTO new_wallets (id, currency_id, currency_type_id, is_delisted, is_disabled, is_depositing_disabled, currency_name, 
                currency_symbol, current_balance, frozen_balance, bonus_balance, total_balance, deposit_address_data, protocols, multi_deposit_addresses, 
                additional_withdrawal_parameter_name, withdrawal_limit) SELECT id, currency_id, currency_type_id, is_delisted, is_disabled, is_depositing_disabled, 
                currency_name, currency_symbol, current_balance, frozen_balance, bonus_balance, total_balance, deposit_address_data, protocols, 
                multi_deposit_addresses, additional_withdrawal_parameter_name, 0 FROM wallets
                """
            )
            execSQL("DROP TABLE wallets")
            execSQL("ALTER TABLE new_wallets RENAME TO wallets")
        }
    }
}


val MIGRATIONS: Array<Migration> = arrayOf(
    MIGRATION_1_2,          MIGRATION_2_3,          MIGRATION_3_4,
    MIGRATION_4_5,          MIGRATION_5_6,          MIGRATION_6_7,
    MIGRATION_7_8,          MIGRATION_8_9,          MIGRATION_9_10,
    MIGRATION_10_11,        MIGRATION_11_12,        MIGRATION_12_13,
    MIGRATION_13_14,        MIGRATION_14_15,        MIGRATION_15_16,
    MIGRATION_16_17,        MIGRATION_17_18,        MIGRATION_18_19,
    MIGRATION_19_20,        MIGRATION_20_21,        MIGRATION_21_22,
    MIGRATION_22_23,        MIGRATION_23_24,        MIGRATION_24_25,
    MIGRATION_25_26,        MIGRATION_26_27,        MIGRATION_27_28,
    MIGRATION_28_29,        MIGRATION_29_30,        MIGRATION_30_31,
    MIGRATION_31_32,        MIGRATION_32_33,        MIGRATION_33_34,
    MIGRATION_34_35,        MIGRATION_35_36,        MIGRATION_36_37,
    MIGRATION_37_38,        MIGRATION_38_39,        MIGRATION_39_40,
    MIGRATION_40_41,        MIGRATION_41_42,        MIGRATION_42_43,
    MIGRATION_43_44,        MIGRATION_44_45,        MIGRATION_45_46,
    MIGRATION_46_47,        MIGRATION_47_48,        MIGRATION_48_49,
    MIGRATION_49_50,        MIGRATION_50_51,        MIGRATION_51_52,
    MIGRATION_52_53,        MIGRATION_53_54,        MIGRATION_54_55,
    MIGRATION_55_56,        MIGRATION_56_57,        MIGRATION_57_58,
    MIGRATION_58_59,        MIGRATION_59_60,        MIGRATION_60_61,
    MIGRATION_61_62,        MIGRATION_62_63,        MIGRATION_63_64,
    MIGRATION_64_65,        MIGRATION_65_66,        MIGRATION_66_67,
    MIGRATION_67_68,        MIGRATION_68_69,        MIGRATION_69_70,
    MIGRATION_70_71,        MIGRATION_71_72,        MIGRATION_72_73,
    MIGRATION_73_74,        MIGRATION_74_75,        MIGRATION_75_76,
    MIGRATION_76_77,        MIGRATION_77_78,        MIGRATION_78_79,
    MIGRATION_79_80,        MIGRATION_80_81,        MIGRATION_81_82,
    MIGRATION_82_83
)