package org.dda.testwork.shared.mvp.base


actual abstract class CommonMvpPresenter<View : CommonMvpView> {

    protected actual val view: View
        get() = TODO("Not yet implemented")

    actual open fun attachView(view: View) {}

    protected actual open fun onFirstViewAttach() {}

    actual open fun detachView(view: View) {}

    actual open fun destroyView(view: View) {}

    actual open fun onDestroy() {}
}