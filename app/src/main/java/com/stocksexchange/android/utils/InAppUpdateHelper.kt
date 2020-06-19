package com.stocksexchange.android.utils

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.content.IntentSender
import androidx.activity.ComponentActivity
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleObserver
import androidx.lifecycle.OnLifecycleEvent
import com.crashlytics.android.Crashlytics
import com.google.android.play.core.appupdate.AppUpdateInfo
import com.google.android.play.core.appupdate.AppUpdateManager
import com.google.android.play.core.appupdate.AppUpdateManagerFactory
import com.google.android.play.core.install.InstallState
import com.google.android.play.core.install.model.AppUpdateType
import com.google.android.play.core.install.model.InstallStatus
import com.google.android.play.core.install.model.UpdateAvailability
import com.stocksexchange.android.Constants.REQUEST_CODE_IN_APP_UPDATE
import timber.log.Timber
import kotlin.properties.Delegates

/**
 * A helper class containing functionality for in-app updates.
 */
class InAppUpdateHelper(val context: Context, val listener: Listener) : LifecycleObserver {


    private var mHasNotifiedAboutUpdate: Boolean = false

    private var mAppUpdateInfo: AppUpdateInfo? = null

    private var mAppUpdateManager: AppUpdateManager? = null
    private var mHostActivity: Activity? = null

    private var mState by Delegates.observable(State.NO_UPDATES) { _, _, newState ->
        handleStateChange(newState)
    }




    init {
        initAppUpdateManager()
    }


    private fun initAppUpdateManager() {
        mAppUpdateManager = AppUpdateManagerFactory.create(context)
    }


    @Suppress("NON_EXHAUSTIVE_WHEN")
    fun onNextStep() {
        when(mState) {
            State.PENDING_UPDATE -> startDownloadingProcess()
            State.UPDATE_DOWNLOADED -> mAppUpdateManager?.completeUpdate()
        }
    }


    @OnLifecycleEvent(Lifecycle.Event.ON_RESUME)
    private fun checkForUpdates() {
        mAppUpdateManager?.appUpdateInfo?.addOnSuccessListener {
            if((it.updateAvailability() == UpdateAvailability.UPDATE_AVAILABLE) &&
                it.isUpdateTypeAllowed(AppUpdateType.FLEXIBLE)) {
                onUpdateAvailable(it)
            }

            if(it.installStatus() == InstallStatus.DOWNLOADED) {
                onDownloadingFinished()
            }
        }
    }


    private fun handleStateChange(newState: State) {
        listener.onStateChanged(newState)
    }


    private fun startDownloadingProcess() {
        if(mAppUpdateInfo == null) {
            return
        }

        registerUpdateListener()

        try {
            mAppUpdateManager?.startUpdateFlowForResult(
                mAppUpdateInfo,
                AppUpdateType.FLEXIBLE,
                mHostActivity,
                REQUEST_CODE_IN_APP_UPDATE
            )
        } catch(error: IntentSender.SendIntentException) {
            unregisterUpdateListener()

            Timber.e("Unable to start the application update process. Error: ${error.localizedMessage}")

            Crashlytics.logException(error)
        }
    }


    private fun registerUpdateListener() {
        mAppUpdateManager?.registerListener(installStateUpdateListener)
    }


    private fun unregisterUpdateListener() {
        mAppUpdateManager?.unregisterListener(installStateUpdateListener)
    }


    fun registerWith(hostActivity: ComponentActivity) {
        mHostActivity = hostActivity.apply {
            lifecycle.addObserver(this@InAppUpdateHelper)
        }
    }


    private fun onUpdateAvailable(updateInfo: AppUpdateInfo) {
        if(mHasNotifiedAboutUpdate) {
            return
        }

        mHasNotifiedAboutUpdate = true
        mAppUpdateInfo = updateInfo

        mState = State.PENDING_UPDATE
    }


    private fun onUpdateFailed() {
        mState = State.UPDATE_FAILED

        unregisterUpdateListener()
    }


    private fun onUpdateCancelled() {
        mState = State.UPDATE_CANCELLED

        unregisterUpdateListener()
    }


    private fun onDownloadingStarted() {
        mState = State.DOWNLOADING_UPDATE
    }


    private fun onDownloadingFinished() {
        mState = State.UPDATE_DOWNLOADED

        unregisterUpdateListener()
    }


    @SuppressLint("SwitchIntDef")
    private val installStateUpdateListener: ((InstallState) -> Unit) = { installState ->
        when(installState.installStatus()) {
            InstallStatus.FAILED -> onUpdateFailed()
            InstallStatus.CANCELED -> onUpdateCancelled()
            InstallStatus.DOWNLOADING -> onDownloadingStarted()
            InstallStatus.DOWNLOADED -> onDownloadingFinished()
        }
    }


    @OnLifecycleEvent(Lifecycle.Event.ON_DESTROY)
    private fun releaseResources() {
        unregisterUpdateListener()

        mAppUpdateManager = null
        mHostActivity = null
    }


    enum class State {

        NO_UPDATES,
        PENDING_UPDATE,
        UPDATE_FAILED,
        UPDATE_CANCELLED,
        DOWNLOADING_UPDATE,
        UPDATE_DOWNLOADED

    }


    interface Listener {

        fun onStateChanged(state: State)

    }


}