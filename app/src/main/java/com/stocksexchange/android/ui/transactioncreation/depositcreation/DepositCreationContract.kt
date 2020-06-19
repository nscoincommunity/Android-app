package com.stocksexchange.android.ui.transactioncreation.depositcreation

import com.stocksexchange.android.model.TransactionData
import com.stocksexchange.android.ui.transactioncreation.base.TransactionCreationView
import com.stocksexchange.api.model.rest.parameters.TransactionCreationParameters

interface DepositCreationContract {


    interface View : TransactionCreationView {

        fun updateCreateAddressButtonState(params: TransactionCreationParameters, data: TransactionData)

    }


    interface ActionListener {

        fun onAddressParameterValueClicked(value: String)

        fun onExtraParameterValueClicked(value: String)

        fun onCopyButtonClicked()

        fun onCreateAddressButtonClicked()

    }


}