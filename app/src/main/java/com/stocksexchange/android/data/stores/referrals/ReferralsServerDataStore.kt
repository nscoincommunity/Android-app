package com.stocksexchange.android.data.stores.referrals

import com.stocksexchange.api.StexRestApi
import com.stocksexchange.api.model.rest.ReferralCodeProvisionResponse
import com.stocksexchange.core.model.Result
import com.stocksexchange.core.utils.helpers.performBackgroundOperation

class ReferralsServerDataStore(
    private val stexRestApi: StexRestApi
) : ReferralsDataStore {


    override suspend fun provideReferralCode(referralCode: String): Result<ReferralCodeProvisionResponse> {
        return performBackgroundOperation {
            stexRestApi.provideReferralCode(referralCode)
        }
    }


}