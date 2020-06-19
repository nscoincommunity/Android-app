package com.stocksexchange.android.di

import com.stocksexchange.android.di.utils.get
import com.stocksexchange.android.ui.about.AboutContract
import com.stocksexchange.android.ui.about.AboutPresenter
import com.stocksexchange.android.ui.alertprice.additem.AlertPriceAddContract
import com.stocksexchange.android.ui.alertprice.additem.AlertPriceAddPresenter
import com.stocksexchange.android.ui.alertprice.AlertPriceContract
import com.stocksexchange.android.ui.alertprice.AlertPricePresenter
import com.stocksexchange.android.ui.auth.AuthenticationContract
import com.stocksexchange.android.ui.auth.AuthenticationPresenter
import com.stocksexchange.android.ui.balance.BalanceContainerContract
import com.stocksexchange.android.ui.balance.BalanceContainerPresenter
import com.stocksexchange.android.ui.currencymarketpreview.CurrencyMarketPreviewContract
import com.stocksexchange.android.ui.currencymarketpreview.CurrencyMarketPreviewPresenter
import com.stocksexchange.android.ui.currencymarkets.CurrencyMarketsContainerContract
import com.stocksexchange.android.ui.currencymarkets.CurrencyMarketsContainerPresenter
import com.stocksexchange.android.ui.currencymarkets.fragment.CurrencyMarketsContract
import com.stocksexchange.android.ui.currencymarkets.fragment.CurrencyMarketsPresenter
import com.stocksexchange.android.ui.currencymarkets.search.CurrencyMarketsSearchContract
import com.stocksexchange.android.ui.currencymarkets.search.CurrencyMarketsSearchPresenter
import com.stocksexchange.android.ui.dashboard.DashboardContract
import com.stocksexchange.android.ui.dashboard.DashboardPresenter
import com.stocksexchange.android.ui.profile.ProfileContract
import com.stocksexchange.android.ui.profile.ProfilePresenter
import com.stocksexchange.android.ui.deeplinkhandler.DeepLinkHandlerContract
import com.stocksexchange.android.ui.deeplinkhandler.DeepLinkHandlerPresenter
import com.stocksexchange.android.ui.help.HelpContract
import com.stocksexchange.android.ui.help.HelpPresenter
import com.stocksexchange.android.ui.inbox.InboxContract
import com.stocksexchange.android.ui.inbox.InboxPresenter
import com.stocksexchange.android.ui.language.LanguageContract
import com.stocksexchange.android.ui.language.LanguagePresenter
import com.stocksexchange.android.ui.login.LoginContract
import com.stocksexchange.android.ui.login.LoginPresenter
import com.stocksexchange.android.ui.news.NewsContainerContract
import com.stocksexchange.android.ui.news.NewsContainerPresenter
import com.stocksexchange.android.ui.news.fragments.blog.BlogNewsContract
import com.stocksexchange.android.ui.news.fragments.blog.BlogNewsPresenter
import com.stocksexchange.android.ui.news.fragments.twitter.TwitterNewsContract
import com.stocksexchange.android.ui.news.fragments.twitter.TwitterNewsPresenter
import com.stocksexchange.android.ui.orderbook.OrderbookContract
import com.stocksexchange.android.ui.orderbook.OrderbookPresenter
import com.stocksexchange.android.ui.orders.OrdersContainerContract
import com.stocksexchange.android.ui.orders.OrdersContainerPresenter
import com.stocksexchange.android.ui.orders.fragment.OrdersContract
import com.stocksexchange.android.ui.orders.fragment.OrdersPresenter
import com.stocksexchange.android.ui.orders.search.OrdersSearchContract
import com.stocksexchange.android.ui.orders.search.OrdersSearchPresenter
import com.stocksexchange.android.ui.passwordrecovery.PasswordRecoveryContract
import com.stocksexchange.android.ui.passwordrecovery.PasswordRecoveryPresenter
import com.stocksexchange.android.ui.pinrecovery.PinRecoveryContract
import com.stocksexchange.android.ui.pinrecovery.PinRecoveryPresenter
import com.stocksexchange.android.ui.protocolselection.ProtocolSelectionContract
import com.stocksexchange.android.ui.protocolselection.ProtocolSelectionPresenter
import com.stocksexchange.android.ui.qrcodescanner.QrCodeScannerContract
import com.stocksexchange.android.ui.qrcodescanner.QrCodeScannerPresenter
import com.stocksexchange.android.ui.referral.ReferralContract
import com.stocksexchange.android.ui.referral.ReferralPresenter
import com.stocksexchange.android.ui.registration.RegistrationContract
import com.stocksexchange.android.ui.registration.RegistrationPresenter
import com.stocksexchange.android.ui.settings.SettingsPresenter
import com.stocksexchange.android.ui.settings.SettingsContract
import com.stocksexchange.android.ui.splash.SplashContract
import com.stocksexchange.android.ui.splash.SplashPresenter
import com.stocksexchange.android.ui.trade.TradeContract
import com.stocksexchange.android.ui.trade.TradePresenter
import com.stocksexchange.android.ui.transactioncreation.depositcreation.DepositCreationContract
import com.stocksexchange.android.ui.transactioncreation.depositcreation.DepositCreationPresenter
import com.stocksexchange.android.ui.transactioncreation.withdrawalcreation.WithdrawalCreationContract
import com.stocksexchange.android.ui.transactioncreation.withdrawalcreation.WithdrawalCreationPresenter
import com.stocksexchange.android.ui.transactions.fragment.TransactionsContract
import com.stocksexchange.android.ui.transactions.fragment.TransactionsPresenter
import com.stocksexchange.android.ui.transactions.search.TransactionsSearchContract
import com.stocksexchange.android.ui.transactions.search.TransactionsSearchPresenter
import com.stocksexchange.android.ui.verification.prompt.VerificationPromptContract
import com.stocksexchange.android.ui.verification.prompt.VerificationPromptPresenter
import com.stocksexchange.android.ui.verification.selection.VerificationSelectionContract
import com.stocksexchange.android.ui.verification.selection.VerificationSelectionPresenter
import com.stocksexchange.android.ui.wallets.fragment.WalletsContract
import com.stocksexchange.android.ui.wallets.fragment.WalletsPresenter
import com.stocksexchange.android.ui.wallets.search.WalletsSearchContract
import com.stocksexchange.android.ui.wallets.search.WalletsSearchPresenter
import org.koin.dsl.module

val mvpPresentersModule = module {

    factory { (view: SplashContract.View) ->
        SplashPresenter(
            view = view,
            model = get(),
            initialLanguageProvider = get(),
            timeFormatter = get(),
            numberFormatter = get(),
            shortcutsHandler = get(),
            credentialsHandler = get(),
            intercomHandler = get(),
            preferenceHandler = get(),
            settingsFactory = get(),
            sessionManager = get(),
            remoteServiceUrlFinder = get(),
            restHostInterceptor = get(STEX_HOST_INTERCEPTOR_REST),
            rssHostIntereptor = get(STEX_HOST_INTERCEPTOR_RSS),
            appLockManager = get(),
            firebasePushClient = get()
        )
    }

    factory { (view: AuthenticationContract.View) -> AuthenticationPresenter(view, get(), get(), get(), get()) }
    factory { (view: PinRecoveryContract.View) -> PinRecoveryPresenter(view, get(), get(), get()) }

    factory { (view: DashboardContract.View) -> DashboardPresenter(view, get(), get()) }
    factory { (view: ProfileContract.View) -> ProfilePresenter(view, get()) }
    factory { (view: BalanceContainerContract.View) -> BalanceContainerPresenter(view, get(), get()) }

    factory { (view: VerificationPromptContract.View) -> VerificationPromptPresenter(view, get()) }

    factory { (view: CurrencyMarketsContainerContract.View) -> CurrencyMarketsContainerPresenter(view, get(), get(), get()) }
    factory { (view: CurrencyMarketsSearchContract.View) -> CurrencyMarketsSearchPresenter(view, get()) }
    factory { (view: CurrencyMarketsContract.View) -> CurrencyMarketsPresenter(view, get(), get(), get()) }

    factory { (view: LoginContract.View) -> LoginPresenter(view, get(), get(), get(), get(), get()) }
    factory { (view: RegistrationContract.View) -> RegistrationPresenter(view, get()) }
    factory { (view: PasswordRecoveryContract.View) -> PasswordRecoveryPresenter(view, get()) }

    factory { (view: WalletsSearchContract.View) -> WalletsSearchPresenter(view, get(), get()) }
    factory { (view: WalletsContract.View) -> WalletsPresenter(view, get(), get()) }

    factory { (view: ProtocolSelectionContract.View) -> ProtocolSelectionPresenter(view, get()) }
    factory { (view: QrCodeScannerContract.View) -> QrCodeScannerPresenter(view, get()) }
    factory { (view: DepositCreationContract.View) -> DepositCreationPresenter(view, get(), get(), get()) }
    factory { (view: WithdrawalCreationContract.View) -> WithdrawalCreationPresenter(view, get(), get()) }

    factory { (view: OrdersContainerContract.View) -> OrdersContainerPresenter(view, get()) }
    factory { (view: OrdersSearchContract.View) -> OrdersSearchPresenter(view, get()) }
    factory { (view: OrdersContract.View) -> OrdersPresenter(view, get(), get(), get()) }

    factory { (view: TransactionsSearchContract.View) -> TransactionsSearchPresenter(view, get()) }
    factory { (view: TransactionsContract.View) -> TransactionsPresenter(view, get(), get()) }

    factory { (view: ReferralContract.View) -> ReferralPresenter(view, get(), get(), get()) }

    factory { (view: VerificationSelectionContract.View) -> VerificationSelectionPresenter(view, get()) }

    factory { (view: SettingsContract.View) -> SettingsPresenter(view, get(), get()) }
    factory { (view: LanguageContract.View) -> LanguagePresenter(view, get(), get(), get()) }

    factory { (view: HelpContract.View) -> HelpPresenter(view, get(), get(), get(), get(), get()) }

    factory { (view: AboutContract.View) -> AboutPresenter(view, get(), get()) }

    factory { (view: CurrencyMarketPreviewContract.View) -> CurrencyMarketPreviewPresenter(view, get(), get(), get(), get()) }

    factory { (view: OrderbookContract.View) -> OrderbookPresenter(view, get(), get()) }

    factory { (view: TradeContract.View) -> TradePresenter(view, get(), get(), get(), get(), get(), get()) }

    factory { (view: DeepLinkHandlerContract.View) -> DeepLinkHandlerPresenter(view, get()) }

    factory { (view: InboxContract.View) -> InboxPresenter(view, get(), get()) }

    factory { (view: NewsContainerContract.View) -> NewsContainerPresenter(view, get()) }
    factory { (view: TwitterNewsContract.View) -> TwitterNewsPresenter(view, get()) }
    factory { (view: BlogNewsContract.View) -> BlogNewsPresenter(view, get()) }

    factory { (view: AlertPriceContract.View) -> AlertPricePresenter(view, get()) }

    factory { (view: AlertPriceAddContract.View) -> AlertPriceAddPresenter(view, get()) }

}