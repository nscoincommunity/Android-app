package com.stocksexchange.android.database.utils

import androidx.sqlite.db.SimpleSQLiteQuery
import androidx.sqlite.db.SupportSQLiteQuery
import com.stocksexchange.android.database.model.*
import com.stocksexchange.api.model.rest.OrderStatus

/**
 * A builder used for creating complex SQL queries.
 */
object QueryBuilder {


    private const val PLACEHOLDER_SYMBOL = "?"


    object Deposits {


        fun getSearchQuery(query: String, sortOrder: String,
                           limit: Int, offset: Int): SimpleSQLiteQuery {
            val rawQuery = """
                SELECT * FROM ${DatabaseDeposit.TABLE_NAME} 
                WHERE ${DatabaseDeposit.CURRENCY_SYMBOL} LIKE (? || '%') 
                ORDER BY ${DatabaseDeposit.TIMESTAMP} $sortOrder 
                LIMIT ?, ?
            """.trimIndent()

            return toSimpleSqliteQuery(
                rawQuery = rawQuery,
                args = listOf(query, offset, limit)
            )
        }


        fun getQuery(sortOrder: String, limit: Int, offset: Int): SimpleSQLiteQuery {
            val rawQuery = """
                SELECT * FROM ${DatabaseDeposit.TABLE_NAME} 
                ORDER BY ${DatabaseDeposit.TIMESTAMP} $sortOrder 
                LIMIT ?, ?
            """.trimIndent()

            return toSimpleSqliteQuery(
                rawQuery = rawQuery,
                args = listOf(offset, limit)
            )
        }


    }


    object Inbox {


        fun getQuery(limit: Int): SimpleSQLiteQuery {
            val rawQuery = """
                SELECT * FROM ${DatabaseInbox.TABLE_NAME} 
                LIMIT ?, ?
            """.trimIndent()

            return toSimpleSqliteQuery(
                    rawQuery = rawQuery,
                    args = listOf(limit)
            )
        }

    }


    object Orders {


        fun getDeleteQuery(status: OrderStatus): SupportSQLiteQuery {
            val rawQuery = """
                DELETE FROM ${DatabaseOrder.TABLE_NAME} 
                ${getQueryWherePartStr(status)}
            """.trimIndent()

            return toSimpleSqliteQuery(
                rawQuery = rawQuery,
                args = getQueryWherePartArgs(status)
            )
        }


        fun getSearchQuery(status: OrderStatus, currencyPairIds: List<Int>,
                           sortOrder: String, limit: Int, offset: Int): SupportSQLiteQuery {
            val rawQuery = """
                SELECT * FROM ${DatabaseOrder.TABLE_NAME} 
                ${getQueryWherePartStr(status)} AND 
                ${DatabaseOrder.CURRENCY_PAIR_ID} IN (${currencyPairIds.joinToString()}) 
                ORDER BY ${DatabaseOrder.TIMESTAMP} $sortOrder 
                LIMIT ?, ?
            """.trimIndent()

            val args = getQueryWherePartArgs(status).apply {
                add(offset)
                add(limit)
            }

            return toSimpleSqliteQuery(
                rawQuery = rawQuery,
                args = args
            )
        }


        fun getQuery(status: OrderStatus, currencyPairId: String,
                     sortOrder: String, limit: Int, offset: Int): SupportSQLiteQuery {
            val rawQuery = StringBuilder().apply {
                append(
                    "SELECT * FROM ${DatabaseOrder.TABLE_NAME} " +
                    "${getQueryWherePartStr(status)} "
                )

                if(currencyPairId.isNotBlank()) {
                    append("AND ${DatabaseOrder.CURRENCY_PAIR_ID} = $currencyPairId ")
                }

                append(
                    "ORDER BY ${DatabaseOrder.TIMESTAMP} $sortOrder " +
                    "LIMIT ?, ?"
                )
            }.toString()

            val args = getQueryWherePartArgs(status).apply {
                add(offset)
                add(limit)
            }

            return toSimpleSqliteQuery(
                rawQuery = rawQuery,
                args = args
            )
        }


        private fun getQueryWherePartStr(status: OrderStatus): String {
            val placeholderString = generatePlaceholderString(getQueryWherePartArgs(status).size)

            return "WHERE ${DatabaseOrder.STATUS_STR} IN ($placeholderString)"
        }


        private fun getQueryWherePartArgs(status: OrderStatus): MutableList<Any> {
            return status.getStatusNames().toMutableList()
        }


    }


    object TradeHistory {


        fun getQuery(currencyPairId: Int, sortOrder: String, count: Int): SupportSQLiteQuery {
            val rawQuery = """
                SELECT * FROM ${DatabaseTrade.TABLE_NAME} 
                WHERE ${DatabaseTrade.CURRENCY_PAIR_ID} = ? 
                ORDER BY ${DatabaseTrade.TIMESTAMP} $sortOrder 
                LIMIT ?
            """.trimIndent()

            return toSimpleSqliteQuery(
                rawQuery = rawQuery,
                args = listOf(currencyPairId, count)
            )
        }


    }


    object Wallets {


        fun getSearchQuery(query: String, sortColumn: String, sortOrder: String): SimpleSQLiteQuery {
            val rawQuery = """
                SELECT * FROM ${DatabaseWallet.TABLE_NAME} 
                WHERE LOWER(${DatabaseWallet.CURRENCY_SYMBOL}) LIKE (? || '%') OR 
                LOWER(${DatabaseWallet.CURRENCY_NAME}) LIKE (? || '%') 
                ORDER BY $sortColumn $sortOrder
            """.trimIndent()

            return toSimpleSqliteQuery(
                rawQuery = rawQuery,
                args = listOf(query, query)
            )
        }


        fun getAllQuery(sortColumn: String, sortOrder: String): SimpleSQLiteQuery {
            return toSimpleSqliteQuery(
                """
                SELECT * FROM ${DatabaseWallet.TABLE_NAME} 
                ORDER BY $sortColumn $sortOrder
                """
            )
        }


    }


    object Withdrawals {


        fun getSearchQuery(query: String, sortOrder: String,
                           limit: Int, offset: Int): SupportSQLiteQuery {
            val rawQuery = """
                SELECT * FROM ${DatabaseWithdrawal.TABLE_NAME} 
                WHERE ${DatabaseWithdrawal.CURRENCY_SYMBOL} LIKE (? || '%') 
                ORDER BY ${DatabaseWithdrawal.CREATION_TIMESTAMP} $sortOrder 
                LIMIT ?, ?
            """.trimIndent()

            return toSimpleSqliteQuery(
                rawQuery = rawQuery,
                args = listOf(query, offset, limit)
            )
        }


        fun getQuery(sortOrder: String, limit: Int, offset: Int): SupportSQLiteQuery {
            val rawQuery = """
                SELECT * FROM ${DatabaseWithdrawal.TABLE_NAME} 
                ORDER BY ${DatabaseWithdrawal.CREATION_TIMESTAMP} $sortOrder 
                LIMIT ?, ?
            """.trimIndent()

            return toSimpleSqliteQuery(
                rawQuery = rawQuery,
                args = listOf(offset, limit)
            )
        }


    }


    private fun generatePlaceholderString(argCount: Int): String {
        return if(argCount == 1) {
            PLACEHOLDER_SYMBOL
        } else {
            ("$PLACEHOLDER_SYMBOL, ".repeat(argCount - 1) + PLACEHOLDER_SYMBOL)
        }
    }


    /**
     * Converts a raw query into an instance of [SimpleSQLiteQuery].
     *
     * @param rawQuery The raw query to convert to
     *
     * @return The instance of [SimpleSQLiteQuery]
     */
    fun toSimpleSqliteQuery(rawQuery: String): SimpleSQLiteQuery {
        return SimpleSQLiteQuery(rawQuery)
    }


    /**
     * Converts a raw query into an instance of [SimpleSQLiteQuery].
     * Has support for binding arguments.
     *
     * @param rawQuery The raw query to convert to
     * @param args The arguments to bind to the query
     *
     * @return The instance of [SimpleSQLiteQuery]
     */
    fun toSimpleSqliteQuery(rawQuery: String, args: List<Any>): SimpleSQLiteQuery {
        return SimpleSQLiteQuery(rawQuery, args.toTypedArray())
    }


}