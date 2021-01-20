package org.dda.testwork.shared.redux

sealed class ErrorKind {
    object None : ErrorKind()
    object Generic : ErrorKind()
    object Network : ErrorKind()
    object Maintenance : ErrorKind()
    object NotFound : ErrorKind()
    data class Api(val httpStatusCode: Int, val apiErrorCode: Int) : ErrorKind()
}
