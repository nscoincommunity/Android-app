package com.stocksexchange.api.model.rest

import android.annotation.SuppressLint
import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import com.stocksexchange.core.utils.extensions.getWithDefault
import kotlinx.android.parcel.Parcelize

@Parcelize
data class ProfileInfo(
    @SerializedName("user_id") val id: Long,
    @SerializedName("email") val email: String,
    @SerializedName("username") val userName: String,   // useless, always == email
    @SerializedName("api_withdrawals_allowed") val areWithdrawalsAllowed: Boolean,
    @SerializedName("verifications") val verifications: Map<String, Boolean>,
    @SerializedName("trading_fee_levels") val tradingFeeLevels: Map<String, Double>,
    @SerializedName("referral_program") val referralProgram: ReferralProgram,
    @SerializedName("settings") val settings: Map<String, Boolean>
) : Parcelable {


    companion object {

        private const val STUB_ID = -1L
        private const val STUB_EMAIL = "stub_email"
        private const val STUB_USERNAME = "stub_user_name"
        private const val MOBILE_NOTIFICATION_STATUS = "mobile_notification_status"


        val STUB_PROFILE_INFO = ProfileInfo(
            id = STUB_ID,
            email = STUB_EMAIL,
            userName = STUB_USERNAME,
            areWithdrawalsAllowed = false,
            verifications = mapOf(),
            tradingFeeLevels = mapOf(),
            referralProgram = ReferralProgram.STUB,
            settings = mapOf()
        )

    }


    val isStub: Boolean
        get() = (
            (id == STUB_ID) &&
            (email == STUB_EMAIL) &&
            (userName == STUB_USERNAME)
        )


    val emailUserName: String
        get() = (email.substringBefore('@'))




    fun isVerified(): Boolean {
        for(value in verifications.values) {
            if(value) {
                return true
            }
        }

        return false
    }


    @SuppressLint("DefaultLocale")
    fun isVerifiedBy(type: VerificationType): Boolean {
        return verifications.getWithDefault(type.name.toLowerCase(), false)
    }


    fun getNotificationStatus(): Boolean {
        return settings.getWithDefault(MOBILE_NOTIFICATION_STATUS, false)
    }


    @SuppressLint("DefaultLocale")
    fun getTradingFee(type: VerificationType): Double {
        return tradingFeeLevels.getWithDefault(type.name.toLowerCase(), 0.0)
    }


    fun getTradingFeeInPercentage(type: VerificationType): Double {
        return (getTradingFee(type) * 100.0)
    }


}