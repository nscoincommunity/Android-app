package com.stocksexchange.android.ui.help

import com.stocksexchange.android.model.HelpItemModel
import com.stocksexchange.android.ui.base.mvp.model.BaseModel
import com.stocksexchange.android.ui.help.HelpModel.ActionListener

class HelpModel : BaseModel<ActionListener>() {


    fun getItems(): List<HelpItemModel> {
        return HelpItemModel.values().toList()
    }


    fun runIntercomUserRegistrationDelay(delayTimeInMillis: Long, onFinish: (() -> Unit)) {
        createUiLaunchCoroutine {
            kotlinx.coroutines.delay(delayTimeInMillis)

            onFinish()
        }
    }


    interface ActionListener : BaseActionListener


}