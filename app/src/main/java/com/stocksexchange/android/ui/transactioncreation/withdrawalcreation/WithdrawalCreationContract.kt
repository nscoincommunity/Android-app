package com.stocksexchange.android.ui.transactioncreation.withdrawalcreation

import com.stocksexchange.android.model.InputViewState
import com.stocksexchange.android.model.WithdrawalInputView
import com.stocksexchange.android.ui.transactioncreation.base.TransactionCreationView

interface WithdrawalCreationContract {


    interface View : TransactionCreationView {

        fun updateAvailableBalance(availableBalance: String)

        fun launchQrCodeScannerActivity()

        fun navigateToBalanceContainerScreen()

        fun setAddressInputViewText(text: String)

        fun setAmountInputViewText(text: String)

        fun setFinalAmountText(text: String)

        fun setInputViewState(state: InputViewState, inputViews: List<WithdrawalInputView>)

        fun checkCameraPermission(): Boolean

        fun getInputViewValue(inputView: WithdrawalInputView): String

    }


    interface ActionListener {

        fun onAddressInputViewIconClicked()

        fun onQrCodeReceived(qrCode: String)

        fun onAmountInputViewLabelClicked()

        fun onAmountInputViewTextEntered(amount: String)

        fun onAmountInputViewTextRemoved()

        fun onWithdrawButtonClicked()

    }


}