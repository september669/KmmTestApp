package org.dda.testwork.shared.mvp.base


expect abstract class CommonMvpPresenter<View : CommonMvpView>() {

    protected val view: View

    open fun attachView(view: View)

    protected open fun onFirstViewAttach()

    open fun detachView(view: View)

    open fun destroyView(view: View)

    open fun onDestroy()
}