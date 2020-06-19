package com.stocksexchange.android.utils

import android.content.Context
import android.content.SharedPreferences
import android.preference.PreferenceManager

class ReloadProvider(context: Context) {


    companion object {

        private const val KEY_WALLETS = "reload_wallets_page"
        private const val KEY_WALLETS_SEARCH = "reload_wallets_search"
        private const val KEY_ACTIVE_ORDERS = "reload_orders"

    }


    private val sharedPreferences: SharedPreferences = PreferenceManager.getDefaultSharedPreferences(context)




    fun wallets(): Boolean {
        return get(KEY_WALLETS, false);
    }


    fun setWallets(value: Boolean) {
        put(KEY_WALLETS, value)
    }


    fun walletsSearch(): Boolean {
        return get(KEY_WALLETS_SEARCH, false);
    }


    fun setWalletsSearch(value: Boolean) {
        put(KEY_WALLETS_SEARCH, value)
    }


    fun activeOrders(): Boolean {
        return get(KEY_ACTIVE_ORDERS, false);
    }


    fun setActiveOrders(value: Boolean) {
        put(KEY_ACTIVE_ORDERS, value)
    }

    /**
     * Puts a value associated with a key inside shared preferences.
     *
     * @param key The key of the value
     * @param value The value itself
     */
    fun put(key: String, value: Any) {
        put(key to value)
    }


    /**
     * Puts pairs of data inside shared preferences.
     *
     * @param pairs The pairs of data to put
     */
    fun put(vararg pairs: Pair<String, *>) {
        sharedPreferences.edit {
            pairs.forEach { put(it) }
        }
    }


    private fun SharedPreferences.Editor.put(pair: Pair<String, *>) {
        val (key, data) = pair

        when (data) {
            is Boolean -> putBoolean(key, data)
            is Int -> putInt(key, data)
            is Long -> putLong(key, data)
            is Float -> putFloat(key, data)
            is String -> putString(key, data)

            else -> throw IllegalArgumentException("Class ${data?.let { it::class }} is not supported")
        }
    }


    /**
     * Removes data from the shared preferences specified
     * by the passed in keys.
     *
     * @param keys The keys of data to remove
     */
    fun remove(vararg keys: String) {
        sharedPreferences.edit {
            keys.forEach { remove(it) }
        }
    }


    /**
     * Retrieves data from the shared preferences.
     *
     * @param key The key to get the data for
     * @param defaultValue The default value to return
     * if the key is absent in the preferences
     */
    @Suppress("UNCHECKED_CAST")
    fun <T : Any> get(key: String, defaultValue: T) : T {
        return with(sharedPreferences) {
            when(defaultValue) {
                is Int -> getInt(key, defaultValue) as T
                is Long -> getLong(key, defaultValue) as T
                is Float -> getFloat(key, defaultValue) as T
                is String -> getString(key, defaultValue) as T
                is Boolean -> getBoolean(key, defaultValue) as T

                else -> throw IllegalArgumentException("Class ${defaultValue.let { it::class }} is not supported")
            }
        }
    }


    private fun SharedPreferences.edit(editAction: SharedPreferences.Editor.() -> Unit) {
        val editor = edit()
        editor.editAction()
        editor.apply()
    }


}