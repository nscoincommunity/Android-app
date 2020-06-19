package com.stocksexchange.android.data.stores.referrals

import com.stocksexchange.api.model.rest.ReferralCodeProvisionResponse
import com.stocksexchange.android.data.base.ReferralsData
import com.stocksexchange.core.model.Result

interface ReferralsDataStore : ReferralsData<
    Result<ReferralCodeProvisionResponse>
>