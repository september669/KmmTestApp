package org.dda.testwork.shared.mvp.redux

import org.dda.ankoLogger.AnkoLogger
import org.dda.ankoLogger.logDebug

class ReduxStore<State : ReduxState, Action : ReduxAction, SideEffect : ReduxSideEffect>(
    initState: State,
    val dispatcher: Dispatcher<State>,
    val reducer: ReduxReducer<State, Action>,
    val sideEffect: ReduxEffector<State, SideEffect>
) : AnkoLogger {

    private var _state: State = initState

    val state: State get() = _state

    init {
        dispatcher.dispatch(prevState = state, state = _state)
    }

    fun dispatch(vararg action: Action): State {
        logDebug { "dispatch($action)" }
        var newSate: State? = null
        action.forEach { actionItem ->
            reducer.invoke(newSate ?: _state, actionItem).apply {
                dispatcher.dispatch(state, this)
                _state = this
                newSate = this
            }
        }

        return newSate ?: _state
    }

    fun dispatchSideEffect(effect: SideEffect) {
        logDebug { "dispatchSideEffect($effect)" }
        sideEffect.invoke(state, effect)
    }
}

abstract class Dispatcher<State : ReduxState> {
    abstract fun dispatch(prevState: State, state: State)
}

fun ReduxState.illegalState(action: ReduxAction) =
    IllegalStateException("call action: $action when state: $this")

fun ReduxState.illegalState(effect: ReduxSideEffect) =
    IllegalStateException("call effect: $effect when state: $this")

fun ReduxState.illegalState(message: String) = IllegalStateException("$message when state: $this")

interface ReduxState : AnkoLogger

interface ReduxAction : AnkoLogger

interface ReduxSideEffect : AnkoLogger

typealias ReduxReducer<State, Action> = (prevState: State, action: Action) -> State

typealias ReduxEffector<State, SideEffect> = (state: State, effect: SideEffect) -> Unit