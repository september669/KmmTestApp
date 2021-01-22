package org.dda.testwork.androidApp.ui.base

import android.content.Context
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.viewbinding.ViewBinding
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.dda.ankoLogger.AnkoLogger
import org.dda.ankoLogger.logDebug
import org.dda.testwork.androidApp.ui.utils.AndroidContextHolder
import org.dda.testwork.shared.coroutine_context.CoroutineExecutionContext
import org.dda.testwork.shared.coroutine_context.ExecutionProgress
import org.dda.testwork.shared.coroutine_context.coroutineDispatchers
import org.kodein.di.DIAware
import org.kodein.di.android.closestDI

abstract class BaseActivity<VB : ViewBinding> : AppCompatActivity(),
    DIAware,
    AnkoLogger,
    CoroutineExecutionContext,
    AndroidContextHolder {

    override val di by closestDI()

    @Volatile
    override var isDestroyedContext = false
    override val dispatchers = coroutineDispatchers()
    override val scopeJob = CoroutineExecutionContext.SupervisorScopeJob()
    override val coroutineContext = Dispatchers.Main + scopeJob

    override val androidContext: Context get() = this

    private var _binding: VB? = null
    protected open val binding: VB
        get() = _binding!!


    protected abstract fun inflateViewBind(): VB

    override fun showProgress(show: Boolean, progress: ExecutionProgress) {
        logDebug("showProgress($show, $progress)")
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        logDebug("onCreate()")
        super.onCreate(savedInstanceState)
        _binding = inflateViewBind().also { vb ->
            setContentView(vb.root)
        }
    }

    override fun onStart() {
        logDebug("onStart()")
        super.onStart()
    }

    override fun onRestart() {
        logDebug("onRestart()")
        super.onRestart()
    }

    override fun onResume() {
        logDebug("onResume()")
        super.onResume()
    }

    override fun onResumeFragments() {
        logDebug("onResumeFragments()")
        super.onResumeFragments()
    }

    override fun onPause() {
        logDebug("onPause()")
        super.onPause()
    }

    override fun onStop() {
        logDebug("onStop()")
        super.onStop()
    }

    @ExperimentalCoroutinesApi
    override fun onDestroy() {
        logDebug("onDestroy()")
        cleanExecutionContext()
        super.onDestroy()
    }

    override fun finish() {
        logDebug("finish()")
        super.finish()
    }

}