package com.stocksexchange.android.ui.qrcodescanner

import com.stocksexchange.android.ui.base.mvp.views.BaseView

interface QrCodeScannerContract {


    interface View : BaseView {

        fun startScanner()

        fun stopScanner()

        fun setResult(qrCode: String)

        fun finishActivity()

    }


    interface ActionListener {

        fun onBackButtonPressed()

        fun onQrCodeScanned(qrCode: String)

    }


}