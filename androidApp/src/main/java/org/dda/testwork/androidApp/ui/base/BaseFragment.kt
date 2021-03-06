package org.dda.testwork.androidApp.ui.base

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.LayoutRes
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.viewbinding.ViewBinding
import dev.icerock.moko.mvvm.createViewModelFactory
import dev.icerock.moko.mvvm.viewmodel.ViewModel
import kotlinx.coroutines.Dispatchers
import org.dda.ankoLogger.logDebug
import org.dda.ankoLogger.logError
import org.dda.testwork.androidApp.ui.utils.AndroidContextHolder
import org.dda.testwork.shared.coroutine_context.CoroutineExecutionContext
import org.dda.testwork.shared.coroutine_context.ExecutionProgress
import org.dda.testwork.shared.coroutine_context.coroutineDispatchers
import org.dda.testwork.shared.redux.ReduxState
import org.kodein.di.DIAware
import org.kodein.di.android.x.closestDI
import org.kodein.di.direct
import org.kodein.di.instance
import kotlin.reflect.KClass


abstract class BaseFragment<
        State : ReduxState,
        VB : ViewBinding,
        VM : ViewModel
        >(@LayoutRes val layoutId: Int) : Fragment(),
    CoroutineExecutionContext,
    DIAware,
    AndroidContextHolder {


    override val di by closestDI()

    @Volatile
    override var isDestroyedContext = false
    override val dispatchers = coroutineDispatchers()
    override val scopeJob = CoroutineExecutionContext.SupervisorScopeJob()
    override val coroutineContext = Dispatchers.Main + scopeJob

    override val androidContext: Context get() = requireContext()

    private var _binding: VB? = null
    protected open val binding: VB
        get() = _binding!!


    protected lateinit var viewModel: VM

    protected abstract fun bindView(view: View): VB

    protected abstract fun makeViewModelBluePrint(): ViewModelBluePrint<VM>

    abstract override fun showProgress(show: Boolean, progress: ExecutionProgress)

    override fun onAttach(context: Context) {
        super.onAttach(context)
        logDebug("onAttach()")
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val (clazz, factory, alsoBlock) = makeViewModelBluePrint()
        viewModel = ViewModelProvider(this, factory).get(clazz.java)
        alsoBlock(viewModel)
        logError { "ViewModelProvider return: $viewModel" }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        logDebug("onCreateView()")
        return inflater.inflate(layoutId, container, false).also { rootView ->
            _binding = bindView(rootView)
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        logDebug("onViewCreated()")
        super.onViewCreated(view, savedInstanceState)
    }

    override fun onStart() {
        super.onStart()
        logDebug("onStart()")
    }

    override fun onResume() {
        logDebug("onResume()")
        super.onResume()
    }

    override fun onPause() {
        logDebug("onPause()")
        super.onPause()
    }

    override fun onStop() {
        logDebug("onStop()")
        super.onStop()
    }

    override fun onDestroyView() {
        logDebug("onDestroyView()")
        super.onDestroyView()
        _binding = null
    }

    override fun onSaveInstanceState(outState: Bundle) {
        logDebug("onSaveInstanceState()")
        super.onSaveInstanceState(outState)
    }

    override fun onDetach() {
        logDebug("onDetach()")
        cleanExecutionContext()
        super.onDetach()
    }

    override fun onDestroy() {
        logDebug("onDestroy()")
        super.onDestroy()
    }

    data class ViewModelBluePrint<VM : ViewModel>(
        val viewModelClass: KClass<VM>,
        val factory: ViewModelProvider.Factory,
        val also: (vm: VM) -> Unit
    )

    protected inline fun <reified VM : ViewModel> viewModelBluePrint(noinline also: (vm: VM) -> Unit = {}): ViewModelBluePrint<VM> {
        return ViewModelBluePrint(
            viewModelClass = VM::class,
            factory = createViewModelFactory {
                di.direct.instance<VM>()
            },
            also = also
        )
    }

}

