package org.dda.testwork.shared.mvp.redux

import org.dda.ankoLogger.AnkoLogger


/***
 *
 *   View --(Action)--> Presenter --(Action)--> Redux.reducer{(Action)->State} --(State)--> Dispatcher --(State)--> View
 *   View --(Effect)--> Presenter --(Effect)--> Redux.effector
 *
 */
interface BaseViewRedux<State : ReduxState>
/*


fun <State : ReduxState> initState(block: () -> State) = BuilderState(block)

fun <State : ReduxState, Action : ReduxAction> BuilderState<State>.withActions(reducer: ReduxReducer<State, Action>) =
    BuilderReducer(this@withActions, reducer)

fun <State : ReduxState,
        Action : ReduxAction,
        Effect : ReduxSideEffect>
        BuilderReducer<State, Action>.withSideEffects(block: ReduxEffector<State, Effect>): ReduxStore<State, Action, Effect> =
    BuilderSideEffects(this, viewState, block).build()

interface BasePresenterRedux<
        State : ReduxState,
        Action : ReduxAction,
        Effect : ReduxSideEffect,
        View : BaseViewRedux<State>
        > : AnkoLogger {

    protected abstract val redux: ReduxStore<State, Action, Effect>

    protected fun initState(block: () -> State) = BuilderState(block)

    protected fun BuilderState<State>.withActions(reducer: ReduxReducer<State, Action>) =
        BuilderReducer(this@withActions, reducer)

    protected fun BuilderReducer<State, Action>.withSideEffects(block: ReduxEffector<State, Effect>): ReduxStore<State, Action, Effect> =
        BuilderSideEffects(this, viewState, block).build()

    fun fire(vararg action: Action) {
        debug { "fire(${action.joinToString("; ")})" }
        redux.dispatch(*action)
    }

    infix fun fire(action: Action) {
        debug { "fire($action)" }
        redux.dispatch(action)
    }

    infix fun fire(effect: Effect) {
        debug { "fireEffect($effect)" }
        redux.dispatchSideEffect(effect)
    }
}

private class DispatcherUi<State : ReduxState>(val view: BaseViewRedux<State>) :
    Dispatcher<State>(), AnkoLogger {

    var isFirstDispatch = true

    override fun dispatch(prevState: State, state: State) {
        checkIsMainThread()
        if (state != prevState || isFirstDispatch) {
            isFirstDispatch = false
            view.setUiState(state.asUiStateShowContent())
        } else {
            debug { "skip dispatch($prevState, $state)" }
        }
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
}*/
