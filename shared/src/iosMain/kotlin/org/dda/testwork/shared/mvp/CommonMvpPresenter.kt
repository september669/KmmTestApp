package org.dda.testwork.shared.mvp

import org.dda.testwork.shared.coroutine_context.ExecutionProgress


actual interface CommonMvpView {
    actual fun showProgress(show: Boolean, progress: ExecutionProgress)
}

actual abstract class CommonMvpPresenter<View : CommonMvpView> {

    protected actual val viewState: View
        get() = TODO("Not yet implemented")

    actual open fun attachView(view: View) {}

    protected actual open fun onFirstViewAttach() {}

    actual open fun detachView(view: View) {}

    actual open fun destroyView(view: View) {}

    actual open fun onDestroy() {}
}