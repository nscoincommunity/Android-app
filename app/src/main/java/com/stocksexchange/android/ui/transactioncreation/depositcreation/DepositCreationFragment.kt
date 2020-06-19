package com.stocksexchange.android.ui.transactioncreation.depositcreation

import android.graphics.drawable.Drawable
import android.view.View
import android.widget.ProgressBar
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.stocksexchange.android.R
import com.stocksexchange.api.model.rest.parameters.TransactionCreationParameters
import com.stocksexchange.android.model.TransactionData
import com.stocksexchange.android.theming.ThemingUtil
import com.stocksexchange.android.ui.transactioncreation.base.BaseTransactionCreationFragment
import com.stocksexchange.android.ui.views.InfoView
import com.stocksexchange.android.ui.views.toolbars.Toolbar
import com.stocksexchange.android.utils.providers.InfoViewIconProvider
import com.stocksexchange.core.formatters.NumberFormatter
import com.stocksexchange.core.handlers.QrCodeHandler
import com.stocksexchange.core.utils.extensions.*
import com.stocksexchange.core.utils.extensions.enable
import kotlinx.android.synthetic.main.deposit_creation_fragment_layout.view.*
import org.koin.android.ext.android.get
import org.koin.android.ext.android.inject
import org.koin.core.parameter.parametersOf
import kotlin.math.roundToInt

class DepositCreationFragment : BaseTransactionCreationFragment<DepositCreationPresenter>(),
    DepositCreationContract.View {


    companion object {}


    override val mPresenter: DepositCreationPresenter by inject { parametersOf(this) }




    override fun init() {
        super.init()

        initCurrencySymbol()
        initDepositCreationView()
        initWarning()
        initButtonsContainer()
    }


    private fun initCurrencySymbol() = with(mRootView.currencySymbolTv) {
        text = mPresenter.transactionCreationParams.name

        ThemingUtil.DepositCreation.currencySymbol(this, getAppTheme())
    }


    private fun initDepositCreationView() = with(mRootView.depositCreationView) {
        onAddressParamValueClickListener = {
            mPresenter.onAddressParameterValueClicked(it)
        }
        onExtraParamValueClickListener = {
            mPresenter.onExtraParameterValueClicked(it)
        }

        ThemingUtil.DepositCreation.depositCreationView(this, getAppTheme())
    }


    private fun initWarning() = with(mRootView.warningTv) {
        text = mStringProvider.getString(
            R.string.deposit_creation_fragment_warning_template,
            mPresenter.transactionData.wallet.currencySymbol
        )

        ThemingUtil.DepositCreation.warning(this, getAppTheme())
    }


    private fun initButtonsContainer() {
        initCopyButton()
        initCreateAddressButton()
    }


    private fun initCopyButton() = with(mRootView.copyBtn) {
        text = getStr(R.string.action_copy)

        setOnClickListener {
            mPresenter.onCopyButtonClicked()
        }

        ThemingUtil.DepositCreation.primaryButton(this, getAppTheme())
    }


    private fun initCreateAddressButton() = with(mRootView.createAddressBtn) {
        text = getStr(R.string.action_create_address)

        updateCreateAddressButtonState(
            params = mPresenter.transactionCreationParams,
            data = mPresenter.transactionData
        )

        setOnClickListener {
            mPresenter.onCreateAddressButtonClicked()
        }

        ThemingUtil.DepositCreation.secondaryButton(this, getAppTheme())
    }


    override fun showToolbarProgressBar() = mRootView.toolbar.showProgressBar()


    override fun hideToolbarProgressBar() = mRootView.toolbar.hideProgressBar()


    override fun addData(data: TransactionData) {
        val wallet = data.wallet
        val currency = data.currency
        val addressData = wallet.getDepositAddressData(mPresenter.transactionCreationParams.protocolId)!!
        val addressText = addressData.addressParameterValue
        val numberFormatter = get<NumberFormatter>()
        val qrCodeHandler = get<QrCodeHandler>()
        val density = resources.displayMetrics.density
        val qrCodeImageSize = (dimenInPx(R.dimen.deposit_creation_view_address_param_qr_code_image_size) / density).roundToInt()

        with(mRootView.depositCreationView) {
            setAddressParamQrCodeImage(qrCodeHandler.generateQrCodeImage(addressText, qrCodeImageSize))
            setAddressParamValue(addressText)

            if(addressData.hasAdditionalParameter) {
                setExtraParamTitle(addressData.strippedAdditionalParameterName)
                setExtraParamValue(addressData.additionalParameterValue)

                showExtraParamViews()
            } else {
                hideExtraParamViews()
            }

            setDepositFeeValue("${numberFormatter.formatTransactionFee(currency.depositFee)} ${currency.depositFeeCurrencySymbol}")
            setMinimumAmountValue("${numberFormatter.formatAmount(currency.minimumDepositAmount)} ${currency.symbol}")
        }
    }


    private fun showWarning() {
        mRootView.warningTv.makeVisible()
    }


    private fun hideWarning() {
        mRootView.warningTv.makeGone()
    }


    override fun showMainView() {
        getMainView().makeVisible()

        super.showMainView()
    }


    override fun hideMainView() {
        super.hideMainView()

        getMainView().makeGone()
    }


    override fun showEmptyView(caption: String) {
        super.showEmptyView(caption)

        showWarning()
    }


    override fun showErrorView(caption: String) {
        super.showErrorView(caption)

        showWarning()
    }


    override fun hideInfoView() {
        getInfoView().makeInvisible()

        hideWarning()
    }


    override fun updateCreateAddressButtonState(
        params: TransactionCreationParameters,
        data: TransactionData
    ) = with(mRootView.createAddressBtn) {
        val protocolId = params.protocolId
        val wallet = data.wallet
        val depositAddressData = wallet.getDepositAddressData(protocolId)

        if(!data.isEmpty && (depositAddressData?.supportsNewAddressCreation != true)) {
            disable(true)
        } else {
            enable(true)
        }
    }


    override fun getToolbarTitle(): String = getStr(R.string.deposit)


    override fun getInfoViewIcon(infoViewIconProvider: InfoViewIconProvider): Drawable? {
        return infoViewIconProvider.getTransactionCreation()
    }


    override fun getToolbar(): Toolbar = mRootView.toolbar


    override fun getContentContainer(): View = mRootView.contentContainer


    override fun getMainView(): View = mRootView.depositCreationView


    override fun getProgressBar(): ProgressBar = mRootView.progressBar


    override fun getRefreshProgressBar(): SwipeRefreshLayout = mRootView.swipeRefreshLayout


    override fun getInfoView(): InfoView = mRootView.infoView


    override fun getContentLayoutResourceId(): Int = R.layout.deposit_creation_fragment_layout


}