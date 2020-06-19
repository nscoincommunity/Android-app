package com.stocksexchange.android.ui.views.dialogs.base

import android.app.Dialog
import android.content.Context
import android.content.DialogInterface
import com.stocksexchange.android.R
import com.stocksexchange.android.utils.providers.StringProvider
import org.koin.core.KoinComponent
import org.koin.core.inject

abstract class BaseStandardDialog(
    context: Context,
    styleResourceId: Int = R.style.DefaultDialogTheme
) : Dialog(context, styleResourceId), DialogInterface.OnShowListener,
    DialogInterface.OnDismissListener, KoinComponent {


    protected val mStringProvider: StringProvider by inject()




    override fun onShow(dialog: DialogInterface?) {
        // Stub
    }


    override fun onDismiss(dialog: DialogInterface?) {
        // Stub
    }


}