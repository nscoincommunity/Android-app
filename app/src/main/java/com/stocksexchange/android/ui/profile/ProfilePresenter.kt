package com.stocksexchange.android.ui.profile

import com.stocksexchange.android.model.ProfileItemModel
import com.stocksexchange.android.ui.base.mvp.presenters.BasePresenter

class ProfilePresenter(
    view: ProfileContract.View,
    model: ProfileModel
) : BasePresenter<ProfileContract.View, ProfileModel>(view, model),
    ProfileContract.ActionListener {


    override fun start() {
        super.start()

        if(mView.isDataSetEmpty()) {
            mView.setItems(mModel.getItems())
        }
    }


    override fun onProfileItemClicked(itemModel: ProfileItemModel) {
        when(itemModel) {
            ProfileItemModel.LOGIN -> onLoginItemClicked()
            ProfileItemModel.REGISTRATION -> onRegistrationItemClicked()
            ProfileItemModel.SETTINGS -> onSettingsItemClicked()
            ProfileItemModel.REFERRAL_PROGRAM -> onReferralProgramItemClicked()
            ProfileItemModel.VERIFICATION -> onVerificationItemClicked()
            ProfileItemModel.SUPPORT_CENTER -> onSupportCenterItemClicked()
            ProfileItemModel.ABOUT -> onAboutItemClicked()
        }
    }


    private fun onLoginItemClicked() {
        mView.launchLoginActivity()
    }


    private fun onRegistrationItemClicked() {
        mView.launchRegistrationActivity()
    }


    private fun onSettingsItemClicked() {
        mView.navigateToSettingsScreen()
    }


    private fun onReferralProgramItemClicked() {
        mView.navigateToReferralScreen()
    }


    private fun onVerificationItemClicked() {
        mView.navigateToVerificationScreen()
    }


    private fun onSupportCenterItemClicked() {
        mView.navigateToHelpScreen()
    }


    private fun onAboutItemClicked() {
        mView.navigateToAboutScreen()
    }


}