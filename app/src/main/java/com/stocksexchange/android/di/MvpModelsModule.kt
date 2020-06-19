package com.stocksexchange.android.di

import com.stocksexchange.android.ui.alertprice.additem.AlertPriceAddModel
import com.stocksexchange.android.ui.alertprice.AlertPriceModel
import com.stocksexchange.android.ui.auth.AuthenticationModel
import com.stocksexchange.android.ui.balance.BalanceContainerModel
import com.stocksexchange.android.ui.base.mvp.model.StubModel
import com.stocksexchange.android.ui.currencymarketpreview.CurrencyMarketPreviewModel
import com.stocksexchange.android.ui.currencymarkets.fragment.CurrencyMarketsModel
import com.stocksexchange.android.ui.profile.ProfileModel
import com.stocksexchange.android.ui.deeplinkhandler.DeepLinkHandlerModel
import com.stocksexchange.android.ui.help.HelpModel
import com.stocksexchange.android.ui.inbox.InboxModel
import com.stocksexchange.android.ui.language.LanguageModel
import com.stocksexchange.android.ui.login.LoginModel
import com.stocksexchange.android.ui.news.fragments.blog.BlogNewsModel
import com.stocksexchange.android.ui.news.fragments.twitter.TwitterNewsModel
import com.stocksexchange.android.ui.orderbook.OrderbookModel
import com.stocksexchange.android.ui.orders.fragment.OrdersModel
import com.stocksexchange.android.ui.passwordrecovery.PasswordRecoveryModel
import com.stocksexchange.android.ui.pinrecovery.PinRecoveryModel
import com.stocksexchange.android.ui.referral.ReferralModel
import com.stocksexchange.android.ui.registration.RegistrationModel
import com.stocksexchange.android.ui.settings.SettingsModel
import com.stocksexchange.android.ui.splash.SplashModel
import com.stocksexchange.android.ui.trade.TradeModel
import com.stocksexchange.android.ui.transactioncreation.depositcreation.DepositCreationModel
import com.stocksexchange.android.ui.transactioncreation.withdrawalcreation.WithdrawalCreationModel
import com.stocksexchange.android.ui.transactions.fragment.TransactionsModel
import com.stocksexchange.android.ui.wallets.fragment.WalletsModel
import org.koin.dsl.module

val mvpModelsModule = module {

    factory { StubModel() }

    factory { SplashModel(get(), get(), get(), get()) }

    factory { AuthenticationModel(get(), get(), get()) }
    factory { PinRecoveryModel(get(), get()) }

    factory { CurrencyMarketsModel(get()) }

    factory { LoginModel(get(), get(), get()) }
    factory { RegistrationModel(get()) }
    factory { PasswordRecoveryModel(get()) }

    factory { BalanceContainerModel(get(), get()) }

    factory { WalletsModel(get()) }
    factory { DepositCreationModel(get(), get()) }
    factory { WithdrawalCreationModel(get(), get(), get()) }

    factory { OrdersModel(get(), get()) }

    factory { TransactionsModel(get(), get(), get()) }

    factory { ReferralModel(get(), get()) }

    factory { SettingsModel(get(), get(), get(), get(), get()) }
    factory { LanguageModel(get()) }

    factory { HelpModel() }

    factory { CurrencyMarketPreviewModel(get(), get(), get(), get(), get(), get(), get()) }

    factory { OrderbookModel(get()) }

    factory { TradeModel(get(), get(), get(), get()) }

    factory { DeepLinkHandlerModel(get(), get(), get()) }

    factory { InboxModel(get()) }

    factory { TwitterNewsModel(get()) }
    factory { BlogNewsModel(get()) }

    factory { AlertPriceModel(get(), get()) }
    factory { AlertPriceAddModel(get(), get()) }

    factory { ProfileModel(get()) }

}