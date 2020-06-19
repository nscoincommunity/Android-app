package com.stocksexchange.android.ui.qrcodescanner

import com.stocksexchange.android.ui.base.mvp.model.StubModel
import com.stocksexchange.android.ui.base.mvp.presenters.BasePresenter

class QrCodeScannerPresenter(
    view: QrCodeScannerContract.View,
    model: StubModel
) : BasePresenter<QrCodeScannerContract.View, StubModel>(view, model),
    QrCodeScannerContract.ActionListener {


    override fun start() {
        super.start()

        mView.startScanner()
    }


    override fun stop() {
        super.stop()

        mView.stopScanner()
    }


    override fun onBackButtonPressed() {
        mView.finishActivity()
    }


    override fun onQrCodeScanned(qrCode: String) {
        mView.setResult(qrCode)
        mView.finishActivity()
    }


}