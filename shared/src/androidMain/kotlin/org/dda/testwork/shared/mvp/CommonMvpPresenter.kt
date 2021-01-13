package org.dda.testwork.shared.mvp

import moxy.MvpPresenter
import moxy.MvpView
import org.dda.testwork.shared.coroutine_context.ExecutionProgress


actual interface CommonMvpView : MvpView {

    actual fun showProgress(show: Boolean, progress: ExecutionProgress)

}


actual abstract class CommonMvpPresenter<View : CommonMvpView> : MvpPresenter<View>() {

    protected actual val viewState: View get() = super.getViewState()

    actual override fun attachView(view: View) = super.attachView(view)

    actual override fun onFirstViewAttach() = super.onFirstViewAttach()

    actual override fun detachView(view: View) = super.detachView(view)

    actual override fun destroyView(view: View) = super.destroyView(view)

    actual override fun onDestroy() = super.onDestroy()


}