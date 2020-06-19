package com.stocksexchange.android.ui.referral

import com.stocksexchange.api.model.rest.ProfileInfo
import com.stocksexchange.android.data.repositories.profileinfos.ProfileInfosRepository
import com.stocksexchange.android.data.repositories.referrals.ReferralsRepository
import com.stocksexchange.android.ui.base.mvp.model.BaseModel
import com.stocksexchange.android.ui.referral.ReferralModel.ActionListener

class ReferralModel(
    private val referralsRepository: ReferralsRepository,
    private val profileInfosRepository: ProfileInfosRepository
) : BaseModel<ActionListener>() {


    companion object {

        const val REQUEST_TYPE_REFERRAL_CODE_PROVISION = 0

    }




    fun performReferralCodeProvision(referralCode: String) {
        performRequest(
            requestType = REQUEST_TYPE_REFERRAL_CODE_PROVISION,
            params = referralCode
        )
    }


    override suspend fun getRequestRepositoryResult(requestType: Int, params: Any): Any? {
        return when(requestType) {

            REQUEST_TYPE_REFERRAL_CODE_PROVISION -> {
                referralsRepository.provideReferralCode(params as String)
            }

            else -> throw IllegalStateException()

        }
    }


    fun saveProfileInfo(profileInfo: ProfileInfo, onFinish: (() -> Unit)) {
        createUiLaunchCoroutine {
            profileInfosRepository.save(profileInfo)

            onFinish()
        }
    }


    interface ActionListener : BaseActionListener


}