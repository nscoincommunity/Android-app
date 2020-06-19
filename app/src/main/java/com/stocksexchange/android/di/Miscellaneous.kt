package com.stocksexchange.android.di

import android.content.Context
import android.graphics.Bitmap
import com.squareup.picasso.LruCache
import com.squareup.picasso.Picasso
import com.stocksexchange.android.BuildConfig
import com.stocksexchange.android.analytics.FirebaseEventLogger
import com.stocksexchange.android.analytics.FirebaseEventLoggerImpl
import com.stocksexchange.android.notification.FirebasePushClient
import com.stocksexchange.android.notification.FirebasePushClientImpl
import com.stocksexchange.android.utils.*
import com.stocksexchange.android.utils.navigation.NavDestAnimationsRetriever
import com.stocksexchange.android.utils.navigation.NavOptionsCreator
import com.stocksexchange.api.utils.UrlBuilder
import com.stocksexchange.core.utils.EncryptionUtil
import com.stocksexchange.core.utils.extensions.getActivityManager
import com.stocksexchange.core.utils.extensions.getLocale
import org.koin.android.ext.koin.androidApplication
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module

val miscellaneousModule = module {

    single<FirebaseEventLogger> { FirebaseEventLoggerImpl(androidApplication(), get(), get()) }

    single<FirebasePushClient> { FirebasePushClientImpl(androidApplication(), get(), get(), get(), get(), get()) }

    single {
        Picasso.Builder(get())
            .defaultBitmapConfig(Bitmap.Config.ARGB_8888)
            .memoryCache(LruCache(getPicassoMemoryCacheSize(get())))
            .build().also {
                Picasso.setSingletonInstance(it)
            }
    }

    single { EncryptionUtil(BuildConfig.ENCRYPTION_KEY) }

    single { TipShowingTracker(get()) }

    single { NavDestAnimationsRetriever() }
    single { NavOptionsCreator(get()) }

    factory { androidContext().getLocale() }

    factory { ImageDownloader(get()) }

    factory { UrlBuilder() }

    factory { RemoteServiceUrlGenerator(get()) }
    factory { RemoteServiceUrlFinder(get(), get(), get(), get(), get(), get(), get()) }

    factory { DashboardArgsCreator() }
    factory { (navigator: NavigationDeepLinkHandler.Navigator) -> NavigationDeepLinkHandler(navigator) }

}


private fun getPicassoMemoryCacheSize(context: Context): Int {
    // ~17% of the available heap
    return ((1024 * 1024 * context.getActivityManager().memoryClass) / 6)
}