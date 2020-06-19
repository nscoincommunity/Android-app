package com.stocksexchange.android.data.repositories.referrals

import com.stocksexchange.api.model.rest.ReferralCodeProvisionResponse
import com.stocksexchange.android.data.base.ReferralsData
import com.stocksexchange.android.data.repositories.base.Repository
import com.stocksexchange.android.model.RepositoryResult

interface ReferralsRepository : ReferralsData<
    RepositoryResult<ReferralCodeProvisionResponse>
>, Repository