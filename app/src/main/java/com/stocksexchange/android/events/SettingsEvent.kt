package com.stocksexchange.android.events

import com.stocksexchange.android.model.Settings
import com.stocksexchange.android.utils.helpers.tag

class SettingsEvent private constructor(
    attachment: Settings,
    sourceTag: String,
    val action: Action
) : BaseEvent<Settings>(TYPE_INVALID, attachment, sourceTag) {


    companion object {


        fun changeLanguage(attachment: Settings, source: Any): SettingsEvent {
            return SettingsEvent(attachment, tag(source), Action.LANGUAGE_CHANGED)
        }


        fun changeFiatCurrency(attachment: Settings, source: Any): SettingsEvent {
            return SettingsEvent(attachment, tag(source), Action.FIAT_CURRENCY_CHANGED)
        }


        fun restoreDefaults(attachment: Settings, source: Any): SettingsEvent {
            return SettingsEvent(attachment, tag(source), Action.DEFAULTS_RESTORED)
        }


        fun changeTheme(attachment: Settings, source: Any): SettingsEvent {
            return SettingsEvent(attachment, tag(source), Action.THEME_CHANGED)
        }


        fun changeGroupingState(attachment: Settings, source: Any): SettingsEvent {
            return SettingsEvent(attachment, tag(source), Action.GROUPING_STATE_CHANGED)
        }


        fun changeGroupingSeparator(attachment: Settings, source: Any): SettingsEvent {
            return SettingsEvent(attachment, tag(source), Action.GROUPING_SEPARATOR_CHANGED)
        }


        fun changeDecimalSeparator(attachment: Settings, source: Any): SettingsEvent {
            return SettingsEvent(attachment, tag(source), Action.DECIMAL_SEPARATOR_CHANGED)
        }


    }


    enum class Action {

        LANGUAGE_CHANGED,
        FIAT_CURRENCY_CHANGED,
        THEME_CHANGED,
        DEFAULTS_RESTORED,
        GROUPING_STATE_CHANGED,
        GROUPING_SEPARATOR_CHANGED,
        DECIMAL_SEPARATOR_CHANGED

    }


}