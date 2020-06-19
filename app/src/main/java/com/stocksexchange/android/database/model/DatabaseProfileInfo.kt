package com.stocksexchange.android.database.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.stocksexchange.api.model.rest.ProfileInfo
import com.stocksexchange.api.model.rest.ReferralProgram
import com.stocksexchange.android.database.model.DatabaseProfileInfo.Companion.TABLE_NAME

/**
 * A Room database model for the [ProfileInfo] class.
 */
@Entity(tableName = TABLE_NAME)
data class DatabaseProfileInfo(
    @PrimaryKey @ColumnInfo(name = ID) var id: Long,
    @ColumnInfo(name = EMAIL) var email: String,
    @ColumnInfo(name = USER_NAME) var userName: String,
    @ColumnInfo(name = ARE_WITHDRAWALS_ALLOWED) var areWithdrawalsAllowed: Boolean,
    @ColumnInfo(name = VERIFICATIONS) var verifications: Map<String, Boolean>,
    @ColumnInfo(name = TRADING_FEE_LEVELS) var tradingFeeLevels: Map<String, Double>,
    @ColumnInfo(name = REFERRAL_PROGRAM) var referralProgram: ReferralProgram,
    @ColumnInfo(name = SETTINGS) var settings: Map<String, Boolean>
) {


    companion object {

        const val TABLE_NAME = "profile_infos"

        const val ID = "id"
        const val EMAIL = "email"
        const val USER_NAME = "user_name"
        const val ARE_WITHDRAWALS_ALLOWED = "are_withdrawals_allowed"
        const val VERIFICATIONS = "verifications"
        const val TRADING_FEE_LEVELS = "trading_fee_levels"
        const val REFERRAL_PROGRAM = "referral_program"
        const val SETTINGS = "settings"
    }


    constructor() : this(
        id = -1L,
        email = "",
        userName = "",
        areWithdrawalsAllowed = false,
        verifications = mapOf(),
        tradingFeeLevels = mapOf(),
        referralProgram = ReferralProgram.STUB,
        settings = mapOf()
    )


}