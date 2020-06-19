package com.stocksexchange.api.model.rest.parameters

import android.os.Parcelable
import com.stocksexchange.api.model.rest.Protocol
import com.stocksexchange.api.model.rest.Wallet
import kotlinx.android.parcel.Parcelize

@Parcelize
data class TransactionCreationParameters(
    val wallet: Wallet,
    val mode: Mode,
    val protocol: Protocol
) : Parcelable {


    companion object {

        fun getDefaultParameters(
            wallet: Wallet = Wallet.STUB_WALLET,
            protocol: Protocol = Protocol.STUB_PROTOCOL
        ): TransactionCreationParameters {
            return TransactionCreationParameters(
                wallet = wallet,
                protocol = protocol,
                mode = Mode.WALLET_RETRIEVAL
            )
        }

    }




    val hasWalletId: Boolean
        get() = wallet.hasId


    val hasProtocol: Boolean
        get() = !protocol.isStub


    val currencyId: Int
        get() = wallet.currencyId


    val walletId: Long
        get() = wallet.id


    val protocolId: Int
        get() = protocol.id


    val name: String
        get() = (if(hasProtocol) protocol.name else wallet.currencySymbol)


    enum class Mode {

        WALLET_RETRIEVAL,
        WALLET_CREATION,
        ADDRESS_CREATION

    }


}