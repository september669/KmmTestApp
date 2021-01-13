package org.dda.testwork.shared.mvp

import org.dda.testwork.shared.coroutine_context.ExecutionProgress


expect interface CommonMvpView {

    fun showProgress(show: Boolean, progress: ExecutionProgress)

}

expect abstract class CommonMvpPresenter<View : CommonMvpView>() {

    protected val viewState: View

    open fun attachView(view: View)

    protected open fun onFirstViewAttach()

    open fun detachView(view: View)

    open fun destroyView(view: View)

    open fun onDestroy()
}