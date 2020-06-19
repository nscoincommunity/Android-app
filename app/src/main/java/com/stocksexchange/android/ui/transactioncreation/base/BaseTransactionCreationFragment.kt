package com.stocksexchange.android.ui.transactioncreation.base

import android.os.Bundle
import android.view.View
import com.stocksexchange.android.model.TransactionData
import com.stocksexchange.android.theming.ThemingUtil
import com.stocksexchange.android.ui.base.dataloading.BaseDataLoadingFragment
import com.stocksexchange.android.ui.transactioncreation.extrasExtractor
import com.stocksexchange.android.ui.views.toolbars.Toolbar
import com.stocksexchange.core.utils.extensions.extract

/**
 * A base fragment that contains functionality for creating
 * transactions (deposits and withdrawals).
 */
abstract class BaseTransactionCreationFragment<P> : BaseDataLoadingFragment<P, TransactionData>(),
    TransactionCreationView where
        P : BaseTransactionCreationPresenter<*, *> {


    companion object {}




    override fun onFetchExtras(extras: Bundle) {
        extras.extract(extrasExtractor).also {
            mPresenter.transactionCreationParams = it.transactionCreationParams
            mPresenter.transactionData = it.transactionData
        }
    }


    override fun init() {
        super.init()

        initToolbar()
        initContentContainer()
    }


    private fun initToolbar() = with(getToolbar()){
        setTitleText(getToolbarTitle())

        setOnLeftButtonClickListener {
            mPresenter.onNavigateUpPressed()
        }

        ThemingUtil.TransactionCreation.toolbar(this, getAppTheme())
    }


    private fun initContentContainer() = with(getContentContainer()) {
        ThemingUtil.TransactionCreation.contentContainer(this, getAppTheme())
    }


    override fun postInit() {
        super.postInit()

        if(!isDataSourceEmpty()) {
            addData(mPresenter.transactionData)
        }
    }


    override fun isDataSourceEmpty(): Boolean = mPresenter.transactionData.isEmpty


    abstract fun getToolbarTitle(): String


    abstract fun getToolbar(): Toolbar


    abstract fun getContentContainer(): View


}