package com.stocksexchange.android.ui.splash

import android.content.Intent
import android.os.Bundle
import com.stocksexchange.android.R
import com.stocksexchange.android.model.PinCodeMode
import com.stocksexchange.android.model.Shortcut
import com.stocksexchange.android.model.SystemWindowType
import com.stocksexchange.android.model.TransitionAnimations
import com.stocksexchange.android.theming.ThemingUtil
import com.stocksexchange.android.theming.factories.ThemeFactory
import com.stocksexchange.android.ui.auth.AuthenticationActivity
import com.stocksexchange.android.ui.auth.newInstance
import com.stocksexchange.android.ui.base.activities.BaseActivity
import com.stocksexchange.android.ui.dashboard.DashboardActivity
import com.stocksexchange.android.ui.dashboard.newInstance
import com.stocksexchange.android.ui.login.LoginActivity
import com.stocksexchange.android.ui.login.newInstance
import com.stocksexchange.android.utils.handlers.ShortcutsHandler
import com.stocksexchange.core.utils.extensions.*
import kotlinx.android.synthetic.main.splash_activity_layout.*
import org.koin.android.ext.android.get
import org.koin.android.ext.android.inject
import org.koin.core.parameter.parametersOf

class SplashActivity : BaseActivity<SplashPresenter>(), SplashContract.View {


    companion object {}


    override val mPresenter: SplashPresenter by inject { parametersOf(this) }

    private lateinit var mDestinationIntent: Intent




    override fun preInit() {
        super.preInit()

        mDestinationIntent = DashboardActivity.newInstance(this)
    }


    override fun onFetchExtras(extras: Bundle) {
        extras.extract(extrasExtractor).also {
            when {
                it.hasShortcutName -> onShortcutNameExtraFetched(it.shortcutName)
                it.hasDestinationIntent -> onDestinationIntentExtraFetched(it.destinationIntent!!)
            }
        }
    }


    private fun onShortcutNameExtraFetched(shortcutName: String) {
        val shortcut = Shortcut.valueOf(shortcutName).also {
            mPresenter.shortcut = it
        }

        mDestinationIntent = get<ShortcutsHandler>().getDestinationIntentForShortcut(
            context = this,
            shortcut = shortcut
        )
    }


    private fun onDestinationIntentExtraFetched(intent: Intent) {
        mDestinationIntent = intent
    }


    override fun init() {
        super.init()

        initInfoViewContainer()
    }


    private fun initInfoViewContainer() {
        hideInfoViewContainer()

        with(ThemingUtil.Splash) {
            infoViewTitle(titleTv)
            infoViewProgressBar(progressBar)
        }
    }


    override fun getContentLayoutResourceId(): Int = R.layout.splash_activity_layout


    override fun shouldUpdateStringProviderLocale(): Boolean = false


    override fun shouldSetColorForSystemWindow(type: SystemWindowType): Boolean = false


    override fun showInfoViewContainer(caption: String) {
        titleTv.text = caption

        infoViewContainer.makeVisible()
    }


    override fun hideInfoViewContainer() {
        infoViewContainer.makeGone()
    }


    override fun launchLoginActivity() {
        startActivity(LoginActivity.newInstance(
            context = this,
            destinationIntent = mDestinationIntent
        ))
    }


    override fun launchAuthenticationActivity(pinCodeMode: PinCodeMode) {
        startActivity(AuthenticationActivity.newInstance(
            context = this,
            pinCodeMode = pinCodeMode,
            transitionAnimations = TransitionAnimations.OVERSHOOT_SCALING_ANIMATIONS,
            theme = get<ThemeFactory>().getDefaultTheme(),
            destinationIntent = mDestinationIntent
        ))
    }


    override fun launchDestinationActivity() {
        startActivity(mDestinationIntent)
    }

    override fun shouldUpdateLastInteractionTime(): Boolean = false


    override fun shouldShowIntercomInAppMessagePopups(): Boolean = false


}