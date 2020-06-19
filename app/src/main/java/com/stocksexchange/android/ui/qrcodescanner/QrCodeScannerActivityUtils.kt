package com.stocksexchange.android.ui.qrcodescanner

import android.content.Context
import android.content.Intent
import com.stocksexchange.core.utils.extensions.intentFor


fun QrCodeScannerActivity.Companion.newInstance(context: Context): Intent {
    return context.intentFor<QrCodeScannerActivity>()
}