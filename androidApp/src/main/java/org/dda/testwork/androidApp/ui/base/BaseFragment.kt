package org.dda.testwork.androidApp.ui.base

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.sync.Mutex
import org.dda.ankoLogger.logDebug
import org.dda.testwork.androidApp.ui.utils.AndroidContextHolder
import org.dda.testwork.shared.coroutine_context.CoroutineExecutionContext
import org.dda.testwork.shared.coroutine_context.ExecutionProgress
import org.dda.testwork.shared.coroutine_context.coroutineDispatchers
import org.dda.testwork.shared.mvp.BaseViewRedux
import org.dda.testwork.shared.mvp.base.CommonMvpView
import org.dda.testwork.shared.mvp.base.ExceptionHandler
import org.dda.testwork.shared.mvp.base.ExecutionContext
import org.dda.testwork.shared.mvp.redux.ReduxState

abstract class BaseFragment<State : ReduxState> : MvpAppCompatFragment(),
    BaseViewRedux<State>,
    ExecutionContext<BaseViewRedux<State>, ExceptionHandler>,
    AndroidContextHolder {

    abstract override fun showProgress(show: Boolean, progress: ExecutionProgress)

    abstract val layout: Int

    @Volatile
    override var isDestroyedContext = false
    override val dispatchers = coroutineDispatchers()
    override val scopeJob = CoroutineExecutionContext.SupervisorScopeJob()
    override val coroutineContext = Dispatchers.Main + scopeJob
    override val baseView: BaseViewRedux<State> get() = this
    //override val exceptionHandler = ErrorHandler()

    override val androidContext: Context get() = requireContext()

    private val onResumeMutex = Mutex(locked = true)


    class ReduxErrorHandler: ExceptionHandler{
        override fun handle(exc: Throwable, view: CommonMvpView): Boolean {
            TODO("Not yet implemented")
        }

    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        logDebug { "onAttach()" }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        logDebug { "onCreateView()" }
        return inflater.inflate(layout, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        logDebug { "onViewCreated()" }
        super.onViewCreated(view, savedInstanceState)
    }

    override fun onStart() {
        super.onStart()
        logDebug { "onStart()" }
    }

    override fun onResume() {
        logDebug { "onResume()" }
        super.onResume()
        onResumeMutex.unlock()
    }

    override fun onPause() {
        logDebug { "onPause()" }
        super.onPause()
    }

    override fun onStop() {
        logDebug { "onStop()" }
        super.onStop()
    }

    override fun onDestroyView() {
        logDebug { "onDestroyView()" }
        super.onDestroyView()
    }

    override fun onSaveInstanceState(outState: Bundle) {
        logDebug { "onSaveInstanceState()" }
        super.onSaveInstanceState(outState)
    }

    override fun onDetach() {
        logDebug { "onDetach()" }
        cleanExecutionContext()
        super.onDetach()
    }

    override fun onDestroy() {
        logDebug { "onDestroy()" }
        super.onDestroy()
    }
}