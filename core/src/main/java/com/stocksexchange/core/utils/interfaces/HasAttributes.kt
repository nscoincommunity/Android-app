package com.stocksexchange.core.utils.interfaces

import android.util.AttributeSet

/**
 * An interface to implement to mark a view that is supports
 * custom attributes.
 */
interface HasAttributes {


    /**
     * Should save custom view's attributes (if any).
     *
     * @param attrs The attribute set to fetch from
     * @param defStyleAttr The style of the attributes
     */
    fun saveAttributes(attrs: AttributeSet?, defStyleAttr: Int) {
        // Stub
    }


    /**
     * Applies the previously saved attributes.
     */
    fun applyAttributes() {
        // Stub
    }


}