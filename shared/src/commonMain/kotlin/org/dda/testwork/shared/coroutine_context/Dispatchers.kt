package org.dda.testwork.shared.coroutine_context

import kotlinx.coroutines.CoroutineDispatcher


interface CoroutineDispatchers {
    val default: CoroutineDispatcher
    val main: CoroutineDispatcher
    val unconfined: CoroutineDispatcher
    val io: CoroutineDispatcher
}

expect fun coroutineDispatchers(): CoroutineDispatchers