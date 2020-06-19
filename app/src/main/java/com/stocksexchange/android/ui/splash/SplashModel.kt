package com.stocksexchange.android.ui.splash

import com.stocksexchange.android.data.repositories.currencypairgroups.CurrencyPairGroupsRepository
import com.stocksexchange.android.data.repositories.favoritecurrencypairs.FavoriteCurrencyPairsRepository
import com.stocksexchange.android.data.repositories.profileinfos.ProfileInfosRepository
import com.stocksexchange.android.model.Settings
import com.stocksexchange.android.data.repositories.settings.SettingsRepository
import com.stocksexchange.android.utils.extensions.log
import com.stocksexchange.android.ui.base.mvp.model.BaseModel
import com.stocksexchange.android.ui.splash.SplashModel.ActionListener

class SplashModel(
    private val settingsRepository: SettingsRepository,
    private val profileInfosRepository: ProfileInfosRepository,
    private val currencyPairGroupsRepository: CurrencyPairGroupsRepository,
    private val favoriteCurrencyPairsRepository: FavoriteCurrencyPairsRepository
) : BaseModel<ActionListener>() {


    companion object {

        const val REQUEST_TYPE_SETTINGS_RETRIEVAL = 0
        const val REQUEST_TYPE_PROFILE_INFO_RETRIEVAL = 1
        const val REQUEST_TYPE_CURRENCY_PAIR_GROUPS_RETRIEVAL = 2
        const val REQUEST_TYPE_FAVORITE_CURRENCY_PAIRS_COUNT_RETRIEVAL = 3

    }




    fun performSettingsRetrievalRequest() {
        performRequest(
            requestType = REQUEST_TYPE_SETTINGS_RETRIEVAL
        )
    }


    fun performProfileInfoRetrievalRequest(email: String) {
        performRequest(
            requestType = REQUEST_TYPE_PROFILE_INFO_RETRIEVAL,
            params = email
        )
    }


    fun performCurrencyPairGroupsRetrieval() {
        performRequest(
            requestType = REQUEST_TYPE_CURRENCY_PAIR_GROUPS_RETRIEVAL
        )
    }


    fun performFavoriteCurrencyPairsCountRetrieval() {
        performRequest(
            requestType = REQUEST_TYPE_FAVORITE_CURRENCY_PAIRS_COUNT_RETRIEVAL
        )
    }


    override suspend fun getRequestRepositoryResult(requestType: Int, params: Any): Any? {
        return when(requestType) {

            REQUEST_TYPE_SETTINGS_RETRIEVAL -> {
                settingsRepository.get().apply {
                    log("settingsRepository.get()")
                }
            }

            REQUEST_TYPE_PROFILE_INFO_RETRIEVAL -> {
                profileInfosRepository.get(params as String).apply {
                    log("profileInfosRepository.get(params: $params)")
                }
            }

            REQUEST_TYPE_CURRENCY_PAIR_GROUPS_RETRIEVAL -> {
                currencyPairGroupsRepository.getAll().apply {
                    log("currencyPairGroupsRepository.getAll()")
                }
            }

            REQUEST_TYPE_FAVORITE_CURRENCY_PAIRS_COUNT_RETRIEVAL -> {
                favoriteCurrencyPairsRepository.getCount().apply {
                    log("favoriteCurrencyPairsRepository.getCount()")
                }
            }

            else -> throw IllegalStateException()

        }
    }


    fun saveSettings(settings: Settings, onFinish: (() -> Unit)) {
        createUiLaunchCoroutine {
            settingsRepository.save(settings)

            onFinish()
        }
    }


    interface ActionListener : BaseActionListener


}