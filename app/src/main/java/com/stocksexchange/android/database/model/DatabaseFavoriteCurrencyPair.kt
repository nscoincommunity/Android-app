package com.stocksexchange.android.database.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.stocksexchange.android.database.model.DatabaseFavoriteCurrencyPair.Companion.TABLE_NAME

/**
 * A Room database model class representing user's favorite
 * currency pairs.
 */
@Entity(tableName = TABLE_NAME)
data class DatabaseFavoriteCurrencyPair(
    @PrimaryKey @ColumnInfo(name = ID) var id: Int
) {

    companion object {

        const val TABLE_NAME = "favorite_currency_pairs"

        const val ID = "id"

    }


    constructor(): this(-1)

}