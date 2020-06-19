package com.stocksexchange.android.utils.diffcallbacks

import com.stocksexchange.api.model.rest.Wallet
import com.stocksexchange.android.utils.diffcallbacks.base.BaseDiffCallback
import com.stocksexchange.android.ui.wallets.fragment.WalletItem

class WalletsDiffCallback(
    oldList: List<WalletItem>,
    newList: List<WalletItem>
) : BaseDiffCallback<Wallet, WalletItem>(oldList, newList) {


    override fun areItemsTheSame(oldItem: Wallet, newItem: Wallet): Boolean {
        return (oldItem.id == newItem.id)
    }


}