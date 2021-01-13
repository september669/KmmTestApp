package org.dda.testwork.shared.mvp

import kotlinx.atomicfu.atomic
import kotlinx.coroutines.Dispatchers
import org.dda.ankoLogger.AnkoLogger
import org.dda.ankoLogger.logDebug
import org.dda.testwork.shared.coroutine_context.CoroutineExecutionContext
import org.dda.testwork.shared.coroutine_context.coroutineDispatchers


abstract class BasePresenter<View : CommonMvpView> : CommonMvpPresenter<View>(), AnkoLogger,
    ExecutionContext<View, ExceptionHandler> {

    //  ExecutionContext
    private val _isDestroyedContext = atomic(false)
    final override var isDestroyedContext
        get() = _isDestroyedContext.value
        set(value) {
            _isDestroyedContext.value = value
        }
    final override val dispatchers = coroutineDispatchers()
    final override val scopeJob = CoroutineExecutionContext.SupervisorScopeJob()
    final override val coroutineContext = Dispatchers.Main + scopeJob

    final override val baseView: View = viewState

    override fun attachView(view: View) {
        logDebug { "attachView()" }
        super.attachView(view)
    }

    override fun onFirstViewAttach() {
        logDebug { "onFirstViewAttach()" }
        super.onFirstViewAttach()
    }

    override fun detachView(view: View) {
        logDebug { "detachView()" }
        super.detachView(view)
    }

    override fun destroyView(view: View) {
        logDebug { "destroyView()" }
        super.destroyView(view)
    }

    override fun onDestroy() {
        logDebug { "onDestroy()" }
        cleanExecutionContext()
        super.onDestroy()
    }
}