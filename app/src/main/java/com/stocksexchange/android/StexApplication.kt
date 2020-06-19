package com.stocksexchange.android

import android.app.Application
import androidx.lifecycle.ProcessLifecycleOwner
import com.crashlytics.android.Crashlytics
import com.google.firebase.analytics.FirebaseAnalytics
import com.stocksexchange.android.di.applicationModules
import com.stocksexchange.android.utils.listeners.ApplicationLifecycleListener
import com.stocksexchange.core.utils.CrashReportingTree
import io.fabric.sdk.android.Fabric
import io.intercom.android.sdk.Intercom
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin
import org.koin.core.logger.EmptyLogger
import timber.log.Timber

class StexApplication : Application() {


    companion object {

        lateinit var INSTANCE: StexApplication

    }




    private val lifecycleListener: ApplicationLifecycleListener by lazy {
        ApplicationLifecycleListener(this)
    }


    override fun onCreate() {
        super.onCreate()

        INSTANCE = this


        initAppLifecycleListener()
        initDi()
        initLogger()
        initFirebaseAnalytics()
        initCrashlytics()
        initIntercom()
    }


    private fun initAppLifecycleListener() {
        ProcessLifecycleOwner.get().lifecycle
            .addObserver(lifecycleListener)
    }


    private fun initDi() {
        startKoin {
            logger(EmptyLogger())
            androidContext(this@StexApplication)
            modules(applicationModules)
        }
    }


    private fun initLogger() {
        if(BuildConfig.DEBUG) {
            Timber.plant(Timber.DebugTree())
        } else {
            Timber.plant(CrashReportingTree())
        }
    }


    private fun initFirebaseAnalytics() {
        if(!BuildConfig.DEBUG) {
            FirebaseAnalytics.getInstance(this)
        }
    }


    private fun initCrashlytics() {
        if(!BuildConfig.DEBUG) {
            Fabric.with(this, Crashlytics())
        }
    }


    private fun initIntercom() {
        Intercom.initialize(
            this,
            BuildConfig.INTERCOM_API_KEY,
            BuildConfig.INTERCOM_APP_ID
        )
    }


}