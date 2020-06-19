package com.stocksexchange.api.utils

import com.google.gson.Gson
import com.google.gson.JsonObject
import com.stocksexchange.api.model.rest.*
import com.stocksexchange.api.model.rss.NewsBlogRssModel
import com.stocksexchange.api.exceptions.ApiException
import com.stocksexchange.api.exceptions.rest.*
import com.stocksexchange.api.exceptions.rss.NewsBlogException
import com.stocksexchange.api.model.EndpointType
import com.stocksexchange.core.model.Result
import com.stocksexchange.core.utils.extensions.fromJson
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.HttpException
import retrofit2.Response

class ResponseExtractor(private val gson: Gson) {


    private fun <T> toResult(call: Call<T>): Result<T> {
        return try {
            val response = call.execute()

            if(response.isSuccessful) {
                val body = response.body()

                if(body != null) {
                    Result.Success(body)
                } else {
                    Result.Failure(NullPointerException("Response body is null."))
                }
            } else {
                Result.Failure(HttpException(response))
            }
        } catch(exception: Exception) {
            Result.Failure(exception)
        }
    }


    private fun <In, Out> extractResult(
        call: Call<In>,
        endpointType: EndpointType,
        onError: ((ApiResponse<Out>) -> Throwable) = { ApiException() },
        onSuccess: ((In) -> Result<Out>)
    ): Result<Out> {
        val result = toResult(call)

        return when(result) {
            is Result.Success -> onSuccess(result.value)
            is Result.Failure -> if(result.exception is HttpException) {
                handleErrorResponse(
                    response = (result.exception as HttpException).response(),
                    endpointType = endpointType,
                    onError = onError
                )
            } else {
                result
            }
        }
    }


    private fun <In, Out> handleErrorResponse(
        response: Response<In>,
        endpointType: EndpointType,
        onError: ((ApiResponse<Out>)) -> Throwable = { ApiException() }
    ): Result<Out> {
        if(response.errorBody() !is ResponseBody) {
            return Result.Failure(ApiException())
        }

        val errorBody = (response.errorBody() as ResponseBody)
        val apiResponse = gson.fromJson<ApiResponse<Out>>(errorBody.string())

        if(!apiResponse.hasMessage) {
            return Result.Failure(ApiException())
        }

        val errorMessage = apiResponse.message

        if(errorMessage == GeneralError.TOO_MANY_ATTEMPTS.message) {
            return Result.Failure(TooManyRequestsException())
        }

        if((endpointType == EndpointType.PRIVATE) &&
            (errorMessage == GeneralError.UNAUTHENTICATED.message)) {
            return Result.Failure(UserNotAuthenticatedException())
        }

        return Result.Failure(onError(apiResponse))
    }


    private fun <T> extractSimpleResult(
        call: Call<T>,
        endpointType: EndpointType,
        onError: ((ApiResponse<T>)) -> Throwable = { ApiException() },
        onSuccess: ((T) -> Result<T>) = { Result.Success(it) }
    ): Result<T> {
        return extractResult(
            call = call,
            endpointType = endpointType,
            onError = onError,
            onSuccess = onSuccess
        )
    }


    private fun <In : ApiResponse<Out>, Out> extractAdvancedResult(
        call: Call<In>,
        endpointType: EndpointType,
        onError: ((ApiResponse<Out>) -> Throwable) = { ApiException() }
    ): Result<Out> {
        return extractResult(call, endpointType, onError) { rawResult ->
            rawResult.takeIf { rawResult.isSuccess && rawResult.hasData }
                    ?.let { Result.Success(it.data!!) }
                    ?: Result.Failure(ApiException())
        }
    }


    fun extractPingResponse(call: Call<ApiResponse<PingResponse>>): Result<PingResponse> {
        return extractAdvancedResult(call = call, endpointType = EndpointType.PUBLIC)
    }


    fun extractCurrenciesResponse(call: Call<ApiResponse<List<Currency>>>): Result<List<Currency>> {
        return extractAdvancedResult(call = call, endpointType = EndpointType.PUBLIC)
    }


    fun extractCurrencyResponse(call: Call<ApiResponse<Currency>>): Result<Currency> {
        return extractAdvancedResult(call = call, endpointType = EndpointType.PUBLIC)
    }


    fun extractMarketsResponse(call: Call<ApiResponse<List<Market>>>): Result<List<Market>> {
        return extractAdvancedResult(call = call, endpointType = EndpointType.PUBLIC)
    }


    fun extractCurrencyPairsResponse(call: Call<ApiResponse<List<CurrencyPair>>>): Result<List<CurrencyPair>> {
        return extractAdvancedResult(call = call, endpointType = EndpointType.PUBLIC)
    }


    fun extractCurrencyPairResponse(call: Call<ApiResponse<CurrencyPair>>): Result<CurrencyPair> {
        return extractAdvancedResult(call = call, endpointType = EndpointType.PUBLIC)
    }


    fun extractCurrencyPairGroupsResponse(call: Call<ApiResponse<List<CurrencyPairGroup>>>): Result<List<CurrencyPairGroup>> {
        return extractAdvancedResult(call = call, endpointType = EndpointType.PUBLIC)
    }


    fun extractTickerItemsResponse(call: Call<ApiResponse<List<TickerItem>>>): Result<List<TickerItem>> {
        return extractAdvancedResult(call = call, endpointType = EndpointType.PUBLIC)
    }


    fun extractTickerItemResponse(call: Call<ApiResponse<TickerItem>>): Result<TickerItem> {
        return extractAdvancedResult(call = call, endpointType = EndpointType.PUBLIC)
    }


    fun extractHistoryTradesResponse(call: Call<ApiResponse<List<Trade>>>): Result<List<Trade>> {
        return extractAdvancedResult(call = call, endpointType = EndpointType.PUBLIC)
    }


    fun extractOrderbookResponse(call: Call<ApiResponse<Orderbook>>): Result<Orderbook> {
        return extractAdvancedResult(call = call, endpointType = EndpointType.PUBLIC)
    }


    fun extractCandleSticksResponse(call: Call<ApiResponse<List<CandleStick>>>): Result<List<CandleStick>> {
        return extractAdvancedResult(call = call, endpointType = EndpointType.PUBLIC)
    }


    fun extractSignUpResponse(call: Call<SignUpResponse>): Result<SignUpResponse> {
        val onError: ((ApiResponse<SignUpResponse>) -> Throwable) = {
            if(it.hasErrors) {
                RegistrationException.multiple(it.errorList)
            } else {
                RegistrationException.unknown(it.message)
            }
        }

        return extractSimpleResult(
            call = call,
            endpointType = EndpointType.PUBLIC,
            onError = onError
        )
    }


    fun extractAccountVerificationEmailSendingResponse(
        call: Call<AccountVerificationEmailSendingResponse>
    ): Result<AccountVerificationEmailSendingResponse> {
        val onError: ((ApiResponse<AccountVerificationEmailSendingResponse>) -> Throwable) = {
            if(it.message == GeneralError.NOTHING_FOUND.message) {
                AccountVerificationException.userNotFound(it.message)
            } else {
                AccountVerificationException.unknown(it.message)
            }
        }

        return extractSimpleResult(
            call = call,
            endpointType = EndpointType.PUBLIC,
            onError = onError
        )
    }


    fun extractAccountVerificationResponse(
        call: Call<ApiResponse<AccountVerificationResponse>>
    ): Result<AccountVerificationResponse> {
        val onError: ((ApiResponse<AccountVerificationResponse>) -> Throwable) = {
            if(it.hasErrors) {
                AccountVerificationException.multiple(it.errorList)
            } else {
                AccountVerificationException.unknown(it.message)
            }
        }

        return extractAdvancedResult(
            call = call,
            endpointType = EndpointType.PUBLIC,
            onError = onError
        )
    }


    fun extractPasswordResetEmailSendingResponse(
        call: Call<PasswordResetEmailSendingResponse>
    ): Result<PasswordResetEmailSendingResponse> {
        val onError: ((ApiResponse<PasswordResetEmailSendingResponse>) -> Throwable) = {
            if(it.message == GeneralError.USER_NOT_FOUND.message) {
                PasswordRecoveryException.userNotFound(it.message)
            } else {
                PasswordRecoveryException.unknown(it.message)
            }
        }

        return extractSimpleResult(
            call = call,
            endpointType = EndpointType.PUBLIC,
            onError = onError
        )
    }


    fun extractPasswordResetResponse(
        call: Call<PasswordResetResponse>
    ): Result<PasswordResetResponse> {
        val onError: ((ApiResponse<PasswordResetResponse>) -> Throwable) = {
            if(it.hasErrors) {
                PasswordRecoveryException.multiple(it.errorList)
            } else {
                if(it.message == GeneralError.USER_NOT_FOUND.message) {
                    PasswordRecoveryException.userNotFound(it.message)
                } else {
                    PasswordRecoveryException.unknown(it.message)
                }
            }
        }

        return extractSimpleResult(
            call = call,
            endpointType = EndpointType.PUBLIC,
            onError = onError
        )
    }


    fun extractSignInResponse(call: Call<ResponseBody>): Result<SignInResponse> {
        val onError: ((ApiResponse<SignInResponse>) -> Throwable) = {
            if(it.hasErrors) {
                LoginException.multiple(it.errorList)
            } else {
                if(it.message == GeneralError.WRONG_PARAMETERS.message) {
                    LoginException.invalidParameters(it.message)
                } else {
                    LoginException.unknown(it.message)
                }
            }
        }

        val onSuccess: ((ResponseBody) -> Result<SignInResponse>) = {
            val jsonObject = gson.fromJson<JsonObject>(it.string())

            if(jsonObject.has(ApiResponse.JSON_FIELD_KEY_DATA)) {
                val dataJsonObject = jsonObject.getAsJsonObject(ApiResponse.JSON_FIELD_KEY_DATA)
                val confirmation = SignInConfirmation.newInstance(dataJsonObject)

                if(confirmation != null) {
                    Result.Success(SignInResponse(loginConfirmation = confirmation))
                } else {
                    Result.Failure(LoginException.confirmationObjectBadJson())
                }
            } else {
                val accountEmailConfirmation = AccountEmailVerification.newInstance(jsonObject)

                if(accountEmailConfirmation != null) {
                    Result.Success(SignInResponse(accountEmailVerification = accountEmailConfirmation))
                } else {
                    Result.Failure(LoginException.verificationObjectBadJson())
                }
            }
        }

        return extractResult(
            call = call,
            endpointType = EndpointType.PUBLIC,
            onError = onError,
            onSuccess = onSuccess
        )
    }


    fun extractSignInConfirmationResponse(call: Call<ResponseBody>): Result<SignInConfirmationResponse> {
        val onError: ((ApiResponse<SignInConfirmationResponse>) -> Throwable) = {
            when(it.message) {
                GeneralError.WRONG_PARAMETERS.message -> LoginException.invalidParameters(it.message)
                GeneralError.LOGIN_SESSION_EXPIRED.message -> LoginException.sessionExpired(it.message)

                else -> LoginException.unknown(it.message)
            }
        }

        val onSuccess: ((ResponseBody) -> Result<SignInConfirmationResponse>) = {
            val jsonObject = gson.fromJson<JsonObject>(it.string())
            val dataJsonObject = jsonObject.getAsJsonObject(ApiResponse.JSON_FIELD_KEY_DATA)
            val oauthCredentials = OAuthCredentials.newInstance(dataJsonObject)

            if(oauthCredentials != null) {
                Result.Success(SignInConfirmationResponse(oauthCredentials = oauthCredentials))
            } else {
                val confirmation = SignInConfirmation.newInstance(dataJsonObject)

                if(confirmation != null) {
                    Result.Success(SignInConfirmationResponse(confirmation = confirmation))
                } else {
                    Result.Failure(LoginException.confirmationObjectBadJson())
                }
            }
        }

        return extractResult(
            call = call,
            endpointType = EndpointType.PUBLIC,
            onError = onError,
            onSuccess = onSuccess
        )
    }


    fun extractNewOAuthCredentialsResponse(call: Call<ApiResponse<OAuthCredentials>>): Result<OAuthCredentials> {
        val onError: ((ApiResponse<OAuthCredentials>) -> Throwable) = {
            if(it.message == GeneralError.INVALID_REFRESH_TOKEN.message) {
                InvalidRefreshTokenException(it.message)
            } else {
                ApiException()
            }
        }

        return extractAdvancedResult(
            call = call,
            endpointType = EndpointType.PUBLIC,
            onError = onError
        )
    }


    fun extractTwitterNewsItemsResponse(call: Call<ApiResponse<List<NewsTwitterItemModel>>>): Result<List<NewsTwitterItemModel>> {
        return extractAdvancedResult(call = call, endpointType = EndpointType.PUBLIC)
    }


    fun extractNewsBlogRssResponse(call: Call<NewsBlogRssModel>): Result<NewsBlogRssModel> {
        val onError: ((ApiResponse<NewsBlogRssModel>) -> Throwable) = {
            NewsBlogException.unknown(it.message)
        }

        return extractSimpleResult(
            call = call,
            endpointType = EndpointType.PUBLIC,
            onError = onError
        )
    }


    fun extractProfileInfoResponse(call: Call<ApiResponse<ProfileInfo>>): Result<ProfileInfo> {
        return extractAdvancedResult(call = call, endpointType = EndpointType.PRIVATE)
    }


    fun extractTradingFeesResponse(call: Call<ApiResponse<TradingFees>>): Result<TradingFees> {
        return extractAdvancedResult(call = call, endpointType = EndpointType.PRIVATE)
    }


    fun extractActiveOrdersResponse(call: Call<ApiResponse<List<Order>>>): Result<List<Order>> {
        return extractAdvancedResult(call = call, endpointType = EndpointType.PRIVATE)
    }


    fun extractActiveOrderResponse(call: Call<ApiResponse<Order>>): Result<Order> {
        return extractAdvancedResult(call = call, endpointType = EndpointType.PRIVATE)
    }


    fun extractAllActiveOrdersCancellationResponse(call: Call<ApiResponse<OrdersCancellationResponse>>): Result<OrdersCancellationResponse> {
        return extractAdvancedResult(call = call, endpointType = EndpointType.PRIVATE)
    }


    fun extractActiveOrdersCancellationResponse(call: Call<ApiResponse<OrdersCancellationResponse>>): Result<OrdersCancellationResponse> {
        return extractAdvancedResult(call = call, endpointType = EndpointType.PRIVATE)
    }


    fun extractActiveOrderCancellationResponse(call: Call<ApiResponse<OrdersCancellationResponse>>): Result<OrdersCancellationResponse> {
        return extractAdvancedResult(call = call, endpointType = EndpointType.PRIVATE)
    }


    fun extractOrderCreationResponse(call: Call<ApiResponse<Order>>): Result<Order> {
        val onError: ((ApiResponse<Order>) -> Throwable) = {
            when {
                (it.message == GeneralError.AMOUNT_TOO_SMALL.message) -> OrderCreationException.amountToSmall(it.message)
                (it.message == GeneralError.MAX_NUM_OF_OPEN_ORDERS.message) -> OrderCreationException.maxNumOfOpenOrders(it.message)

                else -> OrderCreationException.unknown(it.message)
            }
        }

        return extractAdvancedResult(
            call = call,
            endpointType = EndpointType.PRIVATE,
            onError = onError
        )
    }


    fun extractHistoryOrdersResponse(call: Call<ApiResponse<List<Order>>>): Result<List<Order>> {
        return extractAdvancedResult(call = call, endpointType = EndpointType.PRIVATE)
    }


    fun extractHistoryOrderResponse(call: Call<ApiResponse<Order>>): Result<Order> {
        return extractAdvancedResult(call = call, endpointType = EndpointType.PRIVATE)
    }


    fun extractWalletsResponse(call: Call<ApiResponse<List<Wallet>>>): Result<List<Wallet>> {
        return extractAdvancedResult(call = call, endpointType = EndpointType.PRIVATE)
    }


    fun extractWalletResponse(call: Call<ApiResponse<Wallet>>): Result<Wallet> {
        val onError: ((ApiResponse<Wallet>) -> Throwable) = {
            when(it.message) {
                GeneralError.NOTHING_FOUND.message -> WalletException.walletNotFound(it.message)

                else -> WalletException.unknown(it.message)
            }
        }

        return extractAdvancedResult(
            call = call,
            endpointType = EndpointType.PRIVATE,
            onError = onError
        )
    }


    fun extractWalletCreationResponse(call: Call<ApiResponse<Wallet>>): Result<Wallet> {
        val onError: ((ApiResponse<Wallet>) -> Throwable) = {
            when(it.message) {
                GeneralError.CURRENCY_IS_DELISTED.message -> WalletException.delistedCurrency(it.message)
                GeneralError.DEPOSITS_DISABLED.message -> WalletException.disabledDeposits(it.message)
                GeneralError.NOTHING_FOUND.message -> WalletException.walletCreationDelay(it.message)

                else -> WalletException.unknown(it.message)
            }
        }

        return extractAdvancedResult(
            call = call,
            endpointType = EndpointType.PRIVATE,
            onError = onError
        )
    }


    fun extractDepositAddressDataResponse(call: Call<ApiResponse<TransactionAddressData>>): Result<TransactionAddressData> {
        return extractAdvancedResult(call = call, endpointType = EndpointType.PRIVATE)
    }


    fun extractDepositAddressDataCreationResponse(call: Call<ApiResponse<TransactionAddressData>>): Result<TransactionAddressData> {
        return extractAdvancedResult(call = call, endpointType = EndpointType.PRIVATE)
    }


    fun extractDepositsResponse(call: Call<ApiResponse<List<Deposit>>>): Result<List<Deposit>> {
        return extractAdvancedResult(call = call, endpointType = EndpointType.PRIVATE)
    }

    fun extractInboxResponse(call: Call<ApiResponse<List<Inbox>>>): Result<List<Inbox>> {
        return extractAdvancedResult(call = call, endpointType = EndpointType.PRIVATE)
    }


    fun extractDepositResponse(call: Call<ApiResponse<Deposit>>): Result<Deposit> {
        return extractAdvancedResult(call = call, endpointType = EndpointType.PRIVATE)
    }


    fun extractWithdrawalsResponse(call: Call<ApiResponse<List<Withdrawal>>>): Result<List<Withdrawal>> {
        return extractAdvancedResult(call = call, endpointType = EndpointType.PRIVATE)
    }


    fun extractWithdrawalResponse(call: Call<ApiResponse<Withdrawal>>): Result<Withdrawal> {
        return extractAdvancedResult(call = call, endpointType = EndpointType.PRIVATE)
    }


    fun extractWithdrawalCreationResponse(call: Call<ApiResponse<Withdrawal>>): Result<Withdrawal> {
        val onError: ((ApiResponse<Withdrawal>) -> Throwable) = {
            when {
                (it.message == GeneralError.WRONG_ADDRESS.message) -> {
                    WithdrawalCreationException.invalidAddress(it.message)
                }
                (it.message == GeneralError.DESTINATION_TAG_REQUIRED.message) -> {
                    WithdrawalCreationException.destinationTagRequired(it.message)
                }
                (it.message.startsWith(GeneralError.NOT_ENOUGH_COSTS_TO_PAY_FEE.message)) -> {
                    WithdrawalCreationException.notEnoughCostsToPayFee(it.message)
                }

                else -> WithdrawalCreationException.unknown(it.message)
            }
        }

        return extractAdvancedResult(
            call = call,
            endpointType = EndpointType.PRIVATE,
            onError = onError
        )
    }


    fun extractWithdrawalConfirmationEmailSendingResponse(
        call: Call<WithdrawalConfirmationEmailSendingResponse>
    ): Result<WithdrawalConfirmationEmailSendingResponse> {
        val onError: ((ApiResponse<WithdrawalConfirmationEmailSendingResponse>) -> Throwable) = {
            if(it.message == GeneralError.NOTHING_FOUND.message) {
                WithdrawalProcessingException.notFound(it.message)
            } else {
                WithdrawalProcessingException.unknown(it.message)
            }
        }

        return extractSimpleResult(
            call = call,
            endpointType = EndpointType.PRIVATE,
            onError = onError
        )
    }


    fun extractWithdrawalConfirmationResponse(call: Call<WithdrawalConfirmationResponse>): Result<WithdrawalConfirmationResponse> {
        val onError: ((ApiResponse<WithdrawalConfirmationResponse>) -> Throwable) = {
            if(it.hasMessage) {
                WithdrawalProcessingException.processing(it.message)
            } else {
                WithdrawalProcessingException.unknown(it.message)
            }
        }

        return extractSimpleResult(
            call = call,
            endpointType = EndpointType.PRIVATE,
            onError = onError
        )
    }


    fun extractWithdrawalCancellationResponse(call: Call<WithdrawalCancellationResponse>): Result<WithdrawalCancellationResponse> {
        val onError: ((ApiResponse<WithdrawalCancellationResponse>) -> Throwable) = {
            if(it.hasMessage) {
                WithdrawalProcessingException.processing(it.message)
            } else {
                WithdrawalProcessingException.unknown(it.message)
            }
        }

        return extractSimpleResult(
            call = call,
            endpointType = EndpointType.PRIVATE,
            onError = onError
        )
    }


    fun extractReferralCodeProvisionResponse(call: Call<ReferralCodeProvisionResponse>): Result<ReferralCodeProvisionResponse> {
        val onError: ((ApiResponse<ReferralCodeProvisionResponse>) -> Throwable) = {
            if(it.hasMessage) {
                 ReferralException.invalidCode(it.message)
            } else {
                ReferralException.unknown(it.message)
            }
        }

        return extractSimpleResult(
            call = call,
            endpointType = EndpointType.PRIVATE,
            onError = onError
        )
    }


    fun extractNotificationTokenResponse(call: Call<NotificationTokenUpdateResponse>): Result<NotificationTokenUpdateResponse> {
        val onError: ((ApiResponse<NotificationTokenUpdateResponse>) -> Throwable) = {
            if (it.hasMessage) {
                NotificationException.invalidToken(it.message)
            } else {
                NotificationException.unknown(it.message)
            }
        }

        return extractSimpleResult(
            call = call,
            endpointType = EndpointType.PRIVATE,
            onError = onError
        )
    }


    fun extractNotificationStatusResponse(call: Call<NotificationStatusResponse>): Result<NotificationStatusResponse> {
        val onError: ((ApiResponse<NotificationStatusResponse>) -> Throwable) = {
            if (it.hasMessage) {
                NotificationException.invalidToken(it.message)
            } else {
                NotificationException.unknown(it.message)
            }
        }

        return extractSimpleResult(
            call = call,
            endpointType = EndpointType.PRIVATE,
            onError = onError
        )
    }


    fun extractDeleteInboxItemResponse(call: Call<InboxDeleteItemResponse>): Result<InboxDeleteItemResponse> {
        val onError: ((ApiResponse<InboxDeleteItemResponse>) -> Throwable) = {
            if (it.hasMessage) {
                InboxException.invalidToken(it.message)
            } else {
                InboxException.unknown(it.message)
            }
        }

        return extractSimpleResult(
            call = call,
            endpointType = EndpointType.PRIVATE,
            onError = onError
        )
    }


    fun extractSetInboxReadAllResponse(call: Call<InboxSetReadAllResponse>): Result<InboxSetReadAllResponse> {
        val onError: ((ApiResponse<InboxSetReadAllResponse>) -> Throwable) = {
            if (it.hasMessage) {
                InboxException.invalidToken(it.message)
            } else {
                InboxException.unknown(it.message)
            }
        }

        return extractSimpleResult(
            call = call,
            endpointType = EndpointType.PRIVATE,
            onError = onError
        )
    }


    fun extractInboxUnreadCountResponse(call: Call<ApiResponse<InboxGetUnreadCountResponse>>): Result<InboxGetUnreadCountResponse> {
        val onError: ((ApiResponse<InboxGetUnreadCountResponse>) -> Throwable) = {
            if (it.hasMessage) {
                InboxException.invalidToken(it.message)
            } else {
                InboxException.unknown(it.message)
            }
        }

        return extractAdvancedResult(
            call = call,
            endpointType = EndpointType.PRIVATE,
            onError = onError
        )
    }


    fun extractAlertPriceResponse(call: Call<ApiResponse<List<AlertPrice>>>): Result<List<AlertPrice>> {
        return extractAdvancedResult(call = call, endpointType = EndpointType.PRIVATE)
    }


    fun extractDeleteAlertPriceItemResponse(call: Call<AlertPriceDeleteResponse>): Result<AlertPriceDeleteResponse> {
        val onError: ((ApiResponse<AlertPriceDeleteResponse>) -> Throwable) = {
            if (it.hasMessage) {
                AlertPriceException.invalidToken(it.message)
            } else {
                AlertPriceException.unknown(it.message)
            }
        }

        return extractSimpleResult(
            call = call,
            endpointType = EndpointType.PRIVATE,
            onError = onError
        )
    }


    fun extractCreateAlertPriceResponse(call: Call<ApiResponse<AlertPrice>>): Result<AlertPrice> {
        val onError: ((ApiResponse<AlertPrice>) -> Throwable) = {
            if(it.hasErrors) {
                when(it.message) {
                    GeneralError.ALERT_PRICE_ALREADY_MORE.message -> AlertPriceException.alreadyMore(it.errorsMap)
                    GeneralError.ALERT_PRICE_ALREADY_LESS.message -> AlertPriceException.alreadyLess(it.errorsMap)
                    GeneralError.ALERT_PRICE_EXIST.message -> AlertPriceException.alreadyExist()
                    else -> AlertPriceException.unknown(it.message)
                }
            } else {
                AlertPriceException.unknown(it.message)
            }
        }

        return extractAdvancedResult(
            call = call,
            endpointType = EndpointType.PRIVATE,
            onError = onError
        )
    }


}