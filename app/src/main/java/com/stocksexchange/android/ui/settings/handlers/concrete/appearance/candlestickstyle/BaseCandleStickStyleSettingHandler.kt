package com.stocksexchange.android.ui.settings.handlers.concrete.appearance.candlestickstyle

import com.stocksexchange.android.model.*
import com.stocksexchange.android.ui.settings.SettingsContract
import com.stocksexchange.android.ui.settings.SettingsModel
import com.stocksexchange.android.ui.settings.handlers.BaseSettingHandler
import com.stocksexchange.android.utils.managers.SessionManager
import com.stocksexchange.android.utils.providers.StringProvider

abstract class BaseCandleStickStyleSettingHandler(
    view: SettingsContract.View,
    model: SettingsModel,
    sessionManager: SessionManager,
    private val stringProvider: StringProvider
) : BaseSettingHandler<Setting>(view, model, sessionManager) {


    override fun onSettingClicked(setting: Setting) {
        showCandleStickStyleDialog(setting.tag as CandleStickType)
    }


    private fun showCandleStickStyleDialog(type: CandleStickType) {
        val items = CandleStickStyle.values()
            .map { stringProvider.getString(it.titleId) }
            .toTypedArray()

        view.showMaterialDialog(MaterialDialogBuilder.listDialog(
            items = items,
            itemsCallback = {
                onCandleStickStylePicked(it, type)
            }
        ))
    }


    private fun onCandleStickStylePicked(style: String, type: CandleStickType) {
        val newCandleStickStyle = CandleStickStyle.values().first {
            style == stringProvider.getString(it.titleId)
        }
        val settingId = when(type) {
            CandleStickType.BULLISH -> SettingId.BULLISH_CANDLE_STICK_STYLE
            CandleStickType.BEARISH -> SettingId.BEARISH_CANDLE_STICK_STYLE
        }
        val oldSettings = sessionManager.getSettings()
        val oldCandleStickStyle = when(type) {
            CandleStickType.BULLISH -> oldSettings.bullishCandleStickStyle
            CandleStickType.BEARISH -> oldSettings.bearishCandleStickStyle
        }

        if(newCandleStickStyle == oldCandleStickStyle) {
            return
        }

        val onFinish: ((Settings, Settings) -> Unit) = { newSettings, _ ->
            view.updateSettingWith(model.getItemForId(settingId, newSettings))
        }

        updateSettings(onFinish) {
            when(type) {
                CandleStickType.BULLISH -> it.copy(bullishCandleStickStyle = newCandleStickStyle)
                CandleStickType.BEARISH -> it.copy(bearishCandleStickStyle = newCandleStickStyle)
            }
        }
    }


}