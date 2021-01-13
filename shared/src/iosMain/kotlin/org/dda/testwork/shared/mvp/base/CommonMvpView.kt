package org.dda.testwork.shared.mvp.base

import org.dda.testwork.shared.coroutine_context.ExecutionProgress

actual interface CommonMvpView {
    actual fun showProgress(show: Boolean, progress: ExecutionProgress)
}
