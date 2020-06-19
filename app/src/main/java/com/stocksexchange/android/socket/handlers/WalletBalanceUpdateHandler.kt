package com.stocksexchange.android.socket.handlers

import com.google.gson.Gson
import com.stocksexchange.android.socket.model.data.WalletSocketData
import com.stocksexchange.core.utils.extensions.fromJson
import com.stocksexchange.android.data.repositories.wallets.WalletsRepository
import com.stocksexchange.android.events.WalletEvent
import com.stocksexchange.android.socket.handlers.base.BaseHandler
import com.stocksexchange.android.socket.model.enums.SocketEvent
import org.greenrobot.eventbus.EventBus
import org.json.JSONObject

class WalletBalanceUpdateHandler(
    gson: Gson,
    private val walletsRepository: WalletsRepository
) : BaseHandler<WalletSocketData>(gson) {


    override suspend fun onSocketDataReceivedAsync(channel: String,
                                                   socketData: WalletSocketData) {
        val walletResult = walletsRepository.get(socketData.id)

        if(walletResult.isErroneous()) {
            return
        }

        val originalWallet = walletResult.getSuccessfulResultValue()
        val updatedWallet = originalWallet.copy(
            currentBalance = socketData.currentBalance,
            frozenBalance = socketData.frozenBalance,
            bonusBalance = socketData.bonusBalance,
            totalBalance = socketData.totalBalance
        )

        walletsRepository.save(updatedWallet)

        if(EventBus.getDefault().hasSubscriberForEvent(WalletEvent::class.java)) {
            EventBus.getDefault().post(WalletEvent.updateBalance(
                wallet = updatedWallet,
                source = this
            ))
        }
    }


    override fun convertSocketData(socketDataJsonObj: JSONObject): WalletSocketData {
        return gson.fromJson(socketDataJsonObj.toString())
    }


    override fun getLoggingKey(): String = "onWalletBalanceUpdated"


    override fun getSocketEvent(): SocketEvent = SocketEvent.WALLET_BALANCE_UPDATED


}