package com.stocksexchange.android.ui.pinrecovery

import com.stocksexchange.android.R
import com.stocksexchange.android.model.TransitionAnimations
import com.stocksexchange.android.theming.ThemingUtil
import com.stocksexchange.android.theming.factories.ThemeFactory
import com.stocksexchange.android.theming.model.Theme
import com.stocksexchange.android.ui.base.activities.BaseActivity
import com.stocksexchange.android.ui.dashboard.DashboardActivity
import com.stocksexchange.android.ui.dashboard.newInstance
import kotlinx.android.synthetic.main.pin_recovery_activity_layout.*
import org.koin.android.ext.android.get
import org.koin.android.ext.android.inject
import org.koin.core.parameter.parametersOf

class PinRecoveryActivity : BaseActivity<PinRecoveryPresenter>(), PinRecoveryContract.View {


    companion object {}


    override val mPresenter: PinRecoveryPresenter by inject { parametersOf(this) }

    private var mTheme: Theme = get<ThemeFactory>().getDefaultTheme()




    override fun init() {
        super.init()

        initContentContainer()
        initAppLogo()
        initAppMotto()
        initTitle()
        initSubtitle()
        initButtons()
    }


    private fun initContentContainer() {
        ThemingUtil.PinRecovery.contentContainer(contentContainerRl, getAppTheme())
    }


    private fun initAppLogo() {
        ThemingUtil.PinRecovery.appLogo(appLogoIv, getAppTheme())
    }


    private fun initAppMotto() {
        with(appMottoTv) {
            text = getStr(R.string.app_motto)

            ThemingUtil.PinRecovery.appMotto(this, getAppTheme())
        }
    }


    private fun initTitle() {
        with(titleTv) {
            text = getStr(R.string.pin_recovery_title_text)

            ThemingUtil.PinRecovery.title(this, getAppTheme())
        }
    }


    private fun initSubtitle() {
        with(subtitleTv) {
            text = getStr(R.string.pin_recovery_subtitle_text)

            ThemingUtil.PinRecovery.subtitle(this, getAppTheme())
        }
    }


    private fun initButtons() {
        initCancellationButton()
        initConfirmationButton()
    }


    private fun initCancellationButton() {
        with(cancellationBtn) {
            text = getStr(R.string.action_cancel)

            setOnClickListener {
                mPresenter.onCancellationButtonClicked()
            }

            ThemingUtil.PinRecovery.cancellationButton(this, getAppTheme())
        }
    }


    private fun initConfirmationButton() {
        with(confirmationBtn) {
            text = getStr(R.string.action_sign_out)

            setOnClickListener {
                mPresenter.onConfirmationButtonClicked()
            }

            ThemingUtil.PinRecovery.confirmationButton(this, getAppTheme())
        }
    }


    override fun launchDashboardActivity() {
        startActivity(DashboardActivity.newInstance(this))
    }


    override fun shouldShowIntercomInAppMessagePopups(): Boolean = false


    override fun getContentLayoutResourceId(): Int = R.layout.pin_recovery_activity_layout


    override fun getTransitionAnimations(): TransitionAnimations {
        return TransitionAnimations.HORIZONTAL_SLIDING_ANIMATIONS
    }


    override fun getAppTheme(): Theme = mTheme


}