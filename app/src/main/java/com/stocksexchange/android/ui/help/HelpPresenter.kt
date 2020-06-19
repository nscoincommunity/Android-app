package com.stocksexchange.android.ui.help

import com.stocksexchange.android.model.HelpItemModel
import com.stocksexchange.android.ui.base.mvp.presenters.BasePresenter
import com.stocksexchange.android.utils.handlers.IntercomHandler
import com.stocksexchange.android.utils.helpers.getIntercomUserRegistrationDelay
import com.stocksexchange.android.utils.managers.SessionManager
import com.stocksexchange.core.handlers.PreferenceHandler
import com.stocksexchange.core.providers.ConnectionProvider

class HelpPresenter(
    view: HelpContract.View,
    model: HelpModel,
    private val preferenceHandler: PreferenceHandler,
    private val intercomHandler: IntercomHandler,
    private val connectionProvider: ConnectionProvider,
    private val sessionManager: SessionManager
) : BasePresenter<HelpContract.View, HelpModel>(view, model),
    HelpContract.ActionListener, HelpModel.ActionListener {


    init {
        model.setActionListener(this)
    }


    override fun start() {
        super.start()

        if(mView.isDataSetEmpty()) {
            mView.setItems(mModel.getItems())
        }
    }


    override fun onItemClicked(item: HelpItemModel) {
        if(sessionManager.isUserSignedIn()) {
            registerIntercomIdentifiableUserIfNeeded(item)
        } else {
            registerIntercomUnidentifiableUserIfNeeded(item)
        }
    }


    private fun registerIntercomIdentifiableUserIfNeeded(item: HelpItemModel) {
        registerIntercomUserIfNeeded(
            item = item,
            isUserRegistered = {
                preferenceHandler.isIntercomIdentifiableUserRegistered()
            },
            registerUser = {
                intercomHandler.registerIdentifiableUser(sessionManager.getProfileInfo()?.email ?: "")
            },
            onUserRegistered = {
                preferenceHandler.saveIntercomIdentifiableUserRegistered(true)
            }
        )
    }


    private fun registerIntercomUnidentifiableUserIfNeeded(item: HelpItemModel) {
        registerIntercomUserIfNeeded(
            item = item,
            isUserRegistered = {
                preferenceHandler.isIntercomUnidentifiableUserRegistered()
            },
            registerUser = {
                intercomHandler.registerUnidentifiableUser()
            },
            onUserRegistered = {
                preferenceHandler.saveIntercomUnidentifiableUserRegistered(true)
            }
        )
    }


    private fun registerIntercomUserIfNeeded(
        item: HelpItemModel,
        isUserRegistered: (() -> Boolean),
        registerUser: (() -> Unit),
        onUserRegistered: (() -> Unit)
    ) {
        if(!isUserRegistered()) {
            if(!connectionProvider.isNetworkAvailable()) {
                mView.showToast(mStringProvider.getInternetConnectionCheckMessage())
                return
            }

            mView.showProgressBar()
            registerUser()

            mModel.runIntercomUserRegistrationDelay(
                delayTimeInMillis = getIntercomUserRegistrationDelay(mView.getNetworkInfo()),
                onFinish = {
                    mView.hideProgressBar()

                    onUserRegistered()
                    proceedToIntercomScreen(item)
                }
            )
        } else {
            proceedToIntercomScreen(item)
        }
    }


    private fun proceedToIntercomScreen(item: HelpItemModel) {
        when(item) {
            HelpItemModel.FAQ -> intercomHandler.showHelpCenter()
            HelpItemModel.SUPPORT -> intercomHandler.showMessenger()
        }
    }


    override fun onBackPressed(): Boolean {
        return onNavigateUpPressed()
    }


}