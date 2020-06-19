package com.stocksexchange.android.ui.settings

import com.stocksexchange.android.BuildConfig
import com.stocksexchange.android.Constants
import com.stocksexchange.android.R
import com.stocksexchange.api.model.rest.NotificationStatus
import com.stocksexchange.api.model.rest.ProfileInfo
import com.stocksexchange.android.data.repositories.notification.NotificationRepository
import com.stocksexchange.android.model.*
import com.stocksexchange.android.data.repositories.settings.SettingsRepository
import com.stocksexchange.android.ui.base.mvp.model.BaseModel
import com.stocksexchange.android.utils.handlers.UserDataClearingHandler
import com.stocksexchange.android.utils.extensions.log
import com.stocksexchange.core.handlers.PreferenceHandler
import com.stocksexchange.core.providers.FingerprintProvider

class SettingsModel(
    private val settingsRepository: SettingsRepository,
    private val fingerprintProvider: FingerprintProvider,
    private val userDataClearingHandler: UserDataClearingHandler,
    private val notificationRepository: NotificationRepository,
    private val preferenceHandler: PreferenceHandler
) : BaseModel<SettingsModel.ActionListener>() {


    companion object {

        const val REQUEST_TYPE_NOTIFICATION_STATUS = 0

    }




    fun getSettingItems(settings: Settings, profileInfo: ProfileInfo?): MutableList<Any> {
        return mutableListOf<Any>().apply {
            if(profileInfo != null) {
                addAccountSectionItems(settings, profileInfo)
            }

            addGeneralSectionItems(settings)
            addAppearanceSectionItems(settings)

            if(BuildConfig.DEBUG) {
                addDebugSectionItems(settings)
            }
        }
    }


    private fun MutableList<Any>.addAccountSectionItems(settings: Settings, profileInfo: ProfileInfo) {
        add(SettingAccount(SettingId.SIGN_OUT, profileInfo.emailUserName, profileInfo.userName))

        add(getItemForId(SettingId.CHANGE_PIN, settings))
        add(getItemForId(SettingId.AUTHENTICATION_SESSION_DURATION, settings))

        if(fingerprintProvider.isHardwareAvailable()) {
            add(getItemForId(SettingId.FINGERPRINT_UNLOCK, settings))
        }

        add(getItemForId(SettingId.FORCE_AUTHENTICATION_ON_APP_STARTUP, settings))
        add(getItemForId(SettingId.PUSH_NOTIFICATION, settings))

        if(Constants.IMPLEMENTATION_NOTIFICATION_TURN_ON) {
            add(getItemForId(SettingId.NOTIFICATION_REPORT, settings))
        }
    }


    private fun MutableList<Any>.addGeneralSectionItems(settings: Settings) {
        add(SettingSection(mStringProvider.getString(R.string.general)))
        add(getItemForId(SettingId.APP_VERSION, settings))

        if(BuildConfig.IS_NORMAL_FLAVOR) {
            add(getItemForId(SettingId.LANGUAGE, settings))
        }

        add(getItemForId(SettingId.FIAT_CURRENCY, settings))
        add(getItemForId(SettingId.KEEP_SCREEN_ON, settings))
        add(getItemForId(SettingId.RESTORE_DEFAULTS, settings))
    }


    private fun MutableList<Any>.addAppearanceSectionItems(settings: Settings) {
        add(SettingSection(mStringProvider.getString(R.string.appearance)))

        add(getItemForId(SettingId.ANIMATE_CHARTS, settings))
        add(getItemForId(SettingId.BULLISH_CANDLE_STICK_STYLE, settings))
        add(getItemForId(SettingId.BEARISH_CANDLE_STICK_STYLE, settings))
        add(getItemForId(SettingId.ZOOM_IN_ON_PRICE_CHART, settings))
        add(getItemForId(SettingId.DEPTH_CHART_LINE_STYLE, settings))
        add(getItemForId(SettingId.HIGHLIGHT_ORDERBOOK_REAL_TIME_UPDATES, settings))
        add(getItemForId(SettingId.HIGHLIGHT_NEW_TRADES_REAL_TIME_ADDITION, settings))
    }


    private fun MutableList<Any>.addDebugSectionItems(settings: Settings) {
        add(SettingSection(mStringProvider.getString(R.string.debug)))
        add(getItemForId(SettingId.DEVICE_METRICS, settings))
        add(getItemForId(SettingId.CLEAR_USER_DATA, settings))
        add(getItemForId(SettingId.ANALYTICS_TOASTS, settings))
    }


    fun updateSettings(settings: Settings, onFinish: (() -> Unit)) {
        createUiLaunchCoroutine {
            settingsRepository.save(settings)

            onFinish()
        }
    }


    fun clearAllUserData(onFinish: suspend (() -> Unit)) {
        createUiLaunchCoroutine {
            userDataClearingHandler.clearAllUserData(onFinish)
        }
    }


    fun clearPrivateUserData(onFinish: suspend (() -> Unit)) {
        createUiLaunchCoroutine {
            userDataClearingHandler.clearPrivateUserData(onFinish)
        }
    }


    fun getItemForId(id: SettingId, settings: Settings): Setting {
        return when(id) {

            SettingId.CHANGE_PIN -> {
                Setting(
                    id = id,
                    title = mStringProvider.getString(R.string.change_pin),
                    description = mStringProvider.getString(R.string.settings_fragment_change_pin_description)
                )
            }

            SettingId.AUTHENTICATION_SESSION_DURATION -> {
                Setting(
                    id = id,
                    title = mStringProvider.getString(R.string.authentication_session_duration),
                    description = mStringProvider.getString(settings.authenticationSessionDuration.titleId)
                )
            }

            SettingId.FINGERPRINT_UNLOCK -> {
                Setting(
                    id = id,
                    isCheckable = true,
                    isChecked = settings.isFingerprintUnlockEnabled,
                    title = mStringProvider.getString(R.string.fingerprint_unlock),
                    description = mStringProvider.getString(R.string.settings_fragment_fingerprint_unlock_description)
                )
            }

            SettingId.FORCE_AUTHENTICATION_ON_APP_STARTUP -> {
                Setting(
                    id = id,
                    isCheckable = true,
                    isChecked = settings.isForceAuthenticationOnAppStartupEnabled,
                    title = mStringProvider.getString(R.string.force_authentication_on_app_startup),
                    description = mStringProvider.getString(R.string.settings_fragment_force_authentication_description)
                )
            }

            SettingId.APP_VERSION -> {
                Setting(
                    id = id,
                    title = mStringProvider.getString(R.string.app_version_template, BuildConfig.VERSION_NAME),
                    description = mStringProvider.getString(R.string.app_update_state_up_to_date_version_text)
                )
            }

            SettingId.LANGUAGE -> {
                Setting(
                    id = id,
                    title = mStringProvider.getString(R.string.language),
                    description = mStringProvider.getString(settings.language.nameStringId)
                )
            }

            SettingId.FIAT_CURRENCY -> {
                Setting(
                    id = id,
                    title = mStringProvider.getString(R.string.currency),
                    description = mStringProvider.getString(settings.fiatCurrency.stringId)
                )
            }

            SettingId.KEEP_SCREEN_ON -> {
                Setting(
                    id = id,
                    isCheckable = true,
                    isChecked = settings.shouldKeepScreenOn,
                    title = mStringProvider.getString(R.string.keep_screen_on),
                    description = mStringProvider.getString(R.string.settings_fragment_keep_screen_on_description)
                )
            }

            SettingId.RESTORE_DEFAULTS -> {
                Setting(
                    id = id,
                    title = mStringProvider.getString(R.string.restore_defaults),
                    description = mStringProvider.getString(R.string.settings_fragment_restore_defaults_description)
                )
            }

            SettingId.PUSH_NOTIFICATION -> {
                Setting(
                    id = id,
                    isCheckable = true,
                    isChecked = settings.isNotificationEnabled,
                    title = mStringProvider.getString(R.string.notifications),
                    description = mStringProvider.getString(R.string.settings_fragment_notification_more_options_on_site)
                )
            }

            SettingId.NOTIFICATION_REPORT -> {
                Setting(
                    id = id,
                    title = mStringProvider.getString(R.string.inbox_notification_report),
                    description = getNotificationReportText(),
                    customDescriptionColor = isNotificationReportCustomDescriptionColor()
                )
            }

            SettingId.ANIMATE_CHARTS -> {
                Setting(
                    id = id,
                    isCheckable = true,
                    isChecked = settings.shouldAnimateCharts,
                    title = mStringProvider.getString(R.string.animate_charts),
                    description = mStringProvider.getString(R.string.settings_fragment_animate_charts_description)
                )
            }

            SettingId.BULLISH_CANDLE_STICK_STYLE -> {
                Setting(
                    id = id,
                    title = mStringProvider.getString(R.string.bullish_candle_stick_style),
                    description = mStringProvider.getString(settings.bullishCandleStickStyle.titleId),
                    tag = CandleStickType.BULLISH
                )
            }

            SettingId.BEARISH_CANDLE_STICK_STYLE -> {
                Setting(
                    id = id,
                    title = mStringProvider.getString(R.string.bearish_candle_stick_style),
                    description = mStringProvider.getString(settings.bearishCandleStickStyle.titleId),
                    tag = CandleStickType.BEARISH
                )
            }

            SettingId.ZOOM_IN_ON_PRICE_CHART -> {
                Setting(
                    id = id,
                    isCheckable = true,
                    isChecked = settings.isPriceChartZoomInEnabled,
                    title = mStringProvider.getString(R.string.zoom_in_on_the_price_chart),
                    description = mStringProvider.getString(R.string.settings_fragment_zoom_in_on_the_price_chart_description)
                )
            }

            SettingId.DEPTH_CHART_LINE_STYLE -> {
                Setting(
                    id = id,
                    title = mStringProvider.getString(R.string.depth_chart_line_style),
                    description = mStringProvider.getString(settings.depthChartLineStyle.titleId)
                )
            }

            SettingId.HIGHLIGHT_ORDERBOOK_REAL_TIME_UPDATES -> {
                Setting(
                    id = id,
                    isCheckable = true,
                    isChecked = settings.isOrderbookRealTimeUpdatesHighlightingEnabled,
                    title = mStringProvider.getString(R.string.highlight_orderbook_real_time_updates),
                    description = mStringProvider.getString(R.string.settings_fragment_highlight_orderbook_real_time_updates_description)
                )
            }

            SettingId.HIGHLIGHT_NEW_TRADES_REAL_TIME_ADDITION -> {
                Setting(
                    id = id,
                    isCheckable = true,
                    isChecked = settings.isNewTradesRealTimeAdditionHighlightingEnabled,
                    title = mStringProvider.getString(R.string.highlight_new_trades_real_time_addition),
                    description = mStringProvider.getString(R.string.settings_fragment_highlight_new_trades_real_time_addition_description)
                )
            }

            SettingId.DEVICE_METRICS -> {
                Setting(
                    id = id,
                    title = mStringProvider.getString(R.string.device_metrics),
                    description = mStringProvider.getString(R.string.device_metrics_description)
                )
            }

            SettingId.CLEAR_USER_DATA -> {
                Setting(
                    id = id,
                    title = mStringProvider.getString(R.string.clear_user_data),
                    description = mStringProvider.getString(R.string.clear_user_data_description)
                )
            }

            SettingId.ANALYTICS_TOASTS -> {
                Setting(
                    id = id,
                    isCheckable = true,
                    isChecked = settings.areAnalyticsToastsEnabled,
                    title = mStringProvider.getString(R.string.show_analytics_toasts),
                    description = mStringProvider.getString(R.string.show_toasts_of_analytics_events)
                )
            }

            else -> throw IllegalStateException()
        }
    }


    fun getDecimalSeparatorAutomaticChangeString(decimalSeparator: DecimalSeparator,
                                                 groupingSeparator: GroupingSeparator): String {
        return mStringProvider.getString(
            R.string.settings_fragment_decimal_separator_automatic_change,
            getAutomaticChangeStringFirstArg(decimalSeparator.separator),
            getAutomaticChangeStringSecondArg(groupingSeparator.separator)
        )
    }


    fun getGroupingSeparatorAutomaticChangeString(groupingSeparator: GroupingSeparator,
                                                  decimalSeparator: DecimalSeparator): String {
        return mStringProvider.getString(
            R.string.settings_fragment_grouping_separator_automatic_change,
            getAutomaticChangeStringFirstArg(groupingSeparator.separator),
            getAutomaticChangeStringSecondArg(decimalSeparator.separator)
        )
    }


    private fun getAutomaticChangeStringFirstArg(separator: Char): String {
        return mStringProvider.getString(when(separator) {
            DecimalSeparator.PERIOD.separator -> R.string.settings_fragment_automatic_change_first_arg_period
            DecimalSeparator.COMMA.separator -> R.string.settings_fragment_automatic_change_first_arg_comma

            else -> throw IllegalStateException("Please provide an implementation for $separator")
        })
    }


    private fun getAutomaticChangeStringSecondArg(separator: Char): String {
        return mStringProvider.getString(when(separator) {
            DecimalSeparator.PERIOD.separator -> R.string.settings_fragment_automatic_change_second_arg_period
            DecimalSeparator.COMMA.separator -> R.string.settings_fragment_automatic_change_second_arg_comma

            else -> throw IllegalStateException("Please provide an implementation for $separator")
        })
    }


    fun updateNotificationStatus(status: NotificationStatus) {
        performRequest(
            requestType = REQUEST_TYPE_NOTIFICATION_STATUS,
            params = status
        )
    }


    override suspend fun getRequestRepositoryResult(requestType: Int, params: Any): Any? {
        return when(requestType) {
            REQUEST_TYPE_NOTIFICATION_STATUS -> {
                notificationRepository.updateNotificationStatus(params as NotificationStatus).apply {
                    log("updateNotificationStatus(params: $params)")
                }
            }

            else -> throw IllegalStateException()
        }
    }


    fun getNotificationReportText(): String {
        return when (val inboxUnreadCount = preferenceHandler.getInboxUnreadCount()) {
            0 -> mStringProvider.getString(R.string.inbox_no_notifications)
            else -> String.format(mStringProvider.getString(R.string.inbox_new_notifications), inboxUnreadCount)
        }
    }


    fun isNotificationReportCustomDescriptionColor(): Boolean {
        return (preferenceHandler.getInboxUnreadCount() > 0)
    }


    interface ActionListener : BaseActionListener


}