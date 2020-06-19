package com.stocksexchange.android.di

import com.stocksexchange.android.di.features.settingsModule

val applicationModules = listOf(
    mvpPresentersModule,
    mvpModelsModule,
    apiModule,
    databaseModule,
    handlersModule,
    providersModule,
    dataStoresModule,
    repositoriesModule,
    freshDataHandlersModule,
    factoriesModule,
    managersModule,
    formattersModule,
    socketModule,
    settingsModule,
    miscellaneousModule
)