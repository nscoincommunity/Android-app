package com.stocksexchange.android.utils.providers

import android.content.Context
import androidx.annotation.ArrayRes
import androidx.annotation.PluralsRes
import androidx.annotation.StringRes
import com.stocksexchange.android.R
import com.stocksexchange.api.model.rest.*
import com.stocksexchange.android.model.*
import com.stocksexchange.api.exceptions.rest.InvalidRefreshTokenException
import com.stocksexchange.api.exceptions.rest.TooManyRequestsException
import com.stocksexchange.api.exceptions.rest.UserNotAuthenticatedException
import com.stocksexchange.api.model.rest.OrderMode
import com.stocksexchange.api.model.rest.TransactionMode
import com.stocksexchange.api.model.rest.WalletMode
import com.stocksexchange.api.model.rest.parameters.OrderParameters
import com.stocksexchange.api.model.rest.parameters.TransactionParameters
import com.stocksexchange.api.model.rest.parameters.WalletParameters
import com.stocksexchange.core.exceptions.NoInternetException
import com.stocksexchange.core.utils.extensions.createContextWithLocale
import com.stocksexchange.core.utils.extensions.getLocale
import java.net.ConnectException
import java.net.UnknownHostException
import java.util.Locale

class StringProvider(private val context: Context) {


    private var locale: Locale = context.getLocale()




    fun setLocale(locale: Locale) {
        this.locale = locale
    }


    fun getString(@StringRes id: Int): String {
        return getLocaleAwareContext().getString(id)
    }


    fun getStringWithLocale(locale: Locale, @StringRes id: Int): String {
        return getLocaleAwareContext(locale).getString(id)
    }


    fun getString(@StringRes id: Int, vararg args: Any): String {
        return getLocaleAwareContext().getString(id, *args)
    }


    fun getStringList(@StringRes vararg ids: Int): List<String> {
        return mutableListOf<String>().apply {
            for(id in ids) {
                add(getString(id))
            }
        }
    }


    fun getStringArray(@StringRes vararg ids: Int): Array<String> {
        return getStringList(*ids).toTypedArray()
    }


    fun getQuantityString(@PluralsRes id: Int, quantity: Int, count: Int): String {
        return getLocaleAwareContext().resources.getQuantityString(id, quantity, count)
    }


    fun getStringArray(@ArrayRes id: Int): Array<String> {
        return getLocaleAwareContext().resources.getStringArray(id)
    }


    fun getSomethingWentWrongMessage(): String = getString(R.string.error_something_went_wrong)


    fun getInternetConnectionCheckMessage(): String = getString(R.string.error_check_network_connection)


    fun getErrorMessage(exception: Throwable): String {
        return getString(when(exception) {
            is TooManyRequestsException -> R.string.error_too_many_requests

            // Network related errors (including the ones when the network
            // has just been turned off)
            is NoInternetException,
            is UnknownHostException,
            is ConnectException -> R.string.error_check_network_connection

            is UserNotAuthenticatedException,
            is InvalidRefreshTokenException -> {
                R.string.error_login_session_expired
            }

            else -> R.string.error_something_went_wrong
        })
    }


    fun getCurrencyMarketsEmptyCaption(parameters: CurrencyMarketParameters): String {
        return when(parameters.currencyMarketType) {

            CurrencyMarketType.NORMAL -> {
                getString(R.string.error_no_markets_available, parameters.currencyPairGroup.name)
            }

            CurrencyMarketType.SEARCH -> {
                val searchQuery = parameters.searchQuery

                if(searchQuery.isNotBlank()) {
                    getString(R.string.currency_markets_fragment_search_no_markets_found_template, searchQuery)
                } else {
                    getString(R.string.currency_markets_fragment_search_initial_message)
                }
            }

            CurrencyMarketType.FAVORITES -> getString(R.string.error_no_favorite_markets_available)

        }
    }


    fun getWalletsEmptyCaption(parameters: WalletParameters): String {
        return when(parameters.mode) {

            WalletMode.SEARCH -> {
                val searchQuery = parameters.searchQuery

                if(searchQuery.isNotBlank()) {
                    getString(R.string.wallets_fragment_search_no_wallets_found_template, searchQuery)
                } else {
                    getString(R.string.wallets_fragment_search_initial_message)
                }
            }

            WalletMode.STANDARD -> getString(R.string.error_no_wallets_available)

        }
    }


    fun getTransactionCreationTitle(type: TransactionType): String {
        return getString(when(type) {
            TransactionType.DEPOSITS -> R.string.deposit
            TransactionType.WITHDRAWALS -> R.string.withdrawal
        })
    }


    fun getTransactionCreationEmptyCaption(): String {
        return getString(R.string.error_no_data_available)
    }


    fun getOrdersEmptyCaption(parameters: OrderParameters): String {
        return when(parameters.mode) {

            OrderMode.SEARCH -> {
                val searchQuery = parameters.searchQuery

                if(searchQuery.isNotBlank()) {
                    getString(R.string.orders_fragment_search_no_orders_found_template, searchQuery)
                } else {
                    getString(R.string.orders_fragment_search_initial_message)
                }
            }

            OrderMode.STANDARD -> {
                getString(when(parameters.lifecycleType) {
                    OrderLifecycleType.ACTIVE -> R.string.error_no_active_orders_available
                    OrderLifecycleType.COMPLETED -> R.string.error_no_completed_orders_available
                    OrderLifecycleType.CANCELLED -> R.string.error_no_cancelled_orders_available
                })
            }

        }
    }


    fun getTransactionsEmptyCaption(parameters: TransactionParameters): String {
        return when(parameters.mode) {

            TransactionMode.SEARCH -> {
                val query = parameters.searchQuery

                if(query.isNotBlank()) {
                    getString(when(parameters.type) {
                        TransactionType.DEPOSITS -> R.string.transactions_fragment_search_no_deposits_found_template
                        TransactionType.WITHDRAWALS -> R.string.transactions_fragment_search_no_withdrawals_found_template
                    }, query)
                } else {
                    getString(when(parameters.type) {
                        TransactionType.DEPOSITS -> R.string.transactions_fragment_search_deposits_initial_message
                        TransactionType.WITHDRAWALS -> R.string.transactions_fragment_search_withdrawals_initial_message
                    })
                }
            }

            TransactionMode.STANDARD -> {
                getString(when(parameters.type) {
                    TransactionType.DEPOSITS -> R.string.error_no_deposits_available
                    TransactionType.WITHDRAWALS -> R.string.error_no_withdrawals_available
                })
            }

        }
    }


    fun getPriceChartViewEmptyCaption(): String {
        return getString(R.string.error_no_chart_data_available)
    }


    fun getDepthChartViewEmptyCaption(): String {
        return getPriceChartViewEmptyCaption()
    }


    fun getOrderbookViewEmptyCaption(): String {
        return getString(R.string.error_no_orderbook_available)
    }


    fun getTradeHistoryViewEmptyCaption(): String {
        return getString(R.string.error_no_trades_available)
    }


    fun getUserActiveOrdersViewEmptyCaption(): String {
        return getString(R.string.error_no_active_orders_available)
    }


    fun getUserHistoryOrdersViewEmptyCaption(): String {
        return getString(R.string.error_no_completed_orders_available)
    }


    fun getOrderbookDetailsViewEmptyCaption(): String {
        return getString(R.string.error_no_information_available)
    }


    fun getTradingWalletsErrorCaption(): String {
        return getString(R.string.error)
    }


    fun getPriceAlertsTitle(): String {
        return getString(R.string.alert_price_title)
    }


    fun getInboxEmptyCaption(): String {
        return getString(R.string.inbox_no_available)
    }


    fun getPriceAlertEmptyCaption(): String {
        return getString(R.string.alert_price_no_available)
    }


    fun getTwitterNewsEmptyCaption(): String {
        return getString(R.string.news_no_available)
    }


    fun getBlogNewsEmptyCaption(): String {
        return getString(R.string.news_no_available)
    }


    fun getMessageForPinCodeMode(pinCodeMode: PinCodeMode): String {
        return getString(when(pinCodeMode) {
            PinCodeMode.CREATION -> R.string.authentication_activity_hint_create_pin
            PinCodeMode.CONFIRMATION -> R.string.authentication_activity_hint_confirm_pin
            PinCodeMode.ENTER, PinCodeMode.CHANGE -> R.string.authentication_activity_hint_enter_pin
        })
    }


    fun getPrimaryButtonTextForLoginProcessPhase(loginProcessPhase: LoginProcessPhase): String {
        return getString(when(loginProcessPhase) {
            LoginProcessPhase.AWAITING_CREDENTIALS -> R.string.action_log_in
            LoginProcessPhase.AWAITING_ACCOUNT_VERIFICATION -> R.string.action_resend_verification_email
            LoginProcessPhase.AWAITING_CONFIRMATION -> R.string.action_submit
        })
    }


    fun getPrimaryButtonTextForRegistrationProcessPhase(registrationProcessPhase: RegistrationProcessPhase): String {
        return getString(when(registrationProcessPhase) {
            RegistrationProcessPhase.AWAITING_CREDENTIALS -> R.string.action_register
            RegistrationProcessPhase.AWAITING_ACCOUNT_VERIFICATION -> R.string.action_go_to_login
        })
    }


    fun getPrimaryButtonTextForPasswordRecoveryProcessPhase(passwordRecoveryProcessPhase: PasswordRecoveryProcessPhase): String {
        return getString(when(passwordRecoveryProcessPhase) {
            PasswordRecoveryProcessPhase.AWAITING_EMAIL_ADDRESS -> R.string.action_send_email
            PasswordRecoveryProcessPhase.AWAITING_NEW_PASSWORD -> R.string.action_reset_password
            PasswordRecoveryProcessPhase.PASSWORD_RESET_EMAIL_SENT,
            PasswordRecoveryProcessPhase.PASSWORD_RESET -> R.string.action_go_to_login
        })
    }


    fun getTradeInputViewInvalidValueErrorMessage(inputView: TradeInputView): String {
        return getString(when(inputView) {
            TradeInputView.STOP_PRICE -> R.string.error_invalid_stop_price
            TradeInputView.PRICE -> R.string.error_invalid_price
            TradeInputView.AMOUNT -> R.string.error_invalid_amount
            TradeInputView.TOTAL -> R.string.error_invalid_total
        })
    }


    fun getStopLimitOrderWarningDialogText(tradeType: TradeType, bestPrice: String,
                                           stopPrice: String): String {
        return getString(
            when(tradeType) {
                TradeType.BUY -> R.string.trade_fragment_buy_stop_limit_order_warning_dialog_text
                TradeType.SELL -> R.string.trade_fragment_sell_stop_limit_order_warning_dialog_text
            },
            bestPrice,
            stopPrice
        )
    }


    fun getReferralSubtitle(memberCount: Int): String {
        return getString(
            R.string.referral_fragment_subtitle_text_first_part_template,
            getQuantityString(
                R.plurals.referral_fragment_subtitle_text_second_part_template,
                memberCount,
                memberCount
            )
        )
    }


    private fun getLocaleAwareContext(locale: Locale = this.locale): Context {
        return context.createContextWithLocale(locale)
    }


}