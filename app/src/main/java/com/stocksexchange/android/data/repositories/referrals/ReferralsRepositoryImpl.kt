package com.stocksexchange.android.data.repositories.referrals

import com.stocksexchange.api.model.rest.ReferralCodeProvisionResponse
import com.stocksexchange.android.data.stores.referrals.ReferralsDataStore
import com.stocksexchange.android.model.RepositoryResult
import com.stocksexchange.core.model.Result
import com.stocksexchange.core.providers.ConnectionProvider
import com.stocksexchange.core.exceptions.NoInternetException

class ReferralsRepositoryImpl(
    private val serverDataStore: ReferralsDataStore,
    private val connectionProvider: ConnectionProvider
) : ReferralsRepository {


    override suspend fun provideReferralCode(referralCode: String): RepositoryResult<ReferralCodeProvisionResponse> {
        if(!connectionProvider.isNetworkAvailable()) {
            return RepositoryResult(serverResult = Result.Failure(NoInternetException()))
        }

        return RepositoryResult(serverResult = serverDataStore.provideReferralCode(referralCode))
    }


}