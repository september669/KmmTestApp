package org.dda.testwork.shared.mvp

import kotlinx.coroutines.CancellationException
import org.dda.ankoLogger.logDebug
import org.dda.ankoLogger.logError
import org.dda.testwork.shared.coroutine_context.CoroutineExecutionContext
import org.dda.testwork.shared.coroutine_context.ExecutionProgress

interface ExecutionContext<View : CommonMvpView, Handler : ExceptionHandler> :
    CoroutineExecutionContext {

    val baseView: View
    val exceptionHandler: Handler

    override fun showProgress(show: Boolean, progress: ExecutionProgress) {
        baseView.showProgress(show, progress)
    }

    override fun handleException(exc: Throwable): Boolean {
        logDebug { "handleException(${exc::class.simpleName}: ${exc.message})" }
        val isProcessed = exceptionHandler.handle(exc, baseView)
        if (!isProcessed && exc !is CancellationException) {
            logError("Did not handle exception", exc)
        }
        return isProcessed
    }
}