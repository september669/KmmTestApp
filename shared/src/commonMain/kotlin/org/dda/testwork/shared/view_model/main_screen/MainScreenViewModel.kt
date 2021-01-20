package org.dda.testwork.shared.view_model.main_screen

import org.dda.testwork.shared.redux.*
import org.dda.testwork.shared.view_model.main_screen.MainState.*


class MainScreenViewModel : BaseReduxViewModel<State, Action, Effect>() {

    override val redux = initState {
        State(Screen.RestaurantList)
    }.withActions { prevState, action ->
        action.reduce(prevState, this)
    }.withSideEffects { state, effect ->

    }

    override val exceptionHandler: ExceptionHandler
        get() = TODO("Not yet implemented")

}

interface MainState {

    data class State(
        val screen: Screen
    ) : ReduxState


    sealed class Action : ReduceAction<State, MainScreenViewModel> {
        data class ChangeScreen(val screen: Screen) : Action() {
            override fun reduce(state: State, viewModel: MainScreenViewModel): State {
                return state.copy(screen = screen)
            }
        }
    }

    class Effect : ReduxSideEffect

    enum class Screen {
        RestaurantList, DishHitList
    }
}