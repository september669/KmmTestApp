package org.dda.testwork.shared.mvp

import org.dda.testwork.shared.mvp.base.CommonMvpView
import org.dda.testwork.shared.mvp.redux.ReduxState
import org.dda.testwork.shared.utils.checkWhen


interface BaseViewRedux<State : ReduxState> : CommonMvpView {

    fun setUiState(state: ViewState<State, ErrorKind>) {
        when (state) {
            is ViewState.ShowContent -> renderContent(state.content)
            is ViewState.ShowError -> renderError(state.errorKind)
        }.checkWhen()
    }

    fun renderContent(content: State)

    fun renderError(errorKind: ErrorKind)

}


fun <C : Any> C.asUiStateShowContent() = ViewState.ShowContent(this)
fun ErrorKind.asUiState() = ViewState.ShowError(this)

sealed class ViewState<out C, out E : ErrorKind> {

    open class ShowContent<C>(open val content: C) : ViewState<C, Nothing>()
    open class ShowError<E : ErrorKind>(val errorKind: E) : ViewState<Nothing, E>()

    override fun toString(): String {
        return when (this) {
            is ShowContent -> content.toString()
            is ShowError -> errorKind.toString()
        }
    }
}