package org.dda.testwork.shared.mvp.base

import moxy.MvpPresenter


actual abstract class CommonMvpPresenter<View : CommonMvpView> : MvpPresenter<View>() {

    protected actual val view: View get() = super.getViewState()

    actual override fun attachView(view: View) = super.attachView(view)

    actual override fun onFirstViewAttach() = super.onFirstViewAttach()

    actual override fun detachView(view: View) = super.detachView(view)

    actual override fun destroyView(view: View) = super.destroyView(view)

    actual override fun onDestroy() = super.onDestroy()


}