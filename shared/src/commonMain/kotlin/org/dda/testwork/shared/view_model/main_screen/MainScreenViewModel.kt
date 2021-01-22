package org.dda.testwork.shared.view_model.main_screen

import org.dda.ankoLogger.logError
import org.dda.testwork.shared.redux.*
import org.dda.testwork.shared.view_model.main_screen.MainState.*


class MainScreenViewModel : BaseReduxViewModel<State, Action, Effect, Screen>() {

    override val redux = initState {
        State(Screen.RestaurantList).also {
            postOneTimeAction(it.screen)
        }
    }.withActions { prevState, action ->
        action.reduce(prevState, this).also {
            postOneTimeAction(it.screen)
        }
    }.withSideEffects { state, effect ->

    }

    override val exceptionHandler = object : ExceptionHandler {
        override fun handle(exc: Throwable): Boolean {
            logError("", exc)
            return false
        }
    }

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
        RestaurantList, DishHitList, RestaurantReviewList
    }
}