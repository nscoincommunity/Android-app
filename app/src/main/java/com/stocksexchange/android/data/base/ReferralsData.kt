package com.stocksexchange.android.data.base

interface ReferralsData<
    ReferralCodeProvisionResult
> {

    suspend fun provideReferralCode(referralCode: String): ReferralCodeProvisionResult

}