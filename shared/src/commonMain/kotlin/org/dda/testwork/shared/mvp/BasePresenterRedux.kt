package org.dda.testwork.shared.mvp

import kotlinx.atomicfu.atomic
import org.dda.ankoLogger.AnkoLogger
import org.dda.ankoLogger.logDebug
import org.dda.testwork.shared.mvp.base.BasePresenter
import org.dda.testwork.shared.mvp.redux.*


/***
 *
 *   View --(Action)--> Presenter --(Action)--> Redux.reducer{(Action)->State} --(State)--> Dispatcher --(State)--> View
 *   View --(Effect)--> Presenter --(Effect)--> Redux.effector
 *
 */


abstract class BasePresenterRedux<
        State : ReduxState,
        Action : ReduxAction,
        Effect : ReduxSideEffect,
        View : BaseViewRedux<State>
        > : BasePresenter<View>() {

    protected abstract val redux: ReduxStore<State, Action, Effect>

    protected fun initState(block: () -> State) = BuilderState(block)

    protected fun BuilderState<State>.withActions(reducer: ReduxReducer<State, Action>) =
        BuilderReducer(this@withActions, reducer)

    protected fun BuilderReducer<State, Action>.withSideEffects(block: ReduxEffector<State, Effect>): ReduxStore<State, Action, Effect> =
        BuilderSideEffects(this, view, block).build()

    fun fire(vararg action: Action) {
        logDebug { "fire(${action.joinToString("; ")})" }
        redux.dispatch(*action)
    }

    infix fun fire(action: Action) {
        logDebug { "fire($action)" }
        redux.dispatch(action)
    }

    infix fun fire(effect: Effect) {
        logDebug { "fireEffect($effect)" }
        redux.dispatchSideEffect(effect)
    }
}

open class BuilderState<State : ReduxState>(protected val state: () -> State) {
    constructor(state: BuilderState<State>) : this(state.state)
}

open class BuilderReducer<State : ReduxState, Action : ReduxAction>(
    state: BuilderState<State>,
    protected val reducer: ReduxReducer<State, Action>
) : BuilderState<State>(state) {
    constructor(reducer: BuilderReducer<State, Action>) : this(
        BuilderState(reducer.state),
        reducer.reducer
    )

}

class BuilderSideEffects<State : ReduxState, Action : ReduxAction, Effect : ReduxSideEffect>(
    reducer: BuilderReducer<State, Action>,
    val view: BaseViewRedux<State>,
    var sideEffects: ReduxEffector<State, Effect>
) : BuilderReducer<State, Action>(reducer) {
    fun build() = ReduxStore(
        initState = state(),
        dispatcher = DispatcherUi(view),
        reducer = reducer,
        sideEffect = sideEffects
    )
}

private class DispatcherUi<State : ReduxState>(val view: BaseViewRedux<State>) :
    Dispatcher<State>(),
    AnkoLogger {

    val isFirstDispatch = atomic(true)

    override fun dispatch(prevState: State, newState: State) {
        if (newState != prevState || isFirstDispatch.value) {
            isFirstDispatch.value = false
            view.setUiState(newState.asUiStateShowContent())
        } else {
            logDebug { "skip dispatch(${prevState.toLogString()}, ${newState.toLogString()})" }
        }
    }
}
