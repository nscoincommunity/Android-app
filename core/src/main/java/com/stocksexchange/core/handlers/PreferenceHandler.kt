package com.stocksexchange.core.handlers

import android.content.Context
import android.content.SharedPreferences
import android.preference.PreferenceManager

class PreferenceHandler(context: Context)  {


    companion object {

        private const val KEY_EMAIL = "email"
        private const val KEY_TOKEN_TYPE = "token_type"
        private const val KEY_ACCESS_TOKEN = "access_token"
        private const val KEY_REFRESH_TOKEN = "refresh_token"
        private const val KEY_ACCESS_TOKEN_EXPIRATION_TIME = "access_token_expiration_time"
        private const val KEY_REFRESH_TOKEN_EXPIRATION_TIME = "refresh_token_expiration_time"

        private const val KEY_ARE_FINGERPRINT_ATTEMPTS_USED_UP = "are_fingerprint_attempts_used_up"
        private const val KEY_LAST_AUTH_TIMESTAMP = "last_auth_timestamp"
        private const val KEY_INVALID_PIN_CODES_ATTEMPTS_NUMBER = "invalid_pin_codes_attempts_number"
        private const val KEY_ALLOW_AUTH_TIMESTAMP = "allow_auth_timestamp"

        private const val KEY_IS_INITIAL_APP_LANGUAGE_SELECTED = "is_initial_app_language_selected"
        private const val KEY_IS_INTERCOM_UNIDENTIFIABLE_USER_REGISTERED = "is_intercom_unidentifiable_user_registered"
        private const val KEY_IS_INTERCOM_IDENTIFIABLE_USER_REGISTERED = "is_intercom_identifiable_user_registered"
        private const val KEY_FIREBASE_PUSH_NOTIFICATION_TOKEN = "firebase_push_notification_token"
        private const val KEY_INBOX_UNREAD_COUNT_ITEM = "inbox_unread_count_item"

        private const val KEY_LAST_WORKING_API_BASE_URL = "last_working_api_base_url"
        private const val KEY_LAST_WORKING_RSS_URL = "last_working_rss_url"
        private const val KEY_LAST_WORKING_SOCKET_URL = "last_working_socket_url"
        private const val KEY_LAST_WORKING_HASHED_DOMAIN_NAME = "last_working_hashed_domain_name"
        private const val KEY_LAST_NOTIFICATION_ID = "last_notification_id"

        private const val KEY_APP_STATE_STATUS = "app_state_status"
    }


    private val sharedPreferences: SharedPreferences = PreferenceManager.getDefaultSharedPreferences(context)




    fun saveEmail(email: String) = put(KEY_EMAIL, email)


    fun removeEmail() = remove(KEY_EMAIL)


    fun getEmail(): String = get(KEY_EMAIL, "")


    fun hasEmail(): Boolean = getEmail().isNotBlank()


    fun saveTokenType(tokenType: String) {
        put(KEY_TOKEN_TYPE, tokenType)
    }


    fun removeTokenType() {
        remove(KEY_TOKEN_TYPE)
    }


    fun getTokenType(): String {
        return get(KEY_TOKEN_TYPE, "")
    }


    fun hasTokenType(): Boolean {
        return getTokenType().isNotBlank()
    }


    fun saveAccessToken(accessToken: String) {
        put(KEY_ACCESS_TOKEN, accessToken)
    }


    fun removeAccessToken() {
        remove(KEY_ACCESS_TOKEN)
    }


    fun getAccessToken(): String {
        return get(KEY_ACCESS_TOKEN, "")
    }


    fun hasAccessToken(): Boolean {
        return getAccessToken().isNotBlank()
    }


    fun saveRefreshToken(refreshToken: String) {
        put(KEY_REFRESH_TOKEN, refreshToken)
    }


    fun removeRefreshToken() {
        remove(KEY_REFRESH_TOKEN)
    }


    fun getRefreshToken(): String {
        return get(KEY_REFRESH_TOKEN, "")
    }


    fun hasRefreshToken(): Boolean {
        return getRefreshToken().isNotBlank()
    }


    fun saveAccessTokenExpirationTime(accessTokenExpirationTime: Long) {
        put(KEY_ACCESS_TOKEN_EXPIRATION_TIME, accessTokenExpirationTime)
    }


    fun removeAccessTokenExpirationTime() {
        remove(KEY_ACCESS_TOKEN_EXPIRATION_TIME)
    }


    fun getAccessTokenExpirationTime(): Long {
        return get(KEY_ACCESS_TOKEN_EXPIRATION_TIME, 0L)
    }


    fun hasAccessTokenExpirationTime(): Boolean {
        return (getAccessTokenExpirationTime() >= 0L)
    }


    fun saveRefreshTokenExpirationTime(refreshTokenExpirationTime: Long) {
        put(KEY_REFRESH_TOKEN_EXPIRATION_TIME, refreshTokenExpirationTime)
    }


    fun removeRefreshTokenExpirationTime() {
        remove(KEY_REFRESH_TOKEN_EXPIRATION_TIME)
    }


    fun getRefreshTokenExpirationTime(): Long {
        return get(KEY_REFRESH_TOKEN_EXPIRATION_TIME, 0L)
    }


    fun hasRefreshTokenExpirationTime(): Boolean {
        return (getRefreshTokenExpirationTime() >= 0L)
    }


    fun saveFingerprintAttemptsUsedUp(areFingerprintAttemptsUsedUp: String) {
        put(KEY_ARE_FINGERPRINT_ATTEMPTS_USED_UP, areFingerprintAttemptsUsedUp)
    }


    fun removeFingerprintAttemptsUsedUp() {
        remove(KEY_ARE_FINGERPRINT_ATTEMPTS_USED_UP)
    }


    fun areFingerprintAttemptsUsedUp(): String {
        return get(KEY_ARE_FINGERPRINT_ATTEMPTS_USED_UP, "")
    }


    fun hasFingerprintAttemptsUsedUp(): Boolean {
        return areFingerprintAttemptsUsedUp().isNotBlank()
    }


    fun saveLastAuthTimestamp(lastAuthTimestamp: String) {
        put(KEY_LAST_AUTH_TIMESTAMP, lastAuthTimestamp)
    }


    fun removeLastAuthTimestamp() {
        remove(KEY_LAST_AUTH_TIMESTAMP)
    }


    fun getLastAuthTimestamp(): String {
        return get(KEY_LAST_AUTH_TIMESTAMP, "")
    }


    fun hasLastAuthTimestamp(): Boolean {
        return getLastAuthTimestamp().isNotBlank()
    }


    fun saveInvalidPinCodeAttemptsNumber(invalidPinCodeAttemptsNumber: String) {
        put(KEY_INVALID_PIN_CODES_ATTEMPTS_NUMBER, invalidPinCodeAttemptsNumber)
    }


    fun removeInvalidPinCodeAttemptsNumber() {
        remove(KEY_INVALID_PIN_CODES_ATTEMPTS_NUMBER)
    }


    fun getInvalidPinCodeAttemptsNumber(): String {
        return get(KEY_INVALID_PIN_CODES_ATTEMPTS_NUMBER, "")
    }


    fun hasInvalidPinCodeAttemptsNumber(): Boolean {
        return getInvalidPinCodeAttemptsNumber().isNotBlank()
    }


    fun setAppStateStatus(appState: String) {
        put(KEY_APP_STATE_STATUS, appState)
    }


    fun getAppStateStatus(): String {
        return get(KEY_APP_STATE_STATUS, "")
    }


    fun saveAllowAuthTimestamp(allowAuthTimestamp: String) {
        put(KEY_ALLOW_AUTH_TIMESTAMP, allowAuthTimestamp)
    }


    fun removeAllowAuthTimestamp() {
        remove(KEY_ALLOW_AUTH_TIMESTAMP)
    }


    fun getAllowAuthTimestamp(): String {
        return get(KEY_ALLOW_AUTH_TIMESTAMP, "")
    }


    fun hasAllowAuthTime(): Boolean {
        return getAllowAuthTimestamp().isNotBlank()
    }


    fun saveInitialAppLanguageSelected(isSelected: Boolean) {
        put(KEY_IS_INITIAL_APP_LANGUAGE_SELECTED, isSelected)
    }


    fun removeInitialAppLanguageSelected() {
        remove(KEY_IS_INITIAL_APP_LANGUAGE_SELECTED)
    }


    fun isInitialAppLanguageSelected(): Boolean {
        return get(KEY_IS_INITIAL_APP_LANGUAGE_SELECTED, false)
    }


    fun saveIntercomUnidentifiableUserRegistered(isRegistered: Boolean) {
        put(KEY_IS_INTERCOM_UNIDENTIFIABLE_USER_REGISTERED, isRegistered)
    }


    fun removeIntercomUnidentifiableUserRegistered() {
        remove(KEY_IS_INTERCOM_UNIDENTIFIABLE_USER_REGISTERED)
    }


    fun isIntercomUnidentifiableUserRegistered(): Boolean {
        return get(KEY_IS_INTERCOM_UNIDENTIFIABLE_USER_REGISTERED, false)
    }


    fun saveIntercomIdentifiableUserRegistered(isRegistered: Boolean) {
        put(KEY_IS_INTERCOM_IDENTIFIABLE_USER_REGISTERED, isRegistered)
    }


    fun removeIntercomIdentifiableUserRegistered() {
        remove(KEY_IS_INTERCOM_IDENTIFIABLE_USER_REGISTERED)
    }


    fun isIntercomIdentifiableUserRegistered(): Boolean {
        return get(KEY_IS_INTERCOM_IDENTIFIABLE_USER_REGISTERED, false)
    }


    fun saveFirebasePushToken(token: String) = put(KEY_FIREBASE_PUSH_NOTIFICATION_TOKEN, token)


    fun getFirebasePushToken(): String = get(KEY_FIREBASE_PUSH_NOTIFICATION_TOKEN, "")


    fun removeFirebasePushToken() = put(KEY_FIREBASE_PUSH_NOTIFICATION_TOKEN, "")


    fun saveInboxUnreadCount(count: Int) = put(KEY_INBOX_UNREAD_COUNT_ITEM, count)


    fun getInboxUnreadCount(): Int = get(KEY_INBOX_UNREAD_COUNT_ITEM, 0)


    fun saveLastWorkingApiBaseUrl(lastWorkingApiBaseUrl: String) {
        put(KEY_LAST_WORKING_API_BASE_URL, lastWorkingApiBaseUrl)
    }


    fun removeLastWorkingApiBaseUrl() {
        remove(KEY_LAST_WORKING_API_BASE_URL)
    }


    fun getLastWorkingApiBaseUrl(): String {
        return get(KEY_LAST_WORKING_API_BASE_URL, "")
    }


    fun hasLastWorkingApiBaseUrl(): Boolean {
        return getLastWorkingApiBaseUrl().isNotBlank()
    }


    fun saveLastWorkingRssUrl(lastWorkingSocketUrl: String) {
        put(KEY_LAST_WORKING_RSS_URL, lastWorkingSocketUrl)
    }


    fun removeLastWorkingRssUrl() {
        remove(KEY_LAST_WORKING_RSS_URL)
    }


    fun getLastWorkingRssUrl(): String {
        return get(KEY_LAST_WORKING_RSS_URL, "")
    }


    fun hasLastWorkingRssUrl(): Boolean {
        return getLastWorkingRssUrl().isNotBlank()
    }


    fun saveLastWorkingSocketUrl(lastWorkingSocketUrl: String) {
        put(KEY_LAST_WORKING_SOCKET_URL, lastWorkingSocketUrl)
    }


    fun removeLastWorkingSocketUrl() {
        remove(KEY_LAST_WORKING_SOCKET_URL)
    }


    fun getLastWorkingSocketUrl(): String {
        return get(KEY_LAST_WORKING_SOCKET_URL, "")
    }


    fun hasLastWorkingSocketUrl(): Boolean {
        return getLastWorkingSocketUrl().isNotBlank()
    }


    fun saveLastWorkingHashedDomainName(lastWorkingSocketUrl: String) {
        put(KEY_LAST_WORKING_HASHED_DOMAIN_NAME, lastWorkingSocketUrl)
    }


    fun removeLastWorkingHashedDomainName() {
        remove(KEY_LAST_WORKING_HASHED_DOMAIN_NAME)
    }


    fun getLastWorkingHashedDomainName(): String {
        return get(KEY_LAST_WORKING_HASHED_DOMAIN_NAME, "")
    }


    fun hasLastWorkingHashedDomainName(): Boolean {
        return getLastWorkingHashedDomainName().isNotBlank()
    }


    fun getLastNotificationId(): Int {
        return get(KEY_LAST_NOTIFICATION_ID, 0)
    }


    fun setLastNotificationId(id: Int) {
        put(KEY_LAST_NOTIFICATION_ID, id)
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