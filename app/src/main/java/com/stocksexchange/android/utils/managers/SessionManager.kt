package com.stocksexchange.android.utils.managers

import com.stocksexchange.api.model.rest.CurrencyPairGroup
import com.stocksexchange.api.model.rest.ProfileInfo
import com.stocksexchange.android.database.tables.CurrencyPairGroupsTable
import com.stocksexchange.android.database.tables.ProfileInfosTable
import com.stocksexchange.android.database.tables.SettingsTable
import com.stocksexchange.android.factories.SettingsFactory
import com.stocksexchange.core.model.Result
import com.stocksexchange.android.model.Settings
import com.stocksexchange.api.utils.CredentialsHandler
import org.koin.core.KoinComponent
import org.koin.core.get

class SessionManager : KoinComponent {


    private var mWasVerificationPromptDisplayed: Boolean = false

    private var mFavoriteCurrencyPairsCount: Int = 0

    private var mProfileInfo: ProfileInfo? = null

    private var mSettings: Settings? = null

    private var mCurrencyPairGroups: List<CurrencyPairGroup>? = null





    fun setVerificationPromptDisplayed(isDisplayed: Boolean) {
        mWasVerificationPromptDisplayed = isDisplayed
    }


    fun wasVerificationPromptDisplayed(): Boolean {
        return mWasVerificationPromptDisplayed
    }


    fun setFavoriteCurrencyPairsCount(count: Int) {
        mFavoriteCurrencyPairsCount = count
    }


    fun getFavoriteCurrencyPairsCount(): Int {
        return mFavoriteCurrencyPairsCount
    }


    fun setProfileInfo(profileInfo: ProfileInfo) {
        mProfileInfo = profileInfo
    }


    /**
     * Retrieves profile information.
     *
     * @return The profile information or null if there is no
     * profile information available (that is, user has not
     * signed in yet)
     */
    fun getProfileInfo(): ProfileInfo? {
        if(mProfileInfo == null) {
            mProfileInfo = get<CredentialsHandler>().getEmail()
                    .takeIf { it.isNotBlank() }
                    ?.let { email ->
                        get<ProfileInfosTable>().get(email)
                            .takeIf { it is Result.Success }
                            ?.let { (it as Result.Success).value }
                    } ?: ProfileInfo.STUB_PROFILE_INFO
        }

        return if(mProfileInfo?.isStub != false) null else mProfileInfo
    }


    fun isUserSignedIn(): Boolean {
        return (getProfileInfo() != null)
    }


    fun setSettings(settings: Settings) {
        mSettings = settings
    }


    fun getSettings(): Settings {
        if(mSettings == null) {
            mSettings = get<SettingsTable>().get()
                .takeIf { it is Result.Success }
                ?.let { (it as Result.Success).value }
                ?: get<SettingsFactory>().getDefaultSettings()
        }

        return mSettings!!
    }


    fun setCurrencyPairGroups(currencyPairGroups: List<CurrencyPairGroup>) {
        mCurrencyPairGroups = currencyPairGroups
    }


    fun getCurrencyPairGroups(): List<CurrencyPairGroup> {
        if(mCurrencyPairGroups == null) {
            mCurrencyPairGroups = get<CurrencyPairGroupsTable>().getAll()
                .takeIf { it is Result.Success }
                ?.let { (it as Result.Success).value }
                ?: CurrencyPairGroup.DEFAULT_GROUPS
        }

        return mCurrencyPairGroups!!
    }


    fun reset() {
        setVerificationPromptDisplayed(false)
        setProfileInfo(ProfileInfo.STUB_PROFILE_INFO)
    }


}