package com.stocksexchange.android.di.utils

import android.content.ComponentCallbacks
import org.koin.android.ext.android.get
import org.koin.android.ext.android.getKoin
import org.koin.core.KoinComponent
import org.koin.core.definition.BeanDefinition
import org.koin.core.definition.Definition
import org.koin.core.get
import org.koin.core.inject
import org.koin.core.module.Module
import org.koin.core.parameter.ParametersDefinition
import org.koin.core.qualifier.named
import org.koin.core.scope.Scope


inline fun <reified T> Scope.get(
    name: String,
    noinline parameters: ParametersDefinition? = null
): T {
    return get(T::class, named(name), parameters)
}


inline fun <reified T> Scope.inject(
    name: String,
    noinline parameters: ParametersDefinition? = null
) : Lazy<T> {
    return inject(named(name), parameters)
}


inline fun <reified T> KoinComponent.get(
    name: String,
    noinline parameters: ParametersDefinition? = null
): T {
    return get(named(name), parameters)
}


inline fun <reified T> KoinComponent.inject(
    name: String,
    noinline parameters: ParametersDefinition? = null
) : Lazy<T> {
    return inject(named(name), parameters)
}


inline fun <reified T : Any> ComponentCallbacks.get(
    name: String,
    noinline parameters: ParametersDefinition? = null
): T = getKoin().get(named(name), parameters)


inline fun <reified T : Any> ComponentCallbacks.inject(
    name: String,
    noinline parameters: ParametersDefinition? = null
) = lazy { get<T>(named(name), parameters) }


inline fun <reified T> Module.factory(
    name: String,
    override: Boolean = false,
    noinline definition: Definition<T>
): BeanDefinition<T> {
    return factory(named(name), override, definition)
}


inline fun <reified T> Module.single(
    name: String,
    createdAtStart: Boolean = false,
    override: Boolean = false,
    noinline definition: Definition<T>
): BeanDefinition<T> {
    return single(named(name), createdAtStart, override, definition)
}