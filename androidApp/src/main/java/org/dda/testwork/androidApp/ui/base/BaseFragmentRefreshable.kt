package org.dda.testwork.androidApp.ui.base


import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.LayoutRes
import androidx.core.view.children
import androidx.lifecycle.LiveData
import androidx.viewbinding.ViewBinding
import org.dda.ankoLogger.logDebug
import org.dda.testwork.androidApp.databinding.BaseFragmentRefreshableBinding
import org.dda.testwork.androidApp.ui.utils.setDisplayedChildIfDiffer
import org.dda.testwork.shared.coroutine_context.ExecutionProgress
import org.dda.testwork.shared.redux.*
import org.dda.testwork.shared.utils.checkWhen


abstract class BaseFragmentRefreshable<
        VB : ViewBinding,
        State : ReduxState,
        Action : ReduxAction,
        Effect : ReduxSideEffect,
        VM : BaseReduxViewModel<State, Action, Effect>
        >(@LayoutRes layoutId: Int) :
    BaseFragment<State, VB, VM>(layoutId) {


    private var _bindingRefreshable: BaseFragmentRefreshableBinding? = null
    private val bindingRefreshable: BaseFragmentRefreshableBinding
        get() = _bindingRefreshable!!

    private var _bindingContent: VB? = null
    protected override val binding: VB
        get() = _bindingContent!!

    private fun subscribeProgress(data: LiveData<VMEvents.ShowProgress>) {
        logDebug("subscribeProgress")
        data.observe(this) { item ->
            showProgress(item.show, item.progress)
        }
    }

    private fun subscribeState(data: LiveData<VMEvents.ViewState<State, ErrorKind>>) {
        logDebug("subscribeState")
        data.observe(this) { item ->
            when (item) {
                is VMEvents.ViewState.ShowContent -> {
                    showContent(item.content)
                    renderContent(item.content)
                }
                is VMEvents.ViewState.ShowError -> {
                    showError(item.errorKind)
                }
            }.checkWhen()
        }
    }

    protected abstract fun renderContent(content: State)
    protected abstract fun onRetryButtonClicked()
    protected abstract fun onRefresh()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        subscribeProgress(viewModel.liveDataProgress.ld())
        subscribeState(viewModel.liveDataState.ld())

    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        logDebug("onCreateView()")
        return BaseFragmentRefreshableBinding.inflate(inflater, container, false).also { rootBind ->

            val contentView = inflater.inflate(layoutId, rootBind.containerLayout, true).let {
                (it as ViewGroup).children.first()
            }
            _bindingRefreshable = rootBind
            _bindingContent = bindView(contentView)

            rootBind.refreshLayout.setOnRefreshListener {
                onRefresh()
            }

        }.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _bindingRefreshable = null
        _bindingContent = null
    }


    protected var isRefreshing: Boolean
        get() = bindingRefreshable.refreshLayout.isRefreshing
        set(value) {
            bindingRefreshable.refreshLayout.isRefreshing = value
        }

    protected var isSwipeToRefreshEnabled: Boolean
        get() = bindingRefreshable.refreshLayout.isEnabled
        set(value) {
            bindingRefreshable.refreshLayout.isEnabled = value
        }

    protected fun setSwipeableChildren(vararg viewIds: Int) {
        bindingRefreshable.refreshLayout.setSwipeableChildren(*viewIds)
    }

    fun showRefreshing(enable: Boolean) {
        logDebug { "showRefreshing($enable)" }
        if (isRefreshing != enable)
            isRefreshing = enable
    }

    override fun showProgress(show: Boolean, progress: ExecutionProgress) {
        logDebug { "showProgress($show, $progress)" }
        isRefreshing = show
    }


    private fun showError(errorKind: ErrorKind) {
        logDebug { "showError($errorKind)" }
        bindingRefreshable.flipContentError.setDisplayedChildIfDiffer(
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

    private fun showContent(content: State) {
        logDebug { "showContent(${content.toLogString()})" }
        bindingRefreshable.flipContentError.setDisplayedChildIfDiffer(0)
    }
}