package com.stocksexchange.core.handlers

import android.content.ClipData
import android.content.Context
import android.net.Uri
import com.stocksexchange.core.utils.extensions.getClipboardManager

class ClipboardHandler(private val context: Context) {


    /**
     * Copies the text to the clipboard.
     *
     * @param text The text to copy
     */
    fun copyText(text: String) {
        copyToClipboard(ClipData.newPlainText(text, text))
    }


    /**
     * Copies the URI to the clipboard.
     *
     * @param uri The uri to copy
     */
    fun copyUri(uri: Uri) {
        copyToClipboard(ClipData.newUri(context.contentResolver, uri.toString(), uri))
    }


    private fun copyToClipboard(clipData: ClipData) {
        val clipboardManager = context.getClipboardManager()
        clipboardManager.setPrimaryClip(clipData)
    }


}