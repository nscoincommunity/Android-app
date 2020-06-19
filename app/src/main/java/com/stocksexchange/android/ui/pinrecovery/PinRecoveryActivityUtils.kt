package com.stocksexchange.android.ui.pinrecovery

import android.content.Context
import android.content.Intent
import com.stocksexchange.core.utils.extensions.intentFor


fun PinRecoveryActivity.Companion.newInstance(context: Context): Intent {
    return context.intentFor<PinRecoveryActivity>()
}