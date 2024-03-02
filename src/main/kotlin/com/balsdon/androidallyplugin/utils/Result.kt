package com.balsdon.androidallyplugin.utils

import kotlin.reflect.KClass

inline fun <R, T : R> Result<T>.onException(
    vararg exceptions: KClass<out Throwable>,
    transform: (exception: Throwable) -> T
) = recoverCatching { ex ->
    if (ex::class in exceptions) {
        transform(ex)
    } else throw ex
}