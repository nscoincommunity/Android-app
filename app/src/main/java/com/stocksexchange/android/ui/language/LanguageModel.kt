package com.stocksexchange.android.ui.language

import com.stocksexchange.android.model.*
import com.stocksexchange.android.data.repositories.settings.SettingsRepository
import com.stocksexchange.android.ui.base.mvp.model.BaseModel

class LanguageModel(
    private val settingsRepository: SettingsRepository
) : BaseModel<LanguageModel.ActionListener>() {


    fun getSettingItems(currentLanguage: Language): MutableList<LanguageItemModel> {
        return mutableListOf<LanguageItemModel>().apply {
            for (language in Language.values()) {
                add(LanguageItemModel(
                    language = language,
                    isSelected = (currentLanguage == language)
                ))
            }
        }
    }


    fun updateSettings(settings: Settings, onFinish: (() -> Unit)) {
        createUiLaunchCoroutine {
            settingsRepository.save(settings)

            onFinish()
        }
    }


    interface ActionListener : BaseActionListener


}