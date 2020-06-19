package com.stocksexchange.api

import com.stocksexchange.api.services.StexRestService
import com.stocksexchange.api.model.rest.*
import com.stocksexchange.api.model.rest.parameters.*
import com.stocksexchange.core.model.Result
import com.stocksexchange.api.utils.CredentialsHandler
import com.stocksexchange.api.utils.ResponseExtractor

/**
 * An implementation of the STEX REST API.
 */
class StexRestApi(
    private val stexRestService: StexRestService,
    private val responseExtractor: ResponseExtractor,
    private val credentialsHandler: CredentialsHandler
) {


    private val credentials: OAuthCredentials
        get() = credentialsHandler.getOAuthCredentials()




    fun ping(url: String): Result<PingResponse> {
        return responseExtractor.extractPingResponse(stexRestService.ping(url))
    }


    fun getCurrencies(): Result<List<Currency>> {
        return responseExtractor.extractCurrenciesResponse(stexRestService.getCurrencies())
    }


    fun getCurrency(currencyId: Int): Result<Currency> {
        return responseExtractor.extractCurrencyResponse(stexRestService.getCurrency(currencyId))
    }


    fun getMarkets(): Result<List<Market>> {
        return responseExtractor.extractMarketsResponse(stexRestService.getMarkets())
    }


    fun getAllCurrencyPairs(): Result<List<CurrencyPair>> {
        return responseExtractor.extractCurrencyPairsResponse(stexRestService.getCurrencyPairs(
            code = CurrencyPair.CURRENCY_PAIR_ALL_CODE
        ))
    }


    fun getCurrencyPair(currencyPairId: Int): Result<CurrencyPair> {
        return responseExtractor.extractCurrencyPairResponse(stexRestService.getCurrencyPair(
            currencyPairId = currencyPairId
        ))
    }


    fun getCurrencyPairGroups(): Result<List<CurrencyPairGroup>> {
        return responseExtractor.extractCurrencyPairGroupsResponse(stexRestService.getCurrencyPairGroups())
    }


    fun getTickerItems(): Result<List<TickerItem>> {
        return responseExtractor.extractTickerItemsResponse(stexRestService.getTickerItems())
    }


    fun getTickerItem(currencyPairId: Int): Result<TickerItem> {
        return responseExtractor.extractTickerItemResponse(stexRestService.getTickerItem(
            currencyPairId = currencyPairId
        ))
    }


    fun getHistoryTrades(parameters: TradeHistoryParameters): Result<List<Trade>> {
        return responseExtractor.extractHistoryTradesResponse(stexRestService.getHistoryTrades(
            currencyPairId = parameters.currencyPairId,
            count = parameters.count,
            fromTimestamp = parameters.fromTimestamp,
            tillTimestamp = parameters.tillTimestamp,
            sortOrder = parameters.sortOrder.name
        ))
    }


    fun getOrderbook(parameters: OrderbookParameters): Result<Orderbook> {
        return responseExtractor.extractOrderbookResponse(stexRestService.getOrderbook(
            currencyPairId = parameters.currencyPairId,
            bidOrdersCount = parameters.bidOrdersCount,
            askOrdersCount = parameters.askOrdersCount
        ))
    }


    fun getCandleSticks(parameters: PriceChartDataParameters): Result<List<CandleStick>> {
        return responseExtractor.extractCandleSticksResponse(stexRestService.getCandleSticks(
            currencyPairId = parameters.currencyPairId,
            intervalName = parameters.interval.intervalName,
            startTimestamp = parameters.startTimestamp,
            endTimestamp = parameters.endTimestamp,
            count = parameters.count
        ))
    }


    fun getTwitterNewsItems(): Result<List<NewsTwitterItemModel>> {
        return responseExtractor.extractTwitterNewsItemsResponse(stexRestService.getTwitterNewsItems())
    }


    fun signUp(parameters: SignUpParameters): Result<SignUpResponse> {
        return responseExtractor.extractSignUpResponse(stexRestService.signUp(
            email = parameters.email,
            password = parameters.password
        ))
    }


    fun sendAccountVerificationEmail(email: String): Result<AccountVerificationEmailSendingResponse> {
        return responseExtractor.extractAccountVerificationEmailSendingResponse(stexRestService.sendAccountVerificationEmail(
            email = email
        ))
    }


    fun verifyAccount(verificationToken: String): Result<AccountVerificationResponse> {
        return responseExtractor.extractAccountVerificationResponse(stexRestService.verifyAccount(
            verificationToken = verificationToken
        ))
    }


    fun sendPasswordResetEmail(email: String): Result<PasswordResetEmailSendingResponse> {
        return responseExtractor.extractPasswordResetEmailSendingResponse(stexRestService.sendPasswordResetEmail(
            email = email
        ))
    }


    fun resetPassword(params: PasswordRecoveryParameters): Result<PasswordResetResponse> {
        return responseExtractor.extractPasswordResetResponse(stexRestService.resetPassword(
            passwordResetToken = params.passwordResetToken,
            email = params.email,
            newPassword = params.newPassword
        ))
    }


    fun signIn(parameters: SignInParameters): Result<SignInResponse> {
        return responseExtractor.extractSignInResponse(stexRestService.signIn(
            email = parameters.email,
            password = parameters.password,
            key = parameters.hashedKey
        ))
    }


    fun confirmSignIn(parameters: SignInParameters): Result<SignInConfirmationResponse> {
        return responseExtractor.extractSignInConfirmationResponse(stexRestService.confirmSignIn(
            code = parameters.code,
            key = parameters.key
        ))
    }


    fun getNewOAuthCredentials(refreshToken: String): Result<OAuthCredentials> {
        return responseExtractor.extractNewOAuthCredentialsResponse(stexRestService.getNewOAuthCredentials(
            refreshToken = refreshToken
        ))
    }


    fun getProfileInfo(): Result<ProfileInfo> {
        return responseExtractor.extractProfileInfoResponse(stexRestService.getProfileInfo(
            authorizationHeader = credentials.authorizationHeader
        ))
    }


    fun getTradingFees(parameters: TradingFeesParameters): Result<TradingFees> {
        return responseExtractor.extractTradingFeesResponse(stexRestService.getTradingFees(
            authorizationHeader = credentials.authorizationHeader,
            currencyPairId = parameters.currencyPairId
        ))
    }


    fun getActiveOrders(parameters: OrderParameters): Result<List<Order>> {
        return responseExtractor.extractActiveOrdersResponse(stexRestService.getActiveOrders(
            authorizationHeader = credentials.authorizationHeader,
            currencyPairId = parameters.currencyPairIdStrOrEmptyStr,
            limit = parameters.limit,
            offset = parameters.offset
        ))
    }


    fun getActiveOrder(orderId: Long): Result<Order> {
        return responseExtractor.extractActiveOrderResponse(stexRestService.getActiveOrder(
            authorizationHeader = credentials.authorizationHeader,
            orderId = orderId
        ))
    }


    fun cancelAllActiveOrders(): Result<OrdersCancellationResponse> {
        return responseExtractor.extractAllActiveOrdersCancellationResponse(stexRestService.cancelAllActiveOrders(
            authorizationHeader = credentials.authorizationHeader
        ))
    }


    fun cancelActiveOrders(currencyPairId: Int): Result<OrdersCancellationResponse> {
        return responseExtractor.extractActiveOrdersCancellationResponse(stexRestService.cancelActiveOrders(
            authorizationHeader = credentials.authorizationHeader,
            currencyPairId = currencyPairId
        ))
    }


    fun cancelActiveOrder(order: Order): Result<OrdersCancellationResponse> {
        return responseExtractor.extractActiveOrderCancellationResponse(stexRestService.cancelActiveOrder(
            authorizationHeader = credentials.authorizationHeader,
            orderId = order.id
        ))
    }


    fun createOrder(parameters: OrderCreationParameters): Result<Order> {
        return responseExtractor.extractOrderCreationResponse(stexRestService.createOrder(
            authorizationHeader = credentials.authorizationHeader,
            currencyPairId = parameters.currencyPairId,
            type = parameters.type.name,
            amount = parameters.amount,
            price = parameters.price,
            stopPrice = parameters.stopPrice
        ))
    }


    fun getHistoryOrders(parameters: OrderParameters): Result<List<Order>> {
        return responseExtractor.extractHistoryOrdersResponse(stexRestService.getHistoryOrders(
            authorizationHeader = credentials.authorizationHeader,
            status = parameters.status.name,
            currencyPairId = parameters.currencyPaIrIdOrNull,
            limit = parameters.limit,
            offset = parameters.offset
        ))
    }


    fun getHistoryOrder(orderId: Long): Result<Order> {
        return responseExtractor.extractHistoryOrderResponse(stexRestService.getHistoryOrder(
            authorizationHeader = credentials.authorizationHeader,
            orderId = orderId
        ))
    }


    fun getWallets(parameters: WalletParameters): Result<List<Wallet>> {
        return responseExtractor.extractWalletsResponse(stexRestService.getWallets(
            authorizationHeader = credentials.authorizationHeader,
            sortOrder = parameters.sortOrder.name,
            sortColumn = parameters.sortColumn.type
        ))
    }


    fun getWallet(walletId: Long): Result<Wallet> {
        return responseExtractor.extractWalletResponse(stexRestService.getWallet(
            authorizationHeader = credentials.authorizationHeader,
            walletId = walletId
        ))
    }


    fun createWallet(currencyId: Int, protocolId: Int): Result<Wallet> {
        return responseExtractor.extractWalletCreationResponse(stexRestService.createWallet(
            authorizationHeader = credentials.authorizationHeader,
            currencyId = currencyId,
            protocolId = protocolId
        ))
    }


    fun getDepositAddressData(walletId: Long): Result<TransactionAddressData> {
        return responseExtractor.extractDepositAddressDataResponse(stexRestService.getDepositAddressData(
            authorizationHeader = credentials.authorizationHeader,
            walletId = walletId
        ))
    }


    fun createDepositAddress(walletId: Long, protocolId: Int): Result<TransactionAddressData> {
        return responseExtractor.extractDepositAddressDataCreationResponse(stexRestService.createDepositAddressData(
            authorizationHeader = credentials.authorizationHeader,
            walletId = walletId,
            protocolId = protocolId
        ))
    }


    fun getDeposits(parameters: TransactionParameters): Result<List<Deposit>> {
        return responseExtractor.extractDepositsResponse(stexRestService.getDeposits(
            authorizationHeader = credentials.authorizationHeader,
            sortOrder = parameters.sortOrder.name,
            limit = parameters.limit,
            offset = parameters.offset
        ))
    }


    fun getDeposit(depositId: Long): Result<Deposit> {
        return responseExtractor.extractDepositResponse(stexRestService.getDeposit(
            authorizationHeader = credentials.authorizationHeader,
            depositId = depositId
        ))
    }


    fun getWithdrawals(parameters: TransactionParameters): Result<List<Withdrawal>> {
        return responseExtractor.extractWithdrawalsResponse(stexRestService.getWithdrawals(
            authorizationHeader = credentials.authorizationHeader,
            sortOrder = parameters.sortOrder.name,
            limit = parameters.limit,
            offset = parameters.offset
        ))
    }


    fun getWithdrawal(withdrawalId: Long): Result<Withdrawal> {
        return responseExtractor.extractWithdrawalResponse(stexRestService.getWithdrawal(
            authorizationHeader = credentials.authorizationHeader,
            withdrawalId = withdrawalId
        ))
    }


    fun createWithdrawal(parameters: WithdrawalCreationParameters): Result<Withdrawal> {
        return responseExtractor.extractWithdrawalCreationResponse(stexRestService.createWithdrawal(
            authorizationHeader = credentials.authorizationHeader,
            currencyId = parameters.currencyId,
            protocolId = parameters.protocolId,
            amount = parameters.amount,
            address = parameters.address,
            additionalAddress = parameters.additionalAddress
        ))
    }


    fun sendWithdrawalConfirmationEmail(withdrawalId: Long): Result<WithdrawalConfirmationEmailSendingResponse> {
        return responseExtractor.extractWithdrawalConfirmationEmailSendingResponse(stexRestService.sendWithdrawalConfirmationEmail(
            authorizationHeader = credentials.authorizationHeader,
            withdrawalId = withdrawalId
        ))
    }


    fun confirmWithdrawal(parameters: WithdrawalConfirmationParameters): Result<WithdrawalConfirmationResponse> {
        return responseExtractor.extractWithdrawalConfirmationResponse(stexRestService.confirmWithdrawal(
            authorizationHeader = credentials.authorizationHeader,
            withdrawalId = parameters.withdrawalId,
            confirmationToken = parameters.confirmationToken
        ))
    }


    fun cancelWithdrawal(withdrawalId: Long): Result<WithdrawalCancellationResponse> {
        return responseExtractor.extractWithdrawalCancellationResponse(stexRestService.cancelWithdrawal(
            authorizationHeader = credentials.authorizationHeader,
            withdrawalId = withdrawalId
        ))
    }


    fun provideReferralCode(referralCode: String): Result<ReferralCodeProvisionResponse> {
        return responseExtractor.extractReferralCodeProvisionResponse(stexRestService.provideReferralCode(
            authorizationHeader = credentials.authorizationHeader,
            code = referralCode
        ))
    }

    fun updateNotificationToken(token: String): Result<NotificationTokenUpdateResponse> {
        return responseExtractor.extractNotificationTokenResponse(stexRestService.updateNotificationToken(
            authorizationHeader = credentials.authorizationHeader,
            token = token
        ))
    }


    fun setNotificationStatus(status: NotificationStatus): Result<NotificationStatusResponse> {
        return responseExtractor.extractNotificationStatusResponse(stexRestService.setNotificationStatus(
            authorizationHeader = credentials.authorizationHeader,
            status = status.toString()
        ))
    }


    fun getInbox(parameters: InboxParameters): Result<List<Inbox>> {
        return responseExtractor.extractInboxResponse(stexRestService.getInbox(
            authorizationHeader = credentials.authorizationHeader,
            limit = parameters.limit,
            offset = parameters.offset
        ))
    }


    fun deleteInboxItem(id: String): Result<InboxDeleteItemResponse> {
        return responseExtractor.extractDeleteInboxItemResponse(stexRestService.deleteInboxItem(
            authorizationHeader = credentials.authorizationHeader,
            id = id
        ))
    }


    fun setInboxReadAll(): Result<InboxSetReadAllResponse> {
        return responseExtractor.extractSetInboxReadAllResponse(stexRestService.setInboxReadAll(
            authorizationHeader = credentials.authorizationHeader
        ))
    }


    fun getInboxUnreadCount(): Result<InboxGetUnreadCountResponse> {
        return responseExtractor.extractInboxUnreadCountResponse(stexRestService.getInboxUnreadCount(
            authorizationHeader = credentials.authorizationHeader
        ))
    }


    fun getAlertPrices(): Result<List<AlertPrice>> {
        return responseExtractor.extractAlertPriceResponse(stexRestService.getAlertPrice(
            authorizationHeader = credentials.authorizationHeader
        ))
    }


    fun getAlertPricesByPairId(id: Int): Result<List<AlertPrice>> {
        return responseExtractor.extractAlertPriceResponse(stexRestService.getAlertPriceByPairId(
            authorizationHeader = credentials.authorizationHeader,
            currencyPairId = id.toString()
        ))
    }


    fun deleteAlertPriceItem(id: Int): Result<AlertPriceDeleteResponse> {
        return responseExtractor.extractDeleteAlertPriceItemResponse(stexRestService.deleteAlertPriceItem(
            authorizationHeader = credentials.authorizationHeader,
            id = id
        ))
    }


    fun createAlertPriceItem(params: AlertPriceParameters): Result<AlertPrice> {
        return responseExtractor.extractCreateAlertPriceResponse(stexRestService.createAlertPriceItem(
            authorizationHeader = credentials.authorizationHeader,
            currencyPairId = params.currencyPairId,
            comparison = params.comparison,
            price = params.price
        ))
    }


}