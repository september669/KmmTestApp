package org.dda.testwork.shared.coroutine_context

import kotlinx.coroutines.Dispatchers


private val coroutineDispatchers = object : CoroutineDispatchers {
    override val default = Dispatchers.Default
    override val main = Dispatchers.Main
    override val unconfined = Dispatchers.Unconfined
    override val io = Dispatchers.Default
}

actual fun coroutineDispatchers(): CoroutineDispatchers = coroutineDispatchers