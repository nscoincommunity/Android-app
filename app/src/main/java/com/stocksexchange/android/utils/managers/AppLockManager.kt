package com.stocksexchange.android.utils.managers

import android.app.Activity
import android.app.AlarmManager
import android.app.Application
import android.app.PendingIntent
import com.stocksexchange.android.Constants
import com.stocksexchange.android.model.PinCodeMode
import com.stocksexchange.android.model.TransitionAnimations
import com.stocksexchange.android.receivers.AppLockReceiver
import com.stocksexchange.android.ui.auth.AuthenticationActivity
import com.stocksexchange.android.ui.auth.newInstance
import com.stocksexchange.android.ui.login.LoginActivity
import com.stocksexchange.android.ui.pinrecovery.PinRecoveryActivity
import com.stocksexchange.android.ui.splash.SplashActivity
import com.stocksexchange.core.handlers.PreferenceHandler
import com.stocksexchange.core.utils.EncryptionUtil
import com.stocksexchange.core.utils.extensions.getAlarmManager
import com.stocksexchange.core.utils.extensions.setAlarm
import com.stocksexchange.core.utils.listeners.adapters.ActivityLifecycleCallbacksAdapter

/**
 * A manager class responsible for locking the application (by launching the authentication activity)
 * if the authentication session expires or if the user has not interacted with the app for a certain
 * period of time.
 */
class AppLockManager constructor(
    encryptionUtil: EncryptionUtil,
    private val application: Application,
    private val preferenceHandler: PreferenceHandler,
    private val sessionManager: SessionManager
) : ActivityLifecycleCallbacksAdapter {


    private var mLastAuthenticationTimestamp: Long
    private var mLastInteractionTimestamp: Long

    private var mAlarmManager: AlarmManager




    init {
        application.registerActivityLifecycleCallbacks(this)

        mLastAuthenticationTimestamp = encryptionUtil.decrypt(preferenceHandler.getLastAuthTimestamp())
            .takeIf { it.isNotBlank() }
            ?.toLong()
            ?: 0L
        mLastInteractionTimestamp = 0L

        mAlarmManager = application.getAlarmManager()
    }


    override fun onActivityResumed(activity: Activity) {
        if(!isActivityAuthenticable(activity)) {
            return
        }

        if(isUserPresent()) {
            registerAppLockReceiver()
        }

        if(shouldAuthenticate()) {
            authenticate(activity)
        }
    }


    override fun onActivityPaused(activity: Activity) {
        if(isUserPresent()) {
            unregisterAppLockReceiver()
        }
    }


    private fun registerAppLockReceiver() {
        mAlarmManager.setAlarm(
            (System.currentTimeMillis() + sessionManager.getSettings().authenticationSessionDuration.timeInMillis),
            createAppLockReceiverPendingIntent()
        )
    }


    private fun unregisterAppLockReceiver() {
        mAlarmManager.cancel(createAppLockReceiverPendingIntent())
    }


    private fun createAppLockReceiverPendingIntent(): PendingIntent {
        return AppLockReceiver.init(application, Constants.REQUEST_CODE_APP_LOCK_RECEIVER)
    }


    fun shouldAuthenticate(): Boolean {
        if(!isUserPresent()) {
            return false
        }

        if(!hasLastAuthenticationTimestamp()) {
            return true
        }

        val currentTime = System.currentTimeMillis()
        val authenticationSessionDuration = sessionManager.getSettings().authenticationSessionDuration.timeInMillis
        val isAuthenticationSessionExpired = ((currentTime - mLastAuthenticationTimestamp) > authenticationSessionDuration)
        val isLastInteractionSessionExpired = ((currentTime - mLastInteractionTimestamp) > authenticationSessionDuration)

        return (isAuthenticationSessionExpired && isLastInteractionSessionExpired)
    }


    /**
     * Forces the user to authenticate by redirecting him to
     * the authentication screen.
     *
     * @param activity The activity currently running
     */
    fun authenticate(activity: Activity) {
        activity.startActivity(AuthenticationActivity.newInstance(
            activity,
            PinCodeMode.ENTER,
            TransitionAnimations.OVERSHOOT_SCALING_ANIMATIONS,
            sessionManager.getSettings().theme,
            activity.intent
        ))
        activity.finish()
    }


    fun isUserPresent(): Boolean {
        return preferenceHandler.hasEmail()
    }


    fun updateLastAuthenticationTimestamp(lastAuthenticationTimestamp: Long) {
        mLastAuthenticationTimestamp = lastAuthenticationTimestamp
    }


    /**
     * Updates the last interaction timestamp.
     *
     * @param activity The activity which received the last interaction timestamp
     * @param lastInteractionTimestamp The last user interaction timestamp
     */
    fun updateLastInteractionTimestamp(activity: Activity, lastInteractionTimestamp: Long) {
        if(!isUserPresent()) {
            return
        }

        mLastInteractionTimestamp = lastInteractionTimestamp

        if(isActivityAuthenticable(activity)) {
            registerAppLockReceiver()
        }
    }


    fun reset() {
        mLastAuthenticationTimestamp = 0L
        mLastInteractionTimestamp = 0L

        unregisterAppLockReceiver()
    }


    private fun isActivityAuthenticable(activity: Activity): Boolean {
        if((activity is SplashActivity) || (activity is LoginActivity)) {
            return false
        }

        if((activity is AuthenticationActivity) &&
            (activity.getPinCodeMode() in listOf(PinCodeMode.ENTER, PinCodeMode.CREATION))) {
            return false
        }

        if(activity is PinRecoveryActivity) {
            return false
        }

        return true
    }


    private fun hasLastAuthenticationTimestamp(): Boolean {
        return (mLastAuthenticationTimestamp != 0L)
    }


}