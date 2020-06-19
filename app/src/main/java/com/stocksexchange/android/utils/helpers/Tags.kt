package com.stocksexchange.android.utils.helpers

import com.stocksexchange.android.ui.base.mvp.presenters.BasePresenter
import com.stocksexchange.android.ui.currencymarketpreview.CurrencyMarketPreviewFragment

/**
 * Returns a tag associated with the passed in object. If the tag
 * is not found, then returns the name of the class for the object.
 *
 * @param source The object to get the tag for
 *
 * @return The string tag for the object
 */
fun tag(source: Any): String {
    return when(source) {

        is CurrencyMarketPreviewFragment -> CurrencyMarketPreviewFragment.TAG
        is BasePresenter<*, *> -> source.toString()
        is String -> source

        else -> source::class.java.simpleName

    }
}


/**
 * Returns a tag by combing the name of the passed in class
 * together with the key.
 *
 * @param clazz The enclosing class
 * @param key The key
 *
 * @return The string tag
 */
fun tag(clazz: Class<*>, key: String): String {
    return "${clazz.simpleName}_$key"
}