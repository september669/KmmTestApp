package org.dda.testwork.shared.mvp.base

import moxy.MvpView
import org.dda.testwork.shared.coroutine_context.ExecutionProgress

actual interface CommonMvpView : MvpView {

    actual fun showProgress(show: Boolean, progress: ExecutionProgress)

}
