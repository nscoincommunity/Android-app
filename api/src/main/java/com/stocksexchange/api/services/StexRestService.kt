package com.stocksexchange.api.services

import com.stocksexchange.api.model.EndpointPaths
import com.stocksexchange.api.model.rest.*
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.http.*

/**
 * A service of the STEX REST API that describes and
 * documents every endpoint in detail.
 */
interface StexRestService {


    /**
     *
     * PUBLIC ENDPOINTS
     *
     */


    /**
     * Pings the STEX server.
     */
    @GET
    fun ping(@Url url: String): Call<ApiResponse<PingResponse>>


    /**
     * Retrieves all available currencies.
     */
    @GET(EndpointPaths.Public.GET_CURRENCIES)
    fun getCurrencies(): Call<ApiResponse<List<Currency>>>


    /**
     * Retrieves a particular currency specified by its ID.
     *
     * @param currencyId The ID of the currency to fetch
     */
    @GET(EndpointPaths.Public.GET_CURRENCY)
    fun getCurrency(
        @Path(EndpointPaths.Params.Path.CURRENCY_ID) currencyId: Int
    ): Call<ApiResponse<Currency>>


    /**
     * Retrieves all available markets.
     */
    @GET(EndpointPaths.Public.GET_MARKETS)
    fun getMarkets(): Call<ApiResponse<List<Market>>>


    /**
     * Retrieves currency pairs.
     *
     * @param code The code of the currency pairs to retrieve.
     * Can be "ALL" to retrieve all pairs or one of the values
     * returned by the [getMarkets] method.
     */
    @GET(EndpointPaths.Public.GET_CURRENCY_PAIRS)
    fun getCurrencyPairs(
        @Path(EndpointPaths.Params.Path.CODE) code: String
    ) : Call<ApiResponse<List<CurrencyPair>>>


    /**
     * Retrieves a particular currency pair specified by its ID.
     *
     * @param currencyPairId The ID of the currency pair to fetch
     */
    @GET(EndpointPaths.Public.GET_CURRENCY_PAIR)
    fun getCurrencyPair(
        @Path(EndpointPaths.Params.Path.CURRENCY_PAIR_ID) currencyPairId: Int
    ): Call<ApiResponse<CurrencyPair>>


    /**
     * Retrieves groups of currency pairs.
     */
    @GET(EndpointPaths.Public.GET_CURRENCY_PAIR_GROUPS)
    fun getCurrencyPairGroups(): Call<ApiResponse<List<CurrencyPairGroup>>>


    /**
     * Retrieves ticker of the coins at the moment of making this request.
     */
    @GET(EndpointPaths.Public.GET_TICKER_ITEMS)
    fun getTickerItems(): Call<ApiResponse<List<TickerItem>>>


    /**
     * Retrieves a particular ticker item specified by the currency pair ID.
     *
     * @param currencyPairId The ID of the currency pair of a ticker item
     * to fetch
     */
    @GET(EndpointPaths.Public.GET_TICKER_ITEM)
    fun getTickerItem(
        @Path(EndpointPaths.Params.Path.CURRENCY_PAIR_ID) currencyPairId: Int
    ): Call<ApiResponse<TickerItem>>


    /**
     * Retrieves history trades of the particular currency pair.
     *
     * @param currencyPairId The ID of the currency pair to retrieve the trades for.
     * @param count The count of the trades to retrieve. Default value is 100.
     * @param fromTimestamp The timestamp in milliseconds that specifies that returned
     * trades should have been created after or at this time
     * @param tillTimestamp The timestamp in milliseconds that specifies that returned
     * trades should have been created before or at this time
     * @param sortOrder The order of the sort. The fetched trades are sorted
     * based on the timestamp. Two possible values to use for this parameter:
     * ASC (ascending) or DESC (descending) order. Default value is DESC.
     */
    @GET(EndpointPaths.Public.GET_HISTORY_TRADES)
    fun getHistoryTrades(
        @Path(EndpointPaths.Params.Path.CURRENCY_PAIR_ID) currencyPairId: Int,
        @Query(EndpointPaths.Params.Query.LIMIT) count: Int,
        @Query(EndpointPaths.Params.Query.FROM) fromTimestamp: Long,
        @Query(EndpointPaths.Params.Query.TILL) tillTimestamp: Long,
        @Query(EndpointPaths.Params.Query.SORT) sortOrder: String
    ) : Call<ApiResponse<List<Trade>>>


    /**
     * Retrieves orderbook of the particular currency pair.
     *
     * @param currencyPairId The ID of the currency pair to retrieve
     * the orderbook for.
     * @param bidOrdersCount The count of the bid orders to retrieve.
     * Default value is 100.
     * @param askOrdersCount The count of the ask orders to retrieve.
     * Default value is 100.
     */
    @GET(EndpointPaths.Public.GET_ORDERBOOK)
    fun getOrderbook(
        @Path(EndpointPaths.Params.Path.CURRENCY_PAIR_ID) currencyPairId: Int,
        @Query(EndpointPaths.Params.Query.LIMIT_BIDS) bidOrdersCount: Int,
        @Query(EndpointPaths.Params.Query.LIMIT_ASKS) askOrdersCount: Int
    ) : Call<ApiResponse<Orderbook>>


    /**
     * Retrieves candle sticks of the particular currency pair.
     *
     * @param currencyPairId The ID of the currency pair to retrieve
     * the candle sticks for
     * @param intervalName The interval of the candle sticks to fetch.
     * Can be one of the following constants:
     * - 1 (1 minute)
     * - 5 (5 minutes)
     * - 30 (30 minutes)
     * - 60 (60 minutes)
     * - 240 (240 minutes or 4 hours)
     * - 720 (720 minutes or 12 hours)
     * - 1D (1 day)
     * Default value is 1D.
     * @param startTimestamp The start timestamp (in milliseconds) of the
     * period we want to retrieve the candle sticks for
     * @param endTimestamp The end timestamp (in milliseconds) of the
     * period we want to retrieve the candle sticks for
     * @param count The count of the candle sticks to retrieve.
     * Default value is 100.
     */
    @GET(EndpointPaths.Public.GET_CANDLE_STICKS)
    fun getCandleSticks(
        @Path(EndpointPaths.Params.Path.CURRENCY_PAIR_ID) currencyPairId: Int,
        @Path(EndpointPaths.Params.Path.INTERVAL_NAME) intervalName: String,
        @Query(EndpointPaths.Params.Query.START_TIME) startTimestamp: Long,
        @Query(EndpointPaths.Params.Query.END_TIME) endTimestamp: Long,
        @Query(EndpointPaths.Params.Query.LIMIT) count: Int
    ) : Call<ApiResponse<List<CandleStick>>>


    /**
     * Retrieves the latest STEX news from the official account on Twitter.
     */
    @GET(EndpointPaths.Public.GET_TWITTER_NEWS)
    fun getTwitterNewsItems(): Call<ApiResponse<List<NewsTwitterItemModel>>>


    /**
     * Registers a new user.
     *
     * @param email The email of the user who wants to register
     * @param password The password of the user who wants to register. Must be
     * between 6 and 32 characters.
     */
    @FormUrlEncoded
    @POST(EndpointPaths.Public.SIGN_UP)
    fun signUp(
        @Field(EndpointPaths.Params.Field.EMAIL) email: String,
        @Field(EndpointPaths.Params.Field.PASSWORD) password: String
    ) : Call<SignUpResponse>


    /**
     * Sends an account verification email.
     *
     * @param email The email address of the user to send an
     * account verification email to
     */
    @FormUrlEncoded
    @POST(EndpointPaths.Public.SEND_ACCOUNT_VERIFICATION_EMAIL)
    fun sendAccountVerificationEmail(
        @Field(EndpointPaths.Params.Field.EMAIL) email: String
    ) : Call<AccountVerificationEmailSendingResponse>


    /**
     * Verifies an account.
     *
     * @param verificationToken The token used for verifying the account
     */
    @POST(EndpointPaths.Public.VERIFY_ACCOUNT)
    fun verifyAccount(
        @Path(EndpointPaths.Params.Path.VERIFICATION_TOKEN) verificationToken: String
    ) : Call<ApiResponse<AccountVerificationResponse>>


    /**
     * Sends an email with a link to reset a user's password.
     *
     * @param email The email of the user who wants to reset the password
     */
    @FormUrlEncoded
    @POST(EndpointPaths.Public.SEND_PASSWORD_RESET_EMAIL)
    fun sendPasswordResetEmail(
        @Field(EndpointPaths.Params.Field.EMAIL) email: String
    ) : Call<PasswordResetEmailSendingResponse>


    /**
     * Resets a user's password by replacing it with a new one.
     *
     * @param passwordResetToken The token denoting the password reset process
     * @param email The email of the user who wants to reset a password
     * @param newPassword The new password to set
     */
    @FormUrlEncoded
    @POST(EndpointPaths.Public.RESET_PASSWORD)
    fun resetPassword(
        @Field(EndpointPaths.Params.Field.TOKEN) passwordResetToken: String,
        @Field(EndpointPaths.Params.Field.EMAIL) email: String,
        @Field(EndpointPaths.Params.Field.PASSWORD) newPassword: String
    ): Call<PasswordResetResponse>


    /**
     * Performs a first step of the sign in process: sending the user's credentials.
     * If the credentials are valid, the next step is to send a security code
     * retrieved in one of the possible ways (see [SignInConfirmationType] for
     * possible ways to retrieve a security code).
     *
     * @param email The email of the user trying to sign in
     * @param password The password of the user trying to sign in
     * @param key The key the purpose of which is to denote the sign in process.
     * As mentioned above that the sign in is a two-step process, this key parameter
     * will be used later when performing a second step using [confirmSignIn] method.
     * The key itself should be a random nonce encrypted using SHA-1 hash function.
     */
    @FormUrlEncoded
    @POST(EndpointPaths.Public.SIGN_IN)
    fun signIn(
        @Field(EndpointPaths.Params.Field.EMAIL) email: String,
        @Field(EndpointPaths.Params.Field.PASSWORD) password: String,
        @Field(EndpointPaths.Params.Field.KEY) key: String
    ) : Call<ResponseBody>


    /**
     * Performs a second step of the sign in process: providing a security code
     * received by one of the ways specified by in [SignInConfirmationType] class.
     * If everything is performed successfully, the response of this method
     * should return OAuth related tokens used for authentication in future.
     *
     * @param code The security code received by one of the ways specified in
     * [SignInConfirmationType] class
     * @param key The key used in the first step of the sign in process.
     * Unlike in the first step, the key provided for this method should
     * be in plaintext that was used and transformed by SHA-1 hash function
     * in the first step
     */
    @FormUrlEncoded
    @POST(EndpointPaths.Public.CONFIRM_SIGNIN)
    fun confirmSignIn(
        @Field(EndpointPaths.Params.Field.CODE) code: String,
        @Field(EndpointPaths.Params.Field.KEY) key: String
    ) : Call<ResponseBody>


    /**
     * Retrieves new OAuth credentials for the refresh token.
     *
     * @param refreshToken The refresh token to get new OAuth credentials for
     */
    @POST(EndpointPaths.Public.GET_NEW_OAUTH_CREDENTIALS)
    fun getNewOAuthCredentials(
        @Path(EndpointPaths.Params.Path.REFRESH_TOKEN) refreshToken: String
    ) : Call<ApiResponse<OAuthCredentials>>


    /**
     *
     * PRIVATE ENDPOINTS
     *
     */


    /**
     * Retrieves profile information of the user.
     *
     * @param authorizationHeader The authorization header
     */
    @GET(EndpointPaths.Private.GET_PROFILE_INFO)
    fun getProfileInfo(
        @Header(EndpointPaths.Headers.AUTHORIZATION) authorizationHeader: String
    ) : Call<ApiResponse<ProfileInfo>>


    /**
     * Retrieves fees for trading a particular currency pair.
     *
     * @param authorizationHeader The authorization header
     * @param currencyPairId The ID of the currency pair
     */
    @GET(EndpointPaths.Private.GET_TRADING_FEES)
    fun getTradingFees(
        @Header(EndpointPaths.Headers.AUTHORIZATION) authorizationHeader: String,
        @Path(EndpointPaths.Params.Path.CURRENCY_PAIR_ID) currencyPairId: Int
    ): Call<ApiResponse<TradingFees>>


    /**
     * Retrieves active orders.
     *
     * @param authorizationHeader The authorization header
     * @param currencyPairId The ID of the currency pair to return the active
     * orders for. This parameter is optional and can be empty string.
     */
    @GET(EndpointPaths.Private.GET_ACTIVE_ORDERS)
    fun getActiveOrders(
        @Header(EndpointPaths.Headers.AUTHORIZATION) authorizationHeader: String,
        @Path(EndpointPaths.Params.Path.CURRENCY_PAIR_ID) currencyPairId: String,
        @Query(EndpointPaths.Params.Query.LIMIT) limit: Int,
        @Query(EndpointPaths.Params.Query.OFFSET) offset: Int
    ) : Call<ApiResponse<List<Order>>>


    /**
     * Retrieves an active order by its ID.
     *
     * @param authorizationHeader The authorization header
     * @param orderId The ID of the order
     */
    @GET(EndpointPaths.Private.GET_ACTIVE_ORDER)
    fun getActiveOrder(
        @Header(EndpointPaths.Headers.AUTHORIZATION) authorizationHeader: String,
        @Path(EndpointPaths.Params.Path.ORDER_ID) orderId: Long
    ) : Call<ApiResponse<Order>>


    /**
     * Cancels all active orders.
     *
     * @param authorizationHeader The authorization header
     */
    @DELETE(EndpointPaths.Private.CANCEL_ALL_ACTIVE_ORDERS)
    fun cancelAllActiveOrders(
        @Header(EndpointPaths.Headers.AUTHORIZATION) authorizationHeader: String
    ) : Call<ApiResponse<OrdersCancellationResponse>>


    /**
     * Cancels active orders of the particular currency pair.
     *
     * @param authorizationHeader The authorization header
     * @param currencyPairId The ID of the currency pair to cancel
     * active orders with
     */
    @DELETE(EndpointPaths.Private.CANCEL_ACTIVE_ORDERS)
    fun cancelActiveOrders(
        @Header(EndpointPaths.Headers.AUTHORIZATION) authorizationHeader: String,
        @Path(EndpointPaths.Params.Path.CURRENCY_PAIR_ID) currencyPairId: Int
    ) : Call<ApiResponse<OrdersCancellationResponse>>


    /**
     * Cancels an active order by its ID.
     *
     * @param authorizationHeader The authorization header
     * @param orderId The ID of the order to cancel
     */
    @DELETE(EndpointPaths.Private.CANCEL_ACTIVE_ORDER)
    fun cancelActiveOrder(
        @Header(EndpointPaths.Headers.AUTHORIZATION) authorizationHeader: String,
        @Path(EndpointPaths.Params.Path.ORDER_ID) orderId: Long
    ) : Call<ApiResponse<OrdersCancellationResponse>>


    /**
     * Creates an order.
     *
     * @param authorizationHeader The authorization header
     * @param currencyPairId The ID of the currency pair to create an
     * order for
     * @param type The type of the order. Either "BUY", "SELL",
     * "STOP_LIMIT_BUY", or "STOP_LIMIT_SELL".
     * @param amount The amount to buy or sell
     * @param price The price to buy or sell at
     * @param stopPrice The stop price of the stop-limit orders
     */
    @FormUrlEncoded
    @POST(EndpointPaths.Private.CREATE_ORDER)
    fun createOrder(
        @Header(EndpointPaths.Headers.AUTHORIZATION) authorizationHeader: String,
        @Path(EndpointPaths.Params.Path.CURRENCY_PAIR_ID) currencyPairId: Int,
        @Field(EndpointPaths.Params.Field.TYPE) type: String,
        @Field(EndpointPaths.Params.Field.AMOUNT) amount: String,
        @Field(EndpointPaths.Params.Field.PRICE) price: String,
        @Field(EndpointPaths.Params.Field.TRIGGER_PRICE) stopPrice: String
    ) : Call<ApiResponse<Order>>


    /**
     * Retrieves history orders.
     *
     * @param authorizationHeader The authorization header
     * @param status The status of the history orders. Can be one of the following:
     * [OrderStatus.ALL], [OrderStatus.FINISHED], [OrderStatus.CANCELLED],
     * [OrderStatus.PARTIAL].
     * @param currencyPairId The ID of the currency pair. If provided, would fetch
     * only orders with that ID. This parameter is optional and can be null.
     * @param limit The count of the orders to fetch. Default is 100.
     * @param offset The offset of the orders data set to fetch items from
     */
    @GET(EndpointPaths.Private.GET_HISTORY_ORDERS)
    fun getHistoryOrders(
        @Header(EndpointPaths.Headers.AUTHORIZATION) authorizationHeader: String,
        @Query(EndpointPaths.Params.Query.ORDER_STATUS) status: String,
        @Query(EndpointPaths.Params.Query.CURRENCY_PAIR_ID) currencyPairId: Int?,
        @Query(EndpointPaths.Params.Query.LIMIT) limit: Int,
        @Query(EndpointPaths.Params.Query.OFFSET) offset: Int
    ) : Call<ApiResponse<List<Order>>>


    /**
     * Retrieves a history order by its ID.
     *
     * @param authorizationHeader The authorization header
     * @param orderId The ID of the order to retrieve
     */
    @GET(EndpointPaths.Private.GET_HISTORY_ORDER)
    fun getHistoryOrder(
        @Header(EndpointPaths.Headers.AUTHORIZATION) authorizationHeader: String,
        @Path(EndpointPaths.Params.Path.ORDER_ID) orderId: Long
    ) : Call<ApiResponse<Order>>


    /**
     * Retrieves user's wallets.
     *
     * @param authorizationHeader The authorization header
     * @param sortOrder The order of the sort. Two possible values to use
     * for this parameter: ASC (ascending) or DESC (descending) order.
     * Default value is DESC.
     * @param sortColumn The column by which to sort the data set.
     * The value can be one of the following: "BALANCE", "FROZEN",
     * "BONUS", or "TOTAL". Default is "BALANCE".
     */
    @GET(EndpointPaths.Private.GET_WALLETS)
    fun getWallets(
        @Header(EndpointPaths.Headers.AUTHORIZATION) authorizationHeader: String,
        @Query(EndpointPaths.Params.Query.SORT) sortOrder: String,
        @Query(EndpointPaths.Params.Query.SORT_BY) sortColumn: String
    ) : Call<ApiResponse<List<Wallet>>>


    /**
     * Retrieves a single wallet by its ID.
     *
     * @param authorizationHeader The authorization header
     * @param walletId The ID of the wallet to fetch
     */
    @GET(EndpointPaths.Private.GET_WALLET)
    fun getWallet(
        @Header(EndpointPaths.Headers.AUTHORIZATION) authorizationHeader: String,
        @Path(EndpointPaths.Params.Path.WALLET_ID) walletId: Long
    ) : Call<ApiResponse<Wallet>>


    /**
     * Creates a wallet for the particular currency.
     *
     * @param authorizationHeader The authorization header
     * @param currencyId The ID of the currency to create wallet for
     * @param protocolId The ID of the protocol for multi-currency wallets
     */
    @POST(EndpointPaths.Private.CREATE_WALLET)
    fun createWallet(
        @Header(EndpointPaths.Headers.AUTHORIZATION) authorizationHeader: String,
        @Path(EndpointPaths.Params.Path.CURRENCY_ID) currencyId: Int,
        @Query(EndpointPaths.Params.Query.PROTOCOL_ID) protocolId: Int
    ) : Call<ApiResponse<Wallet>>


    /**
     * Retrieves deposit address data of the wallet.
     *
     * @param authorizationHeader The authorization header
     * @param walletId The ID of the wallet to return the deposit address data of
     */
    @GET(EndpointPaths.Private.GET_DEPOSIT_ADDRESS_DATA)
    fun getDepositAddressData(
        @Header(EndpointPaths.Headers.AUTHORIZATION) authorizationHeader: String,
        @Path(EndpointPaths.Params.Path.WALLET_ID) walletId: Long
    ) : Call<ApiResponse<TransactionAddressData>>


    /**
     * Creates deposit address data of the particular wallet.
     *
     * @param authorizationHeader The authorization header
     * @param walletId The ID of the wallet to create deposit address data of
     * @param protocolId The ID of the protocol of multi-currency wallets
     */
    @POST(EndpointPaths.Private.CREATE_DEPOSIT_ADDRESS_DATA)
    fun createDepositAddressData(
        @Header(EndpointPaths.Headers.AUTHORIZATION) authorizationHeader: String,
        @Path(EndpointPaths.Params.Path.WALLET_ID) walletId: Long,
        @Query(EndpointPaths.Params.Query.PROTOCOL_ID) protocolId: Int
    ) : Call<ApiResponse<TransactionAddressData>>


    /**
     * Retrieves deposits of the user.
     *
     * @param authorizationHeader The authorization header
     * @param sortOrder The order of the sort. The fetched deposits are sorted
     * based on the timestamp. Two possible values to use for this parameter:
     * ASC (ascending) or DESC (descending) order. Default value is DESC.
     * @param limit The count of the deposits to fetch. Default is 100.
     * @param offset The offset of the deposits data set to fetch items from
     */
    @GET(EndpointPaths.Private.GET_DEPOSITS)
    fun getDeposits(
        @Header(EndpointPaths.Headers.AUTHORIZATION) authorizationHeader: String,
        @Query(EndpointPaths.Params.Query.SORT) sortOrder: String,
        @Query(EndpointPaths.Params.Query.LIMIT) limit: Int,
        @Query(EndpointPaths.Params.Query.OFFSET) offset: Int
    ) : Call<ApiResponse<List<Deposit>>>


    /**
     * Retrieves a deposit by its ID.
     *
     * @param authorizationHeader The authorization header
     * @param depositId The ID of the deposit to fetch
     */
    @GET(EndpointPaths.Private.GET_DEPOSIT)
    fun getDeposit(
        @Header(EndpointPaths.Headers.AUTHORIZATION) authorizationHeader: String,
        @Path(EndpointPaths.Params.Path.DEPOSIT_ID) depositId: Long
    ) : Call<ApiResponse<Deposit>>


    /**
     * Retrieves withdrawals of the user.
     *
     * @param authorizationHeader The authorization header
     * @param sortOrder The order of the sort. The fetched withdrawals are sorted
     * based on the timestamp. Two possible values to use for this parameter:
     * ASC (ascending) or DESC (descending) order. Default value is DESC.
     * @param limit The count of the withdrawals to fetch. Default is 100.
     * @param offset The offset of the withdrawals data set to fetch items from
     */
    @GET(EndpointPaths.Private.GET_WITHDRAWALS)
    fun getWithdrawals(
        @Header(EndpointPaths.Headers.AUTHORIZATION) authorizationHeader: String,
        @Query(EndpointPaths.Params.Query.SORT) sortOrder: String,
        @Query(EndpointPaths.Params.Query.LIMIT) limit: Int,
        @Query(EndpointPaths.Params.Query.OFFSET) offset: Int
    ) : Call<ApiResponse<List<Withdrawal>>>


    /**
     * Retrieves a withdrawal by its ID.
     *
     * @param authorizationHeader The authorization header
     * @param withdrawalId The ID of the withdrawal to fetch
     */
    @GET(EndpointPaths.Private.GET_WITHDRAWAL)
    fun getWithdrawal(
        @Header(EndpointPaths.Headers.AUTHORIZATION) authorizationHeader: String,
        @Path(EndpointPaths.Params.Path.WITHDRAWAL_ID) withdrawalId: Long
    ) : Call<ApiResponse<Withdrawal>>


    /**
     * Creates a withdrawal.
     *
     * @param authorizationHeader The authorization header
     * @param currencyId The ID of the currency to create a withdrawal of
     * @param protocolId The ID of the protocol for multi-currency wallets
     * @param amount The amount to withdraw
     * @param address The address of the wallet to withdraw funds into
     * @param additionalAddress The additional address for a withdrawal.
     * For example, if withdrawing funds requires additional address
     * (like payment ID or destination tag), it should be passed as
     * a value for this parameter.
     */
    @FormUrlEncoded
    @POST(EndpointPaths.Private.CREATE_WITHDRAWAL)
    fun createWithdrawal(
        @Header(EndpointPaths.Headers.AUTHORIZATION) authorizationHeader: String,
        @Field(EndpointPaths.Params.Field.CURRENCY_ID) currencyId: Int,
        @Field(EndpointPaths.Params.Field.PROTOCOL_ID) protocolId: Int,
        @Field(EndpointPaths.Params.Field.AMOUNT) amount: String,
        @Field(EndpointPaths.Params.Field.ADDRESS) address: String,
        @Field(EndpointPaths.Params.Field.ADDITIONAL_ADDRESS) additionalAddress: String
    ) : Call<ApiResponse<Withdrawal>>


    /**
     * Sends a withdrawal confirmation email.
     *
     * @param withdrawalId The ID of the withdrawal
     */
    @GET(EndpointPaths.Private.SEND_WITHDRAWAL_CONFIRMATION_EMAIL)
    fun sendWithdrawalConfirmationEmail(
        @Header(EndpointPaths.Headers.AUTHORIZATION) authorizationHeader: String,
        @Path(EndpointPaths.Params.Path.WITHDRAWAL_ID) withdrawalId: Long
    ) : Call<WithdrawalConfirmationEmailSendingResponse>


    /**
     * Confirms a withdrawal.
     *
     * @param authorizationHeader The authorization header
     * @param withdrawalId The ID of the withdrawal to confirm
     * @param confirmationToken The token used for confirming a withdrawal
     */
    @POST(EndpointPaths.Private.CONFIRM_WITHDRAWAL)
    fun confirmWithdrawal(
        @Header(EndpointPaths.Headers.AUTHORIZATION) authorizationHeader: String,
        @Path(EndpointPaths.Params.Path.WITHDRAWAL_ID) withdrawalId: Long,
        @Path(EndpointPaths.Params.Path.CONFIRMATION_TOKEN) confirmationToken: String
    ) : Call<WithdrawalConfirmationResponse>


    /**
     * Cancels a withdrawal.
     *
     * @param authorizationHeader The authorization header
     * @param withdrawalId The ID of the withdrawal to cancel
     */
    @DELETE(EndpointPaths.Private.CANCEL_WITHDRAWAL)
    fun cancelWithdrawal(
        @Header(EndpointPaths.Headers.AUTHORIZATION) authorizationHeader: String,
        @Path(EndpointPaths.Params.Path.WITHDRAWAL_ID) withdrawalId: Long
    ) : Call<WithdrawalCancellationResponse>


    /**
     * Provides a referral code of the user that has invited
     * the current user to join the exchange.
     *
     * @param authorizationHeader The authorization header
     * @param code The referral code
     */
    @POST(EndpointPaths.Private.PROVIDE_REFERRAL_CODE)
    fun provideReferralCode(
        @Header(EndpointPaths.Headers.AUTHORIZATION) authorizationHeader: String,
        @Path(EndpointPaths.Params.Path.CODE) code: String
    ) : Call<ReferralCodeProvisionResponse>


    /**
     * Update firebase notification token
     * for get push notification from api.
     *
     * @param authorizationHeader The authorization header
     * @param token Firebase notification token
     */
    @POST(EndpointPaths.Private.UPDATE_NOTIFICATION_TOKEN)
    fun updateNotificationToken(
        @Header(EndpointPaths.Headers.AUTHORIZATION) authorizationHeader: String,
        @Path(EndpointPaths.Params.Path.NOTIFICATION_TOKEN) token: String
    ) : Call<NotificationTokenUpdateResponse>


    /**
     * Sets a status of notifications.
     *
     * @param authorizationHeader The authorization header
     * @param status status for notification "enable" or "disable"
     */
    @POST(EndpointPaths.Private.SET_NOTIFICATION_STATUS)
    fun setNotificationStatus(
        @Header(EndpointPaths.Headers.AUTHORIZATION) authorizationHeader: String,
        @Path(EndpointPaths.Params.Path.STATUS) status: String
    ) : Call<NotificationStatusResponse>


    /**
     * Retrieves user's Inbox items.
     *
     * @param authorizationHeader The authorization header
     * @param limit The count of the inbox to fetch. Default is 100.
     * @param offset The offset of the inbox data set to fetch items from
     */
    @GET(EndpointPaths.Private.GET_INBOX)
    fun getInbox(
        @Header(EndpointPaths.Headers.AUTHORIZATION) authorizationHeader: String,
        @Query(EndpointPaths.Params.Query.LIMIT) limit: Int,
        @Query(EndpointPaths.Params.Query.OFFSET) offset: Int
    ) : Call<ApiResponse<List<Inbox>>>


    /**
     * Deletes an Inbox item specified by ID.
     *
     * @param authorizationHeader The authorization header
     * @param id The id of item
     */
    @DELETE(EndpointPaths.Private.DELETE_INBOX_ITEM)
    fun deleteInboxItem(
        @Header(EndpointPaths.Headers.AUTHORIZATION) authorizationHeader: String,
        @Path(EndpointPaths.Params.Path.ID) id: String
    ) : Call<InboxDeleteItemResponse>


    /**
     * Marks all Inbox as read.
     *
     * @param authorizationHeader The authorization header
     */
    @POST(EndpointPaths.Private.READ_ALL_INBOX)
    fun setInboxReadAll(
        @Header(EndpointPaths.Headers.AUTHORIZATION) authorizationHeader: String
    ) : Call<InboxSetReadAllResponse>


    /**
     * Retrieves a number of messages that a user has not read yet.
     *
     * @param authorizationHeader The authorization header
     */
    @GET(EndpointPaths.Private.GET_INBOX_UNREAD_ITEM_COUNT)
    fun getInboxUnreadCount(
        @Header(EndpointPaths.Headers.AUTHORIZATION) authorizationHeader: String
    ) : Call<ApiResponse<InboxGetUnreadCountResponse>>


    /**
     * Retrieves Alert price list items.
     *
     * @param authorizationHeader The authorization header
     */
    @GET(EndpointPaths.Private.GET_PRICE_ALERT)
    fun getAlertPrice(
        @Header(EndpointPaths.Headers.AUTHORIZATION) authorizationHeader: String
    ) : Call<ApiResponse<List<AlertPrice>>>


    /**
     * Retrieves Alert price list items by pair id.
     *
     * @param authorizationHeader The authorization header
     * @param currencyPairId The id of pair
     */
    @GET(EndpointPaths.Private.GET_PRICE_ALERT)
    fun getAlertPriceByPairId(
        @Header(EndpointPaths.Headers.AUTHORIZATION) authorizationHeader: String,
        @Query(EndpointPaths.Params.Query.CURRENCY_PAIR_ID) currencyPairId: String
    ) : Call<ApiResponse<List<AlertPrice>>>


    /**
     * Deletes an Alert price item specified by ID.
     *
     * @param authorizationHeader The authorization header
     * @param id The id of item
     */
    @DELETE(EndpointPaths.Private.DELETE_ALERT_PRICE)
    fun deleteAlertPriceItem(
        @Header(EndpointPaths.Headers.AUTHORIZATION) authorizationHeader: String,
        @Path(EndpointPaths.Params.Path.ID) id: Int
    ) : Call<AlertPriceDeleteResponse>


    /**
     * Create an Alert price item.
     *
     * @param authorizationHeader The authorization header
     * @param currencyPairId The id of pair
     * @param comparison The value for alert price "LESS" or "GREATER"
     * @param price The price
     */
    @FormUrlEncoded
    @POST(EndpointPaths.Private.CREATE_ALERT_PRICE)
    fun createAlertPriceItem(
        @Header(EndpointPaths.Headers.AUTHORIZATION) authorizationHeader: String,
        @Field(EndpointPaths.Params.Field.CURRENCY_PAIR_ID) currencyPairId: Int,
        @Field(EndpointPaths.Params.Field.COMPARISON) comparison: String,
        @Field(EndpointPaths.Params.Field.PRICE) price: String
    ): Call<ApiResponse<AlertPrice>>


}