package com.stocksexchange.android.ui.transactioncreation.withdrawalcreation

import android.Manifest
import android.app.Activity
import android.content.Intent
import android.graphics.drawable.Drawable
import android.text.InputType
import android.view.View
import android.widget.ProgressBar
import androidx.navigation.NavOptions
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.stocksexchange.android.Constants
import com.stocksexchange.android.R
import com.stocksexchange.android.model.BalanceTab
import com.stocksexchange.android.model.InputViewState
import com.stocksexchange.android.model.TransactionData
import com.stocksexchange.android.model.WithdrawalInputView
import com.stocksexchange.android.theming.ThemingUtil
import com.stocksexchange.android.ui.balance.BalanceContainerFragment
import com.stocksexchange.android.ui.balance.newArgs
import com.stocksexchange.android.ui.transactioncreation.base.BaseTransactionCreationFragment
import com.stocksexchange.android.ui.qrcodescanner.QrCodeScannerActivity
import com.stocksexchange.android.ui.qrcodescanner.newInstance
import com.stocksexchange.android.ui.views.InfoView
import com.stocksexchange.android.ui.views.toolbars.Toolbar
import com.stocksexchange.android.utils.extensions.*
import com.stocksexchange.android.utils.providers.InfoViewIconProvider
import com.stocksexchange.core.formatters.NumberFormatter
import com.stocksexchange.core.utils.extensions.*
import com.stocksexchange.core.utils.helpers.checkPermissions
import com.stocksexchange.core.utils.helpers.isPermissionSetGranted
import com.stocksexchange.core.utils.listeners.QueryListener
import kotlinx.android.synthetic.main.withdrawal_creation_fragment_layout.view.*
import org.koin.android.ext.android.inject
import org.koin.core.parameter.parametersOf

class WithdrawalCreationFragment : BaseTransactionCreationFragment<WithdrawalCreationPresenter>(),
    WithdrawalCreationContract.View {


    companion object {}


    override val mPresenter: WithdrawalCreationPresenter by inject { parametersOf(this) }

    private val numberFormatter: NumberFormatter by inject()




    override fun init() {
        super.init()

        initAvailableBalance()
        initMainView()
        initWithdrawButton()
    }


    private fun initAvailableBalance() {
        initAvailableBalanceTitle()
        initAvailableBalanceValue()
    }


    private fun initAvailableBalanceTitle() = with(mRootView.availableBalanceTitleTv) {
        text = getStr(R.string.available_balance)

        ThemingUtil.WithdrawalCreation.availableBalanceTitle(this, getAppTheme())
    }


    private fun initAvailableBalanceValue() = with(mRootView.availableBalanceValueTv) {
        val params = mPresenter.transactionCreationParams
        val formattedBalance = numberFormatter.formatBalance(params.wallet.currentBalance)
        val textStr = "$formattedBalance ${params.name}"

        text = textStr

        ThemingUtil.WithdrawalCreation.availableBalanceValue(this, getAppTheme())
    }


    private fun initMainView() = with(mRootView.withdrawalCreationView) {
        setInputViewExtraViewContainerWidth(dimenInPx(R.dimen.withdrawal_creation_view_extra_view_container_width))
        setInputViewType(WithdrawalInputView.ADDRESS, InputType.TYPE_TEXT_FLAG_NO_SUGGESTIONS)
        setInputViewType(WithdrawalInputView.AMOUNT, ctx.getKeyboardNumericInputType())

        ctx.getKeyboardNumericKeyListener()?.also {
            setInputViewKeyListener(WithdrawalInputView.AMOUNT, it)
        }

        setOnInputViewIconClickListener(WithdrawalInputView.ADDRESS) {
            mPresenter.onAddressInputViewIconClicked()
        }

        setOnInputViewLabelClickListener(WithdrawalInputView.AMOUNT) {
            mPresenter.onAmountInputViewLabelClicked()
        }

        addInputViewTextWatcher(
            WithdrawalInputView.AMOUNT,
            QueryListener(object : QueryListener.Callback {

                override fun onQueryEntered(query: String) {
                    mPresenter.onAmountInputViewTextEntered(query)
                }

                override fun onQueryRemoved() {
                    mPresenter.onAmountInputViewTextRemoved()
                }

            })
        )

        ThemingUtil.WithdrawalCreation.withdrawalCreationView(this, getAppTheme())
    }


    private fun initWithdrawButton() = with(mRootView.withdrawBtn) {
        text = getStr(R.string.action_withdraw)

        setOnClickListener {
            mPresenter.onWithdrawButtonClicked()
        }

        ThemingUtil.WithdrawalCreation.withdrawButton(this, getAppTheme())
    }


    override fun showToolbarProgressBar() = mRootView.toolbar.showProgressBar()


    override fun hideToolbarProgressBar() = mRootView.toolbar.hideProgressBar()


    override fun addData(data: TransactionData) = with(mRootView.withdrawalCreationView) {
        val wallet = data.wallet
        val currency = data.currency
        val protocolId = mPresenter.transactionCreationParams.protocolId
        val withdrawalFee = data.getWithdrawalFee(protocolId)
        val withdrawalFeeCurrencySymbol = data.getWithdrawalCurrencySymbol(protocolId)

        if(wallet.hasAdditionalWithdrawalParameter) {
            setExtraLabelText(wallet.strippedAdditionalWithdrawalParameterName)
            setInputViewHintText(
                WithdrawalInputView.EXTRA,
                getStr(if(wallet.isAdditionalWithdrawalParameterOptional) {
                    R.string.optional
                } else {
                    R.string.required
                })
            )

            showExtraInputView()
        } else {
            hideExtraInputView()
        }

        setMinAmountValueText("${numberFormatter.formatAmount(currency.minimumWithdrawalAmount)} ${currency.symbol}")
        setFeeValueText("${numberFormatter.formatAmount(withdrawalFee)} $withdrawalFeeCurrencySymbol")
        setFinalAmountValueText("${numberFormatter.formatAmount(0.0)} ${currency.symbol}")
    }


    override fun showMainView() {
        super.showMainView()

        // Enabling the editing of the input views
        mRootView.withdrawalCreationView.enableInputViewsEditing()
    }


    override fun hideMainView() {
        super.hideMainView()

        // Disabling the editing of the input views
        mRootView.withdrawalCreationView.disableInputViewsEditing()
    }


    override fun updateAvailableBalance(availableBalance: String) {
        mRootView.availableBalanceValueTv.crossFadeText(availableBalance)
    }


    override fun launchQrCodeScannerActivity() {
        startActivityForResult(
            QrCodeScannerActivity.newInstance(ctx),
            Constants.REQUEST_CODE_QR_CODE
        )
    }


    override fun navigateToBalanceContainerScreen() {
        navigate(
            destinationId = R.id.balanceDest,
            arguments = BalanceContainerFragment.newArgs(
                selectedTab = BalanceTab.WITHDRAWALS
            )
        )
    }


    override fun setAddressInputViewText(text: String) {
        mRootView.withdrawalCreationView.setInputViewText(WithdrawalInputView.ADDRESS, text)
    }


    override fun setAmountInputViewText(text: String) {
        mRootView.withdrawalCreationView.setInputViewText(WithdrawalInputView.AMOUNT, text)
    }


    override fun setFinalAmountText(text: String) {
        mRootView.withdrawalCreationView.setFinalAmountValueText(text)
    }


    override fun setInputViewState(state: InputViewState,
                                   inputViews: List<WithdrawalInputView>) {
        for(inputView in inputViews) {
            mRootView.withdrawalCreationView.setInputViewState(inputView, state)
        }
    }


    override fun checkCameraPermission(): Boolean {
        return checkPermissions(
            Constants.REQUEST_CODE_CAMERA_PERMISSION,
            arrayOf(Manifest.permission.CAMERA)
        )
    }


    override fun getToolbarTitle(): String = getStr(R.string.withdrawal)


    override fun getInputViewValue(inputView: WithdrawalInputView): String {
        return mRootView.withdrawalCreationView.getInputViewText(inputView)
    }


    override fun getInfoViewIcon(infoViewIconProvider: InfoViewIconProvider): Drawable? {
        return infoViewIconProvider.getTransactionCreation()
    }


    override fun getToolbar(): Toolbar = mRootView.toolbar


    override fun getContentContainer(): View = mRootView.contentContainer


    override fun getMainView(): View = mRootView.withdrawalCreationView


    override fun getProgressBar(): ProgressBar = mRootView.mainProgressBar


    override fun getRefreshProgressBar(): SwipeRefreshLayout = mRootView.swipeRefreshLayout


    override fun getInfoView(): InfoView = mRootView.infoView


    override fun getContentLayoutResourceId(): Int = R.layout.withdrawal_creation_fragment_layout


    override fun getNavOptionsForDestination(id: Int): NavOptions {
        return mNavOptionsCreator.getWithdrawalCreationNavOptions(id)
    }


    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>,
                                            grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)

        if(isPermissionSetGranted(grantResults)) {
            if(requestCode == Constants.REQUEST_CODE_CAMERA_PERMISSION) {
                mPresenter.onAddressInputViewIconClicked()
            }
        } else {
            showToast(getStr(R.string.error_permissions_not_granted))
        }
    }


    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if(resultCode != Activity.RESULT_OK) {
            return
        }

        when(requestCode) {
            Constants.REQUEST_CODE_QR_CODE -> {
                val qrCode = (data?.getStringExtra(QrCodeScannerActivity.EXTRA_QR_CODE) ?: "")

                mPresenter.onQrCodeReceived(qrCode)
            }
        }
    }


}