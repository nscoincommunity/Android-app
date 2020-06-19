package com.stocksexchange.android.ui.verification.selection

import com.stocksexchange.api.model.rest.VerificationType
import com.stocksexchange.android.ui.base.mvp.model.StubModel
import com.stocksexchange.android.ui.base.mvp.presenters.BasePresenter

class VerificationSelectionPresenter(
    view: VerificationSelectionContract.View,
    model: StubModel
) : BasePresenter<VerificationSelectionContract.View, StubModel>(view, model),
    VerificationSelectionContract.ActionListener {


    override fun onVerificationClicked(item: VerificationType) {
        mView.launchBrowser(item.url)
    }


    override fun onBackPressed(): Boolean {
        return onNavigateUpPressed()
    }


}