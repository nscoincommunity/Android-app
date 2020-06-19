package com.stocksexchange.android.ui.profile

import com.stocksexchange.android.model.ProfileItemModel
import com.stocksexchange.android.ui.base.mvp.model.BaseModel
import com.stocksexchange.android.ui.profile.ProfileModel.ActionListener
import com.stocksexchange.android.utils.managers.SessionManager

class ProfileModel(
    private val sessionManager: SessionManager
) : BaseModel<ActionListener>() {


    fun getItems(): List<ProfileItemModel> {
        return mutableListOf<ProfileItemModel>().apply {
            if(!sessionManager.isUserSignedIn()) {
                add(ProfileItemModel.LOGIN)
                add(ProfileItemModel.REGISTRATION)
            }

            add(ProfileItemModel.SETTINGS)

            if(sessionManager.isUserSignedIn()) {
                add(ProfileItemModel.REFERRAL_PROGRAM)
                add(ProfileItemModel.VERIFICATION)
            }

            add(ProfileItemModel.SUPPORT_CENTER)
            add(ProfileItemModel.ABOUT)
        }
    }


    interface ActionListener : BaseActionListener


}