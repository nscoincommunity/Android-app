package com.stocksexchange.android.ui.splash

import com.stocksexchange.android.R
import com.stocksexchange.api.model.rest.CurrencyPairGroup
import com.stocksexchange.api.model.rest.ProfileInfo
import com.stocksexchange.api.interceptors.HostInterceptor
import com.stocksexchange.android.di.STEX_OK_HTTP_CLIENT_REST
import com.stocksexchange.android.di.utils.get
import com.stocksexchange.android.factories.SettingsFactory
import com.stocksexchange.android.model.*
import com.stocksexchange.android.notification.FirebasePushClient
import com.stocksexchange.android.ui.base.mvp.presenters.BasePresenter
import com.stocksexchange.android.utils.RemoteServiceUrlFinder
import com.stocksexchange.android.utils.handlers.IntercomHandler
import com.stocksexchange.android.utils.handlers.ShortcutsHandler
import com.stocksexchange.android.utils.managers.AppLockManager
import com.stocksexchange.android.utils.managers.SessionManager
import com.stocksexchange.android.utils.providers.InitialLanguageProvider
import com.stocksexchange.api.utils.CredentialsHandler
import com.stocksexchange.core.formatters.NumberFormatter
import com.stocksexchange.core.formatters.TimeFormatter
import com.stocksexchange.core.handlers.PreferenceHandler

class SplashPresenter(
    view: SplashContract.View,
    model: SplashModel,
    private val initialLanguageProvider: InitialLanguageProvider,
    private val timeFormatter: TimeFormatter,
    private val numberFormatter: NumberFormatter,
    private val shortcutsHandler: ShortcutsHandler,
    private val credentialsHandler: CredentialsHandler,
    private val intercomHandler: IntercomHandler,
    private val preferenceHandler: PreferenceHandler,
    private val settingsFactory: SettingsFactory,
    private val sessionManager: SessionManager,
    private val remoteServiceUrlFinder: RemoteServiceUrlFinder,
    private val restHostInterceptor: HostInterceptor,
    private val rssHostIntereptor: HostInterceptor,
    private val appLockManager: AppLockManager,
    private val firebasePushClient: FirebasePushClient
) : BasePresenter<SplashContract.View, SplashModel>(view, model),
    SplashContract.ActionListener, SplashModel.ActionListener {


    var shortcut: Shortcut? = null




    init {
        model.setActionListener(this)
    }


    override fun start() {
        super.start()

        initApp()
    }


    private fun initApp() {
        initSettings()
    }


    private fun initSettings() {
        mModel.performSettingsRetrievalRequest()
    }


    @Suppress("UNCHECKED_CAST")
    override fun onRequestSucceeded(requestType: Int, response: Any, metadata: Any) {
        when(requestType) {

            SplashModel.REQUEST_TYPE_SETTINGS_RETRIEVAL -> {
                onSettingsRetrievalSucceeded(response as Settings)
            }

            SplashModel.REQUEST_TYPE_PROFILE_INFO_RETRIEVAL -> {
                onProfileInfoRetrievalSucceeded(response as ProfileInfo)
            }

            SplashModel.REQUEST_TYPE_CURRENCY_PAIR_GROUPS_RETRIEVAL -> {
                onCurrencyPairGroupsRetrievalSucceeded(response as List<CurrencyPairGroup>)
            }

            SplashModel.REQUEST_TYPE_FAVORITE_CURRENCY_PAIRS_COUNT_RETRIEVAL -> {
                onFavoriteCurrencyPairsCountRetrievalSucceeded(response as Int)
            }

        }
    }


    override fun onRequestFailed(requestType: Int, exception: Throwable, metadata: Any) {
        when(requestType) {

            SplashModel.REQUEST_TYPE_SETTINGS_RETRIEVAL -> {
                onSettingsRetrievalFailed()
            }

            SplashModel.REQUEST_TYPE_PROFILE_INFO_RETRIEVAL -> {
                onProfileInfoRetrievalFailed()
            }

            SplashModel.REQUEST_TYPE_CURRENCY_PAIR_GROUPS_RETRIEVAL -> {
                onCurrencyPairGroupsRetrievalFailed()
            }

            SplashModel.REQUEST_TYPE_FAVORITE_CURRENCY_PAIRS_COUNT_RETRIEVAL -> {
                onFavoriteCurrencyPairsCountRetrievalFailed()
            }

        }
    }


    private fun onSettingsRetrievalSucceeded(originalSettings: Settings) {
        val updatedSettings = applySettingsUpdates(originalSettings)

        // No need to save if identical
        if(originalSettings == updatedSettings) {
            sessionManager.setSettings(originalSettings)

            initStuffAfterSettingsInitialization(originalSettings)
        } else {
            saveSettings(updatedSettings)
        }
    }


    private fun onSettingsRetrievalFailed() {
        saveSettings(applySettingsUpdates(settingsFactory.getDefaultSettings()))
    }


    private fun applySettingsUpdates(settings: Settings): Settings {
        return applyInitialAppLanguageUpdate(settings)
    }


    private fun applyInitialAppLanguageUpdate(settings: Settings): Settings {
        if(preferenceHandler.isInitialAppLanguageSelected()) {
            return settings
        }

        preferenceHandler.saveInitialAppLanguageSelected(true)

        val initialAppLanguage = initialLanguageProvider.getInitialLanguage()
        val updatedSettings = settings.copy(language = initialAppLanguage)

        return updatedSettings
    }


    private fun saveSettings(settings: Settings) {
        mModel.saveSettings(settings) {
            sessionManager.setSettings(settings)

            initStuffAfterSettingsInitialization(settings)
        }
    }


    private fun initStuffAfterSettingsInitialization(settings: Settings) {
        initStringProvider(settings)
        initTimeFormatter(settings)
        initShortcuts()
        initRemoteServicesUrls()
    }


    private fun initStringProvider(settings: Settings) {
        mStringProvider.setLocale(settings.language.locale)
    }


    private fun initTimeFormatter(settings: Settings) {
        timeFormatter.setLocale(settings.language.locale)
    }


    private fun initShortcuts() {
        shortcutsHandler.setupShortcuts()
    }


    private fun initRemoteServicesUrls() {
        val listener = object : RemoteServiceUrlFinder.Listener {

            override fun onFindingProcessStarted() {
                mView.showInfoViewContainer(mStringProvider.getString(R.string.connecting))
            }

            override fun onFindingProcessEnded() {
                mView.hideInfoViewContainer()
            }

            override fun onFindingProcessSucceeded(workingUrls: RemoteServiceUrls) {
                onRemoteServicesUrlsFound(
                    apiBaseUrl = workingUrls.apiBaseUrl,
                    rssUrl = workingUrls.rssUrl,
                    socketUrl = workingUrls.socketUrl
                )
            }

            override fun onFindingProcessFailed() {
                mView.showMaterialDialog(MaterialDialogBuilder.confirmationDialog(
                    title = mStringProvider.getString(R.string.note),
                    content = mStringProvider.getString(R.string.splash_could_not_connect_dialog_content),
                    positiveBtnText = mStringProvider.getString(R.string.ok),
                    isCancelable = false,
                    positiveBtnClick = {
                        mView.finishActivity()
                    }
                ))
            }

        }

        remoteServiceUrlFinder.listener = listener
        remoteServiceUrlFinder.run()
    }


    private fun onRemoteServicesUrlsFound(apiBaseUrl: String, rssUrl: String, socketUrl: String) {
        restHostInterceptor.setHost(apiBaseUrl)
        rssHostIntereptor.setHost(rssUrl)
        mSocketConnection.initSocket(socketUrl, get(STEX_OK_HTTP_CLIENT_REST))

        proceedAfterRemoteServicesUrlsInitialization()
    }


    private fun proceedAfterRemoteServicesUrlsInitialization() {
        if(credentialsHandler.hasEmail()) {
            mModel.performProfileInfoRetrievalRequest(credentialsHandler.getEmail())
        } else {
            initIntercomUserRegistration(null)
        }
    }


    private fun onProfileInfoRetrievalSucceeded(profileInfo: ProfileInfo) {
        sessionManager.setProfileInfo(profileInfo)

        initIntercomUserRegistration(profileInfo)
        setNotificationStatus(profileInfo)
        updateNotificationToken()
        getInboxUnreadCount()
    }


    private fun onProfileInfoRetrievalFailed() {
        initIntercomUserRegistration(null)
    }


    private fun initIntercomUserRegistration(profileInfo: ProfileInfo?) {
        if(profileInfo == null) {
            if(preferenceHandler.isIntercomUnidentifiableUserRegistered()) {
                intercomHandler.registerUnidentifiableUser()
            }
        } else {
            if(preferenceHandler.isIntercomIdentifiableUserRegistered()) {
                intercomHandler.registerIdentifiableUser(profileInfo.email)
            }
        }

        initCurrencyPairGroupsInitialization()
    }


    private fun initCurrencyPairGroupsInitialization() {
        mModel.performCurrencyPairGroupsRetrieval()
    }


    private fun onCurrencyPairGroupsRetrievalSucceeded(currencyPairGroups: List<CurrencyPairGroup>) {
        sessionManager.setCurrencyPairGroups(currencyPairGroups)

        initFavoriteCurrencyPairsCount()
    }


    private fun onCurrencyPairGroupsRetrievalFailed() {
        sessionManager.setCurrencyPairGroups(CurrencyPairGroup.DEFAULT_GROUPS)

        initFavoriteCurrencyPairsCount()
    }


    private fun initFavoriteCurrencyPairsCount() {
        mModel.performFavoriteCurrencyPairsCountRetrieval()
    }


    private fun onFavoriteCurrencyPairsCountRetrievalSucceeded(count: Int) {
        sessionManager.setFavoriteCurrencyPairsCount(count)

        proceedToNextScreen()
    }


    private fun onFavoriteCurrencyPairsCountRetrievalFailed() {
        proceedToNextScreen()
    }


    private fun proceedToNextScreen() {
        when {
            shouldSignIn() -> launchLoginActivityAndFinish()
            shouldAuthenticate() -> launchAuthActivityAndFinish()

            else -> launchDestinationActivityAndFinish()
        }
    }


    private fun shouldSignIn(): Boolean {
        return ((shortcut?.requiresUserPresence == true) &&
                !sessionManager.isUserSignedIn())
    }


    private fun launchLoginActivityAndFinish() {
        mView.launchLoginActivity()
        mView.finishActivity()
    }


    private fun shouldAuthenticate(): Boolean {
        val isForceAuthenticationOnAppStartupEnabled = sessionManager.getSettings().isForceAuthenticationOnAppStartupEnabled

        return (appLockManager.shouldAuthenticate() ||
                (appLockManager.isUserPresent() && isForceAuthenticationOnAppStartupEnabled))
    }


    private fun launchAuthActivityAndFinish() {
        mView.launchAuthenticationActivity(if(sessionManager.getSettings().hasPinCode) {
            PinCodeMode.ENTER
        } else {
            PinCodeMode.CREATION
        })
        mView.finishActivity()
    }


    private fun launchDestinationActivityAndFinish() {
        mView.launchDestinationActivity()
        mView.finishActivity()
    }


    private fun updateNotificationToken() {
        firebasePushClient.updateNotificationToken()
    }


    private fun setNotificationStatus(profileInfo: ProfileInfo?) {
        if (profileInfo == null) {
            return
        }

        val newSettings = sessionManager.getSettings().copy(
            isNotificationEnabled = profileInfo.getNotificationStatus()
        )

        mModel.saveSettings(newSettings) {
            sessionManager.setSettings(newSettings)
        }
    }


    private fun getInboxUnreadCount() {
        firebasePushClient.getInboxUnreadCount()
    }


}