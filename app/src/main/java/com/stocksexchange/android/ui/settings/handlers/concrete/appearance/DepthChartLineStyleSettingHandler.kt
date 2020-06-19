package com.stocksexchange.android.ui.settings.handlers.concrete.appearance

import com.stocksexchange.android.model.*
import com.stocksexchange.android.ui.settings.SettingsContract
import com.stocksexchange.android.ui.settings.SettingsModel
import com.stocksexchange.android.ui.settings.handlers.BaseSettingHandler
import com.stocksexchange.android.utils.managers.SessionManager
import com.stocksexchange.android.utils.providers.StringProvider

class DepthChartLineStyleSettingHandler(
    view: SettingsContract.View,
    model: SettingsModel,
    sessionManager: SessionManager,
    private val stringProvider: StringProvider
) : BaseSettingHandler<Setting>(view, model, sessionManager) {


    override fun onSettingClicked(setting: Setting) {
        showDepthChartLineStyleDialog()
    }


    private fun showDepthChartLineStyleDialog() {
        val items = DepthChartLineStyle.values()
            .map { stringProvider.getString(it.titleId) }
            .toTypedArray()

        view.showMaterialDialog(MaterialDialogBuilder.listDialog(
            items = items,
            itemsCallback = {
                onDepthChartLineStyleItemPicked(it)
            }
        ))
    }


    private fun onDepthChartLineStyleItemPicked(style: String) {
        val newDepthChartLineStyle = DepthChartLineStyle.values().first {
            style == stringProvider.getString(it.titleId)
        }
        val oldSettings = sessionManager.getSettings()

        if(newDepthChartLineStyle == oldSettings.depthChartLineStyle) {
            return
        }

        val onFinish: ((Settings, Settings) -> Unit) = { newSettings, _ ->
            view.updateSettingWith(model.getItemForId(
                SettingId.DEPTH_CHART_LINE_STYLE,
                newSettings
            ))
        }

        updateSettings(onFinish) {
            it.copy(depthChartLineStyle = newDepthChartLineStyle)
        }
    }


    override fun getSettingId(): SettingId = SettingId.DEPTH_CHART_LINE_STYLE


}