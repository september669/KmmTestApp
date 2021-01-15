package org.dda.testwork.androidApp.ui.base

//import org.dda.testwork.androidApp.databinding.BaseFragmentRefreshableBinding.inflate
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.LayoutRes
import org.dda.ankoLogger.logDebug
import org.dda.testwork.androidApp.databinding.BaseFragmentRefreshableBinding
import org.dda.testwork.androidApp.ui.utils.setDisplayedChildIfDiffer
import org.dda.testwork.shared.coroutine_context.ExecutionProgress
import org.dda.testwork.shared.mvp.BaseViewRedux
import org.dda.testwork.shared.mvp.ErrorKind
import org.dda.testwork.shared.mvp.base.CommonMvpView
import org.dda.testwork.shared.mvp.base.ExceptionHandler
import org.dda.testwork.shared.mvp.redux.ReduxState


interface BaseViewRefreshable<State : ReduxState> : BaseViewRedux<State> {

    fun showRefreshing(enable: Boolean)

}


abstract class BaseFragmentRefreshable<State : ReduxState>(@LayoutRes layout: Int) :
    BaseFragment<State>(layout),
    BaseViewRefreshable<State> {


    private var _binding: BaseFragmentRefreshableBinding? = null
    private val binding get() = _binding!!


    private lateinit var contentView: View

    override val exceptionHandler = object : ExceptionHandler {
        override fun handle(exc: Throwable, view: CommonMvpView): Boolean {
            TODO("Not yet implemented")
        }

    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        logDebug { "onCreateView()" }
        return BaseFragmentRefreshableBinding.inflate(inflater, container, false).let { bind ->
            contentView = inflater.inflate(layout, bind.containerLayout, true)
            _binding = bind
            bind.root
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }


    protected var isRefreshing: Boolean
        get() = binding.refreshLayout.isRefreshing
        set(value) {
            binding.refreshLayout.isRefreshing = value
        }

    protected var isSwipeToRefreshEnabled: Boolean
        get() = binding.refreshLayout.isEnabled
        set(value) {
            binding.refreshLayout.isEnabled = value
        }

    protected fun setSwipeableChildren(vararg viewIds: Int) {
        binding.refreshLayout.setSwipeableChildren(*viewIds)
    }

    override fun showRefreshing(enable: Boolean) {
        logDebug { "showRefreshing($enable)" }
        if (isRefreshing != enable)
            isRefreshing = enable
    }

    override fun showProgress(show: Boolean, progress: ExecutionProgress) {
        logDebug { "showProgress($show, $progress)" }
        isRefreshing = show
    }


    override fun renderError(errorKind: ErrorKind) {
        binding.flipContentError.setDisplayedChildIfDiffer(
            if (errorKind is ErrorKind.None) {
                0
            } else {
                1
            }
        )
        /*when (errorKind) {
            ErrorKind.None -> TODO()
            ErrorKind.Generic -> TODO()
            ErrorKind.Network -> TODO()
            ErrorKind.Maintenance -> TODO()
            ErrorKind.NotFound -> TODO()
            is ErrorKind.Api -> TODO()
        }.checkWhen()*/
    }

    override fun renderContent(content: State) {
        binding.flipContentError.setDisplayedChildIfDiffer(0)
    }
}