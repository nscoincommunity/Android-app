package com.stocksexchange.android.ui.views.dialogs.base

import android.content.Context
import android.view.View
import com.afollestad.materialdialogs.MaterialDialog
import com.stocksexchange.android.utils.providers.StringProvider
import org.koin.core.KoinComponent
import org.koin.core.inject

/**
 * A base dialog that acts as a wrapper around an instance of the
 * [MaterialDialog].
 */
@Suppress("LeakingThis")
abstract class BaseMaterialDialog(context: Context) : KoinComponent {


    protected val mStringProvider: StringProvider by inject()

    private lateinit var mMaterialDialog: MaterialDialog




    init {
        init(context)
    }


    protected open fun init(context: Context) {
        initMaterialDialog(context)
        initViews()
    }


    private fun initMaterialDialog(context: Context) {
        mMaterialDialog = MaterialDialog.Builder(context)
            .customView(getLayoutResourceId(), false)
            .build()
    }


    abstract fun initViews()


    protected fun<T : View> findViewById(id: Int): T {
        return mMaterialDialog.customView!!.findViewById(id)
    }


    open fun show() {
        mMaterialDialog.show()
    }


    open fun dismiss() {
        mMaterialDialog.dismiss()
    }


    abstract fun getLayoutResourceId(): Int


}