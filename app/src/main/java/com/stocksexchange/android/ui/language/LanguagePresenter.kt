package com.stocksexchange.android.ui.language

import com.stocksexchange.android.events.SettingsEvent
import com.stocksexchange.android.model.*
import com.stocksexchange.android.ui.base.mvp.presenters.BasePresenter
import com.stocksexchange.android.utils.SavedState
import com.stocksexchange.android.utils.extensions.extract
import com.stocksexchange.android.utils.managers.SessionManager
import com.stocksexchange.core.formatters.TimeFormatter
import org.greenrobot.eventbus.EventBus

class LanguagePresenter(
    view: LanguageContract.View,
    model: LanguageModel,
    private val timeFormatter: TimeFormatter,
    private val sessionManager: SessionManager
) : BasePresenter<LanguageContract.View, LanguageModel>(view, model), LanguageContract.ActionListener,
    LanguageModel.ActionListener {


    var isToolbarRightButtonVisible: Boolean = false

    private var mNewSettings: Settings = sessionManager.getSettings()




    init {
        model.setActionListener(this)
    }


    override fun start() {
        super.start()

        if(mView.isDataSetEmpty()) {
            setSettingItems()
        }
    }


    private fun setSettingItems() {
        mView.setItems(mModel.getSettingItems(mNewSettings.language))
    }


    override fun onToolbarRightButtonClicked() {
        mModel.updateSettings(mNewSettings) {
            sessionManager.setSettings(mNewSettings)

            mStringProvider.setLocale(mNewSettings.language.locale)
            timeFormatter.setLocale(mNewSettings.language.locale)

            EventBus.getDefault().post(SettingsEvent.changeLanguage(
                attachment = mNewSettings,
                source = this
            ))
        }
    }


    override fun onLanguageItemClicked(itemModel: LanguageItemModel) {
        val selectedLanguage = itemModel.language

        val oldSettings = sessionManager.getSettings()
        val newSettings = oldSettings.copy(language = selectedLanguage).also {
            mNewSettings = it
        }

        isToolbarRightButtonVisible = (newSettings != oldSettings)
        mView.setToolbarRightButtonVisible(isToolbarRightButtonVisible)

        setSettingItems()
    }


    override fun onBackPressed(): Boolean {
        return onNavigateUpPressed()
    }


    override fun onRestoreState(savedState: SavedState) {
        super.onRestoreState(savedState)

        savedState.extract(presenterStateExtractor).also {
            isToolbarRightButtonVisible = it.isToolbarRightButtonVisible
            mNewSettings = it.newSettings
        }
    }


    override fun onSaveState(savedState: SavedState) {
        super.onSaveState(savedState)

        savedState.saveState(PresenterState(
            isToolbarRightButtonVisible = isToolbarRightButtonVisible,
            newSettings = mNewSettings
        ))
    }


}