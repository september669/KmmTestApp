package org.dda.testwork.shared.mvp.base

import org.dda.testwork.shared.coroutine_context.ExecutionProgress

expect interface CommonMvpView {

    fun showProgress(show: Boolean, progress: ExecutionProgress)

}
