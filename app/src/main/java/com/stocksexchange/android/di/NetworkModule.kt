package com.stocksexchange.android.di

import com.google.gson.GsonBuilder
import com.stocksexchange.android.BuildConfig
import com.stocksexchange.android.Constants
import com.stocksexchange.android.R
import com.stocksexchange.api.StexRestApi
import com.stocksexchange.api.StexRssApi
import com.stocksexchange.api.services.StexRestService
import com.stocksexchange.api.services.StexRssService
import com.stocksexchange.api.authenticators.OAuthTokensAuthenticator
import com.stocksexchange.api.interceptors.HostInterceptor
import com.stocksexchange.api.interceptors.UserAgentInterceptor
import com.stocksexchange.android.di.utils.get
import com.stocksexchange.android.di.utils.single
import com.stocksexchange.android.utils.providers.StringProvider
import com.stocksexchange.api.utils.ResponseExtractor
import com.stocksexchange.core.utils.helpers.composeUserAgentHeader
import okhttp3.CertificatePinner
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import org.koin.dsl.module
import org.simpleframework.xml.convert.AnnotationStrategy
import org.simpleframework.xml.core.Persister
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.converter.simplexml.SimpleXmlConverterFactory
import java.util.concurrent.TimeUnit

const val STEX_OK_HTTP_CLIENT_REST = "stex_ok_http_client_rest"
const val STEX_OK_HTTP_CLIENT_RSS = "stex_ok_http_client_rss"

const val STEX_HOST_INTERCEPTOR_REST = "stex_host_interceptor_rest"
const val STEX_HOST_INTERCEPTOR_RSS = "stex_host_interceptor_rss"

private const val STEX_RETROFIT_REST = "stex_retrofit_rest"
private const val STEX_RETROFIT_RSS = "stex_retrofit_rss"

val apiModule = module {

    single(STEX_OK_HTTP_CLIENT_REST) {
        OkHttpClient.Builder().apply {
            connectTimeout(Constants.STEX_OK_HTTP_CLIENT_TIMEOUT, TimeUnit.MILLISECONDS)
            readTimeout(Constants.STEX_OK_HTTP_CLIENT_TIMEOUT, TimeUnit.MILLISECONDS)
            certificatePinner(CertificatePinner.Builder()
                .add(Constants.STEX_HOSTNAME, BuildConfig.CERTIFICATE_PUBLIC_KEY_HASH_FIRST)
                .add(Constants.STEX_HOSTNAME, BuildConfig.CERTIFICATE_PUBLIC_KEY_HASH_SECOND)
                .add(Constants.STEX_HOSTNAME, BuildConfig.CERTIFICATE_PUBLIC_KEY_HASH_THIRD)
                .build()
            )
            addInterceptor(get(STEX_HOST_INTERCEPTOR_REST))
            addInterceptor(get<UserAgentInterceptor>())

            if(BuildConfig.DEBUG) {
                addInterceptor(get<HttpLoggingInterceptor>())
            }

            authenticator(get<OAuthTokensAuthenticator>())
        }.build()
    }

    single(STEX_OK_HTTP_CLIENT_RSS) {
        OkHttpClient.Builder().apply {
            connectTimeout(Constants.STEX_OK_HTTP_CLIENT_TIMEOUT, TimeUnit.MILLISECONDS)
            readTimeout(Constants.STEX_OK_HTTP_CLIENT_TIMEOUT, TimeUnit.MILLISECONDS)

            addInterceptor(get(STEX_HOST_INTERCEPTOR_RSS))
            addInterceptor(get<UserAgentInterceptor>())

            if(BuildConfig.DEBUG) {
                addInterceptor(get<HttpLoggingInterceptor>())
            }
        }.build()
    }

    single(STEX_RETROFIT_REST) {
        Retrofit.Builder()
            .baseUrl(Constants.STEX_API_BASE_URL + "/")
            .addConverterFactory(GsonConverterFactory.create(get()))
            .client(get(STEX_OK_HTTP_CLIENT_REST))
            .build()
    }

    single(STEX_RETROFIT_RSS) {
        Retrofit.Builder()
            .baseUrl(Constants.STEX_WEBSITE_URL + "/")
            .addConverterFactory(SimpleXmlConverterFactory.create(Persister(AnnotationStrategy())))
            .client(get(STEX_OK_HTTP_CLIENT_RSS))
            .build()
    }

    single { get<Retrofit>(STEX_RETROFIT_REST).create(StexRestService::class.java) }
    single { get<Retrofit>(STEX_RETROFIT_RSS).create(StexRssService::class.java) }

    single { StexRestApi(get(), get(), get()) }
    single { StexRssApi(get(), get()) }

    single { ResponseExtractor(get()) }

    single { GsonBuilder().create() }

    single(STEX_HOST_INTERCEPTOR_REST) { HostInterceptor() }
    single(STEX_HOST_INTERCEPTOR_RSS) { HostInterceptor() }

    factory {
        UserAgentInterceptor(composeUserAgentHeader(
            get<StringProvider>().getString(R.string.app_name),
            BuildConfig.VERSION_NAME
        ))
    }

    factory {
        HttpLoggingInterceptor().apply {
            level = HttpLoggingInterceptor.Level.BODY
        }
    }

    factory { OAuthTokensAuthenticator(get(), inject()) }

}