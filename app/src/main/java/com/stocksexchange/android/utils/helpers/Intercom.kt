package com.stocksexchange.android.utils.helpers

import com.stocksexchange.android.Constants
import com.stocksexchange.core.model.NetworkGeneration
import com.stocksexchange.core.model.NetworkInfo
import com.stocksexchange.core.model.NetworkType

/**
 * Retrieves an intentional delay in milliseconds to wait for
 * an intercom user registration completion since there is no way
 * to know exactly when it is finished.
 *
 * @param networkInfo The network information
 *
 * @return The delay in milliseconds
 */
fun getIntercomUserRegistrationDelay(networkInfo: NetworkInfo): Long {
    return when(networkInfo.networkType) {
        NetworkType.NOT_AVAILABLE,
        NetworkType.UNKNOWN -> Constants.INTERCOM_USER_REGISTRATION_DELAY_IN_MS_DEFAULT
        NetworkType.WIFI -> Constants.INTERCOM_USER_REGISTRATION_DELAY_IN_MS_WIFI
        NetworkType.CELLULAR -> when(networkInfo.networkGeneration) {
            NetworkGeneration.SECOND_GEN -> Constants.INTERCOM_USER_REGISTRATION_DELAY_IN_MS_2G
            NetworkGeneration.THIRD_GEN -> Constants.INTERCOM_USER_REGISTRATION_DELAY_IN_MS_3G
            NetworkGeneration.FOURTH_GEN -> Constants.INTERCOM_USER_REGISTRATION_DELAY_IN_MS_4G
            NetworkGeneration.FIFTH_GEN -> Constants.INTERCOM_USER_REGISTRATION_DELAY_IN_MS_5G
            NetworkGeneration.UNKNOWN -> Constants.INTERCOM_USER_REGISTRATION_DELAY_IN_MS_DEFAULT
        }
    }
}