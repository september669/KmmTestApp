package org.dda.testwork.shared.mvp

interface ExceptionHandler {
    /**
     * @param exc error to handle
     * @return 'true' if exception is known and it's handled
     */
    fun handle(exc: Throwable, view: CommonMvpView): Boolean
}