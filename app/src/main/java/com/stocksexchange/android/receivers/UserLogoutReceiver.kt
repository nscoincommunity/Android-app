package com.stocksexchange.android.receivers

import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.PowerManager
import com.stocksexchange.android.Constants
import com.stocksexchange.android.data.repositories.settings.SettingsRepository
import com.stocksexchange.android.factories.SettingsFactory
import com.stocksexchange.android.receivers.base.BaseBroadcastReceiver
import com.stocksexchange.android.utils.handlers.UserDataClearingHandler
import com.stocksexchange.api.utils.CredentialsHandler
import com.stocksexchange.core.handlers.CoroutineHandler
import com.stocksexchange.core.utils.extensions.getAlarmManager
import com.stocksexchange.core.utils.extensions.getPowerManager
import com.stocksexchange.core.utils.extensions.setAlarm
import org.koin.core.KoinComponent
import org.koin.core.get
import org.koin.core.inject

/**
 * A receiver used for logging out a user.
 */
class UserLogoutReceiver : BaseBroadcastReceiver() {


    companion object : KoinComponent {

        private val TAG = "${Constants.PACKAGE_NAME}:userlogoutreceiver"

        private val ACTION_LOG_OUT_USER = "${Constants.PACKAGE_NAME}.ACTION_LOG_OUT_USER"


        fun recreateUserLogoutAlarm(context: Context) {
            val credentialsHandler = get<CredentialsHandler>()

            if(!credentialsHandler.hasOAuthCredentials()) {
                return
            }

            val oauthCredentials = credentialsHandler.getOAuthCredentials()
            val refreshTokenExpirationTimeInMillis = oauthCredentials.refreshTokenExpirationTimeInMillis

            recreateUserLogoutAlarm(
                context = context,
                triggerAtMillis = refreshTokenExpirationTimeInMillis
            )
        }


        fun recreateUserLogoutAlarm(context: Context, triggerAtMillis: Long) {
            val pendingIntent = createPendingIntent(context)

            context.getAlarmManager().apply {
                // Cancelling the previous intent (if needed)
                cancel(pendingIntent)

                // Setting a new one
                setAlarm(triggerAtMillis, pendingIntent)
            }
        }


        fun cancelUserLogoutAlarm(context: Context) {
            context.getAlarmManager().cancel(createPendingIntent(context))
        }


        private fun createPendingIntent(context: Context): PendingIntent {
            return PendingIntent.getBroadcast(
                context,
                Constants.REQUEST_CODE_USER_LOGOUT_RECEIVER,
                Intent(context, UserLogoutReceiver::class.java).apply {
                    action = ACTION_LOG_OUT_USER
                },
                PendingIntent.FLAG_UPDATE_CURRENT
            )
        }

    }


    private val mSettingsRepository: SettingsRepository by inject()

    private val mSettingsFactory: SettingsFactory by inject()

    private val mUserDataClearingHandler: UserDataClearingHandler by inject()

    private val mCoroutineHandler: CoroutineHandler by inject()




    override fun onReceive(context: Context, intent: Intent) {
        val powerManager = context.getPowerManager()
        val wakeLock = powerManager.newWakeLock(PowerManager.PARTIAL_WAKE_LOCK, TAG)

        wakeLock.acquire()

        when(intent.action) {
            ACTION_LOG_OUT_USER -> handleLogOutUser()
        }

        wakeLock.release()
    }


    private fun handleLogOutUser() {
        val pendingResult = goAsync()

        mCoroutineHandler.createBgLaunchCoroutine {
            mUserDataClearingHandler.clearAllUserData() {
                val settingsResult = mSettingsRepository.get()

                if(settingsResult.isSuccessful()) {
                    val oldSettings = settingsResult.getSuccessfulResultValue()
                    val newSettings = mSettingsFactory.getSettingsAfterUserLogout(oldSettings)

                    mSettingsRepository.save(newSettings)
                }

                pendingResult.finish()
            }
        }
    }


}