package com.stocksexchange.android.receivers

import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.widget.Toast
import com.stocksexchange.android.Constants
import com.stocksexchange.android.R
import com.stocksexchange.android.receivers.base.BaseBroadcastReceiver
import com.stocksexchange.android.utils.handlers.SharingHandler
import com.stocksexchange.android.utils.providers.StringProvider
import com.stocksexchange.core.handlers.ClipboardHandler
import com.stocksexchange.core.utils.extensions.intentFor
import org.koin.core.get
import org.koin.core.inject

/**
 * A receiver used for receiving and handling actions
 * of the custom tabs browser.
 */
class CustomTabsReceiver : BaseBroadcastReceiver() {


    companion object {

        private const val EXTRA_REQUEST_CODE = "request_code"


        fun init(context: Context, uri: Uri, requestCode: Int): PendingIntent {
            val intent = context.intentFor<CustomTabsReceiver>().apply {
                putExtra(EXTRA_REQUEST_CODE, requestCode)
                data = uri
            }

            return PendingIntent.getBroadcast(
                context,
                requestCode,
                intent,
                PendingIntent.FLAG_CANCEL_CURRENT
            )
        }

    }


    private val mStringProvider: StringProvider by inject()




    override fun onReceive(context: Context, intent: Intent) {
        if(intent.data == null) {
            return
        }

        val uri = intent.data!!

        when(intent.getIntExtra(EXTRA_REQUEST_CODE, Constants.REQUEST_CODE_SHARE_VIA)) {
            Constants.REQUEST_CODE_SHARE_VIA -> onShareViaTabClicked(context, uri.toString())
            Constants.REQUEST_CODE_COPY_LINK -> onCopyLinkTabClicked(context, uri)
        }
    }


    private fun onShareViaTabClicked(context: Context, url: String) {
        get<SharingHandler>().shareText(
            context = context,
            text = url,
            chooserTitle = mStringProvider.getString(R.string.action_share_via)
        )
    }


    private fun onCopyLinkTabClicked(context: Context, uri: Uri) {
        get<ClipboardHandler>().copyUri(uri)

        Toast.makeText(
            context,
            mStringProvider.getString(R.string.copied_to_clipboard),
            Toast.LENGTH_LONG
        ).show()
    }


}