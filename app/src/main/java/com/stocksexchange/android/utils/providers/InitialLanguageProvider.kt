package com.stocksexchange.android.utils.providers

import android.content.Context
import com.stocksexchange.android.BuildConfig
import com.stocksexchange.android.model.Language
import com.stocksexchange.core.utils.extensions.getLocales

class InitialLanguageProvider(private val context: Context) {


    /**
     * Retrieves an initial app language based on a device's
     * locale list.
     *
     * @return The initial language of the app
     */
    fun getInitialLanguage(): Language {
        if(BuildConfig.IS_CHINESE_FLAVOR) {
            return Language.ENGLISH
        }

        val deviceLocales = context.getLocales()

        for(deviceLocale in deviceLocales) {
            for(language in Language.values()) {
                if(language.locale.language == deviceLocale.language) {
                    return language
                }
            }
        }

        return Language.ENGLISH
    }


}