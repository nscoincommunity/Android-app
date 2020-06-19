package com.stocksexchange.android.utils.handlers

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import com.stocksexchange.android.R
import com.stocksexchange.android.utils.providers.StringProvider
import com.stocksexchange.core.utils.extensions.canIntentBeHandled
import com.stocksexchange.core.utils.extensions.shortToast

class SharingHandler(
    private val stringProvider: StringProvider
) {


    companion object {

        const val TYPE_PREFIX_TEXT = "text/"

        const val DATA_TYPE_PLAIN_TEXT = "${TYPE_PREFIX_TEXT}plain"

    }




    /**
     * Shares a text by sending the intent.
     *
     * @param context The context to launch the intent from
     * @param text The text to share
     * @param chooserTitle The title for the chooser
     */
    fun shareText(context: Context, text: String, chooserTitle: String) {
        val intent = Intent(Intent.ACTION_SEND).apply {
            type = DATA_TYPE_PLAIN_TEXT
            putExtra(Intent.EXTRA_TEXT, text)

            if(context !is AppCompatActivity) {
                addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            }
        }

        if(context.canIntentBeHandled(intent)) {
            context.startActivity(Intent.createChooser(intent, chooserTitle))
        } else {
            context.shortToast(stringProvider.getString(
                R.string.error_no_sharing_client_template,
                stringProvider.getString(R.string.error_unable_to_proceed)
            ))
        }

    }


}