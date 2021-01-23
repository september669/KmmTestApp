package org.dda.testwork.shared.coroutine_context

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.runBlocking

actual fun runTest(
    block: suspend CoroutineScope.() -> Unit
) {
    runBlocking(block = block)
}