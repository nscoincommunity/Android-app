package com.stocksexchange.android.ui.deeplinkhandler

import com.stocksexchange.android.R
import com.stocksexchange.api.model.rest.CurrencyMarket
import com.stocksexchange.android.ui.base.activities.BaseActivity
import com.stocksexchange.android.ui.dashboard.DashboardActivity
import com.stocksexchange.android.ui.dashboard.newInstance
import com.stocksexchange.android.ui.login.LoginActivity
import com.stocksexchange.android.ui.login.newInstance
import com.stocksexchange.android.ui.passwordrecovery.PasswordRecoveryActivity
import com.stocksexchange.android.ui.passwordrecovery.newSecondPhaseInstance
import com.stocksexchange.android.ui.splash.SplashActivity
import com.stocksexchange.android.ui.splash.newInstance
import com.stocksexchange.android.utils.DashboardArgsCreator
import com.stocksexchange.core.utils.extensions.isNetworkAvailable
import org.koin.android.ext.android.get
import org.koin.android.ext.android.inject
import org.koin.core.parameter.parametersOf

class DeepLinkHandlerActivity : BaseActivity<DeepLinkHandlerPresenter>(), DeepLinkHandlerContract.View {


    override val mPresenter: DeepLinkHandlerPresenter by inject { parametersOf(this) }




    override fun shouldInitActivity(): Boolean {
        return if(!isNetworkAvailable()) {
            showToast(getString(R.string.error_check_network_connection))
            finishActivity()

            false
        } else if(!intent.getBooleanExtra(ExtrasKeys.KEY_IS_APP_INITIALIZED, false)) {
            intent.putExtra(ExtrasKeys.KEY_IS_APP_INITIALIZED, true)

            startActivity(SplashActivity.newInstance(this, intent))
            finishActivity()

            false
        } else {
            true
        }
    }


    override fun init() {
        super.init()

        if((intent != null) && (intent.data != null)) {
            mPresenter.onUriRetrieved(intent.data!!.toString())
        } else {
            finishActivityWithError(getString(R.string.error_invalid_deep_link))
        }
    }


    override fun shouldUpdateLastInteractionTime(): Boolean = false


    override fun canObserveNetworkStateChanges(): Boolean = false


    override fun getContentLayoutResourceId(): Int = R.layout.deep_link_handler_activity_layout


    override fun launchLoginActivity() {
        startActivity(LoginActivity.newInstance(
            context = this,
            wasAccountJustVerified = true
        ))
    }


    override fun launchPasswordRecoveryActivity(passwordResetToken: String) {
        startActivity(PasswordRecoveryActivity.newSecondPhaseInstance(
            context = this,
            passwordResetToken = passwordResetToken
        ))
    }


    override fun navigateToCurrencyMarketPreviewScreen(currencyMarket: CurrencyMarket) {
        startActivity(DashboardActivity.newInstance(
            context = this,
            dashboardArgs = get<DashboardArgsCreator>().getCurrencyMarketPreviewScreenOpeningArgs(
                currencyMarket = currencyMarket
            )
        ))
    }


    override fun navigateToWithdrawalsScreen(wasWithdrawalJustConfirmed: Boolean, wasWithdrawalJustCancelled: Boolean) {
        startActivity(DashboardActivity.newInstance(
            context = this,
            dashboardArgs = get<DashboardArgsCreator>().getWithdrawalsScreenOpeningArgs(
                wasWithdrawalJustConfirmed = wasWithdrawalJustConfirmed,
                wasWithdrawalJustCancelled = wasWithdrawalJustCancelled
            )
        ))
    }


    override fun finishActivityWithError(error: String) {
        showToast(error)
        finishActivity()
    }


}