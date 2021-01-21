package org.dda.testwork.shared.coroutine_context

import kotlinx.coroutines.*
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock
import org.dda.ankoLogger.logDebug
import org.dda.ankoLogger.logError
import kotlin.coroutines.CoroutineContext

interface CoroutineExecutionContext : BaseExecutionContext, CoroutineScope {
    companion object {
        const val SHOW_PROGRESS_DELAY_MS = 250L
    }

    class SupervisorScopeJob : Job by SupervisorJob()

    val dispatchers: CoroutineDispatchers

    val scopeJob: SupervisorScopeJob

    val coroutineTag: String get() = loggerTag

    fun coroutineName(suffix: String): CoroutineName = CoroutineName("$coroutineTag:$suffix")

    override val coroutineContext: CoroutineContext// = Dispatchers.Main + scopeJob
    /* + CoroutineExceptionHandler { _, thr ->
       //Uncomment this block, if you want to debug uncaught exceptions
       //Do not pass it to release!!!
           onExceptionHandler(thr)
       }
   */
    /***************************************/

    fun launchDefault(mutex: Mutex? = null, block: suspend CoroutineScope.() -> Unit): Job {
        return launchAny(
            context = dispatchers.default,
            progress = executionProgressGlobalDefault,
            showProgress = false,
            showProgressDelay = 0,
            doOnError = null,
            mutex = mutex,
            block = block
        )
    }

    fun <R> asyncIO(
        block: suspend CoroutineScope.() -> R
    ): Deferred<R> {
        return asyncAny(dispatchers.io + coroutineName("asyncIO"), block)
    }

    fun <R> asyncUi(
        block: suspend CoroutineScope.() -> R
    ): Deferred<R> {
        return asyncAny(dispatchers.main + coroutineName("asyncUi"), block)
    }

    private fun <R> asyncAny(
        context: CoroutineContext,
        block: suspend CoroutineScope.() -> R
    ): Deferred<R> {
        return async(context = context) {
            try {
                block.invoke(this)
            } catch (exc: Exception) {
                logError("", exc)
                throw exc
            }
        }.also {
            checkDestroyed()
        }
    }

    /**
     * Вложенные вызовы launchXXX не рекомендуются - возможны артефакты показа ошибок
     */
    fun launchUi(
        mutex: Mutex? = null,
        block: suspend CoroutineScope.() -> Unit
    ): Job {
        return launchAny(
            context = dispatchers.main + coroutineName("launchUi"),
            progress = executionProgressGlobalDefault,
            showProgress = false,
            showProgressDelay = 0,
            doOnError = null,
            mutex = mutex,
            block = block
        )
    }

    /**
     * Вложенные вызовы launchXXX не рекомендуются - возможны артефакты показа ошибок
     */
    fun launchUiProgress(
        progress: ExecutionProgress = executionProgressGlobalDefault,
        showProgressDelay: Long = SHOW_PROGRESS_DELAY_MS,
        mutex: Mutex? = null,
        doOnError: ((exc: Exception) -> Boolean)? = null,
        block: suspend CoroutineScope.() -> Unit
    ): Job {
        return launchAny(
            context = dispatchers.main + coroutineName("launchUiProgress"),
            progress = progress,
            showProgress = true,
            showProgressDelay = showProgressDelay,
            doOnError = doOnError,
            mutex = mutex,
            block = block
        )
    }

    /**
     * Вложенные вызовы launchXXX не рекомендуются - возможны артефакты показа ошибок
     */
    fun launchNet(
        progress: ExecutionProgress = executionProgressGlobalDefault,
        showProgressDelay: Long = SHOW_PROGRESS_DELAY_MS,
        doOnError: ((exc: Exception) -> Boolean)? = null,
        mutex: Mutex? = null,
        block: suspend CoroutineScope.() -> Unit
    ): Job {
        return launchAny(
            context = dispatchers.io + coroutineName("launchNet"),
            progress = progress,
            showProgress = false,
            showProgressDelay = showProgressDelay,
            doOnError = doOnError,
            mutex = mutex,
            block = block
        )
    }

    /**
     * Вложенные вызовы launchXXX не рекомендуются - возможны артефакты показа ошибок
     */
    fun launchNetProgress(
        progress: ExecutionProgress = executionProgressGlobalDefault,
        showProgressDelay: Long = SHOW_PROGRESS_DELAY_MS,
        doOnError: ((exc: Exception) -> Boolean)? = null,
        mutex: Mutex? = null,
        block: suspend CoroutineScope.() -> Unit
    ): Job {
        return launchAny(
            context = dispatchers.io + coroutineName("launchNetProgress"),
            progress = progress,
            showProgress = true,
            showProgressDelay = showProgressDelay,
            doOnError = doOnError,
            mutex = mutex,
            block = block
        )
    }

    /**
     * Вложенные вызовы launchXXX не рекомендуются - возможны артефакты показа ошибок
     */
    private fun launchAny(
        context: CoroutineContext,
        progress: ExecutionProgress,
        showProgress: Boolean,
        showProgressDelay: Long,
        doOnError: ((exc: Exception) -> Boolean)?,
        mutex: Mutex? = null,
        block: suspend CoroutineScope.() -> Unit
    ): Job {
        val lock = Any()
        mutex?.tryLock(lock)
        return this.launch(context = context) {
            if (mutex != null && !mutex.holdsLock(lock)) {
                mutex.lock(lock)
            }
            doBeforeLaunch()
            var isLaunchOk = false
            try {
                withProgress(
                    scope = this@launch,
                    showProgress = showProgress,
                    progress = progress,
                    showProgressDelay = showProgressDelay
                ) {
                    block.invoke(this)
                }
                isLaunchOk = true
            } catch (exc: Exception) {
                logError(exc.message, exc)
                val isConsumed = doOnError?.let {
                    withContext(dispatchers.main) { doOnError(exc) }
                } ?: false
                if (!isConsumed && !withContext(dispatchers.main) { handleException(exc) }) {
                    logError("Not processed exception", exc)
                    throw exc
                }
            } finally {
                doAfterLaunch(isLaunchOk)
                mutex?.unlock(lock)
            }
        }.also {
            it.invokeOnCompletion {
                if (mutex != null && mutex.holdsLock(lock)) {
                    mutex.unlock(lock)
                }
            }
            checkDestroyed()
        }
    }

    private suspend fun withProgress(
        scope: CoroutineScope,
        showProgress: Boolean,
        progress: ExecutionProgress,
        showProgressDelay: Long,
        block: suspend CoroutineScope.() -> Unit
    ) {
        var jobProgress: Job? = null
        try {
            jobProgress = if (showProgress) {
                scope.launch(dispatchers.main + SupervisorJob()) {
                    delay(showProgressDelay)
                    showProgress(true, progress)
                }
            } else {
                null
            }
            scope.block()
        } finally {
            if (jobProgress != null) {
                progress.beforeHide()
                jobProgress.cancelAndJoin()
                withContext(dispatchers.main + SupervisorJob()) {
                    showProgress(false, progress)
                }
            }
        }
    }

    fun doBeforeLaunch() {
        logDebug { "doBeforeLaunch()" }
    }

    fun doAfterLaunch(launchOk: Boolean) {
        logDebug { "doAfterLaunch($launchOk)" }
    }

    /***************************************/


    fun <T> Flow<T>.executeOnEach(
        showProgressDelay: Long = SHOW_PROGRESS_DELAY_MS,
        progress: ExecutionProgress = executionProgressGlobalDefault,
        mutex: Mutex? = null,
        showProgress: Boolean = false,
        blockContext: CoroutineContext = dispatchers.main,
        block: suspend CoroutineScope.(item: T) -> Unit
    ): Job {
        return onEach { item ->
            coroutineScope {
                withProgress(
                    scope = this@coroutineScope,
                    showProgress = showProgress,
                    progress = progress,
                    showProgressDelay = showProgressDelay
                ) {
                    try {
                        withContext(blockContext) {
                            if (mutex == null) {
                                block(item)
                            } else {
                                mutex.withLock {
                                    block(item)
                                }
                            }
                        }
                    } catch (exc: Exception) {
                        if (!handleException(exc)) {
                            logError("Not processed exception", exc)
                            throw exc
                        }
                    }
                }
            }
        }.catch { exc ->
            if (!handleException(exc)) {
                logError("Not processed exception", exc)
                throw exc
            }
        }.launchIn(this@CoroutineExecutionContext + coroutineName("executeOnEach"))
    }

    /*
    fun <T> ReceiveChannel<T>.executeOnEach(
        showProgressDelay: Long = SHOW_PROGRESS_DELAY_MS,
        progress: ExecutionProgress = executionProgressGlobalDefault,
        mutex: Mutex? = null,
        showProgress: Boolean = false,
        blockContext: CoroutineContext = dispatchers.main,
        block: suspend CoroutineScope.(item: T) -> Unit
    ): Job {
        return consumeAsFlow().executeOnEach(
            showProgressDelay = showProgressDelay,
            progress = progress,
            mutex = mutex,
            showProgress = showProgress,
            blockContext = blockContext,
            block = block
        )
    }
*/

    /***************************************/

    override fun cleanExecutionContext() {
        logDebug { "CoroutineExecutionContext.cleanExecutionContext()" }
        super.cleanExecutionContext()
        this.cancel()
    }
}


