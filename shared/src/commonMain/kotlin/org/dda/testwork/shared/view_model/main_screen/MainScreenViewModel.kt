package org.dda.testwork.shared.view_model.main_screen

import org.dda.testwork.shared.redux.*
import org.dda.testwork.shared.view_model.main_screen.MainState.*


class MainScreenViewModel : BaseReduxViewModel<State, Action, Effect>() {

    override val redux = initState {
        State.RestaurantList
    }.withActions { prevState, action ->
        prevState
    }.withSideEffects { state, effect ->

    }

    override val exceptionHandler: ExceptionHandler
        get() = TODO("Not yet implemented")

}

interface MainState {
    sealed class State : ReduxState {
        object RestaurantList : State()

        override fun toLogString(): String {
            return when (this) {
                RestaurantList -> this::class.simpleName!!
            }
        }
    }

    class Action : ReduxAction
    class Effect : ReduxSideEffect
}