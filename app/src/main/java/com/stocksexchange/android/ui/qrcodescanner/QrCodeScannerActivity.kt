package com.stocksexchange.android.ui.qrcodescanner

import android.content.Intent
import com.google.zxing.BarcodeFormat
import com.stocksexchange.android.R
import com.stocksexchange.android.model.TransitionAnimations
import com.stocksexchange.android.theming.ThemingUtil
import com.stocksexchange.android.ui.base.activities.BaseActivity
import com.stocksexchange.core.utils.extensions.*
import kotlinx.android.synthetic.main.qr_code_scanner_activity_layout.*
import org.koin.android.ext.android.inject
import org.koin.core.parameter.parametersOf

class QrCodeScannerActivity : BaseActivity<QrCodeScannerPresenter>(), QrCodeScannerContract.View {


    companion object {

        const val EXTRA_QR_CODE = "qr_code"

    }


    override val mPresenter: QrCodeScannerPresenter by inject { parametersOf(this) }

    private var mHintMarginBottom = 0




    override fun preInit() {
        super.preInit()

        mHintMarginBottom = dimenInPx(R.dimen.qr_code_scanner_activity_hint_margin_bottom)
    }

    override fun init() {
        super.init()

        initBackButton()
        initScannerView()
        initHint()
    }


    private fun initBackButton() = with(backBtnIv) {
        setOnClickListener {
            mPresenter.onBackButtonPressed()
        }

        doOnApplyWindowInsets { view, windowInsets, _->
            view.setTopMargin(windowInsets.systemWindowInsetTop)
        }

        ThemingUtil.QrCodeScanner.backButton(this, getAppTheme())
    }


    private fun initScannerView() = with(scannerView) {
        setFormats(listOf(BarcodeFormat.QR_CODE))

        ThemingUtil.QrCodeScanner.scannerView(this, getAppTheme())
    }


    private fun initHint() = with(hintTv) {
        text = getStr(R.string.qr_code_scanner_activity_hint_text)

        doOnApplyWindowInsets { view, windowInsets, _ ->
            view.setBottomMargin(mHintMarginBottom + windowInsets.systemWindowInsetBottom)
        }

        ThemingUtil.QrCodeScanner.hint(this, getAppTheme())
    }


    override fun startScanner() = with(scannerView) {
        setResultHandler {
            mPresenter.onQrCodeScanned(it.text)
        }

        startCamera()
    }


    override fun stopScanner() = with(scannerView) {
        stopCamera()
    }


    override fun setResult(qrCode: String) {
        val data = Intent().apply {
            putExtra(EXTRA_QR_CODE, qrCode)
        }

        setResult(RESULT_OK, data)
    }


    override fun shouldShowIntercomInAppMessagePopups(): Boolean = false


    override fun getContentLayoutResourceId(): Int = R.layout.qr_code_scanner_activity_layout


    override fun getTransitionAnimations(): TransitionAnimations {
        return TransitionAnimations.FADING_ANIMATIONS
    }


}