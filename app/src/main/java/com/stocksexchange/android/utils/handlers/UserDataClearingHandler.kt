package com.stocksexchange.android.utils.handlers

import android.content.Context
import com.stocksexchange.android.data.repositories.alertprice.AlertPriceRepository
import com.stocksexchange.android.data.repositories.deposits.DepositsRepository
import com.stocksexchange.android.data.repositories.inbox.InboxRepository
import com.stocksexchange.android.data.repositories.orders.OrdersRepository
import com.stocksexchange.android.data.repositories.profileinfos.ProfileInfosRepository
import com.stocksexchange.android.data.repositories.wallets.WalletsRepository
import com.stocksexchange.android.data.repositories.withdrawals.WithdrawalsRepository
import com.stocksexchange.android.receivers.UserLogoutReceiver
import com.stocksexchange.android.utils.managers.AppLockManager
import com.stocksexchange.android.utils.managers.SessionManager
import com.stocksexchange.api.utils.CredentialsHandler
import com.stocksexchange.core.handlers.PreferenceHandler

class UserDataClearingHandler(
    private val context: Context,
    private val profileInfosRepository: ProfileInfosRepository,
    private val walletsRepository: WalletsRepository,
    private val ordersRepository: OrdersRepository,
    private val depositsRepository: DepositsRepository,
    private val withdrawalsRepository: WithdrawalsRepository,
    private val credentialsHandler: CredentialsHandler,
    private val intercomHandler: IntercomHandler,
    private val sessionManager: SessionManager,
    private val inboxRepository: InboxRepository,
    private val appLockManager: AppLockManager,
    private val alertPriceRepository: AlertPriceRepository,
    private val preferenceHandler: PreferenceHandler
) {

    /**
     * Clears all user related data stored inside the database
     * as well as in preferences.
     *
     * @param onFinish The callback to invoke when the clearing
     * is done
     */
    suspend fun clearAllUserData(onFinish: suspend (() -> Unit)) {
        profileInfosRepository.clear()
        walletsRepository.clear()
        ordersRepository.clear()
        depositsRepository.clear()
        withdrawalsRepository.clear()
        credentialsHandler.clearUserData()
        intercomHandler.logout()
        sessionManager.reset()
        inboxRepository.clear()
        appLockManager.reset()
        alertPriceRepository.clear()

        preferenceHandler.removeFirebasePushToken()

        UserLogoutReceiver.cancelUserLogoutAlarm(context)

        onFinish()
    }


    /**
     * Clears only the private user data.
     *
     * @param onFinish The callback to invoke when the clearing is done
     */
    suspend fun clearPrivateUserData(onFinish: suspend (() -> Unit)) {
        walletsRepository.clear()
        ordersRepository.clear()
        depositsRepository.clear()
        withdrawalsRepository.clear()
        inboxRepository.clear()
        alertPriceRepository.clear()

        preferenceHandler.removeFirebasePushToken()

        onFinish()
    }


}