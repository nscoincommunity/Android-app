package com.stocksexchange.android.database.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.stocksexchange.api.model.rest.CurrencyPairGroup
import com.stocksexchange.android.database.model.DatabaseCurrencyPairGroup.Companion.TABLE_NAME

/**
 * A Room database model for the [CurrencyPairGroup] class.
 */
@Entity(tableName = TABLE_NAME)
data class DatabaseCurrencyPairGroup(
    @PrimaryKey @ColumnInfo(name = ID) var id: Int,
    @ColumnInfo(name = NAME) var name: String,
    @ColumnInfo(name = POSITION) var position: Int
) {

    companion object {

        const val TABLE_NAME = "currency_pair_groups"

        const val ID = "id"
        const val NAME = "name"
        const val POSITION = "position"

    }

    constructor(): this(
        id = -1,
        name = "",
        position = -1
    )

}