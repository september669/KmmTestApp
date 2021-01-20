package org.dda.testwork.shared.view_model.dish_hit_list

import org.dda.ankoLogger.logDebug
import org.dda.testwork.shared.api.dto.Dish
import org.dda.testwork.shared.redux.*
import org.dda.testwork.shared.repo.RepoDishes
import org.dda.testwork.shared.utils.checkWhen
import org.dda.testwork.shared.view_model.dish_hit_list.DishHitList.*


class DishHitListViewModel(
    private val repoDishes: RepoDishes
) : BaseReduxViewModel<State, Action, Effect>() {

    override val exceptionHandler: ExceptionHandler
        get() = TODO("Not yet implemented")

    override val redux = initState {
        State.Init
    }.withActions { prevState, action ->
        action.reduce(prevState, this)
    }.withSideEffects { state, effect ->
        when (effect) {
            Effect.OnRefresh -> doRefresh(state)
        }.checkWhen()
    }

    private fun doRefresh(state: State) = launchUiProgress {
        logDebug { "doRefresh(${state.toLogString()})" }
        val dishList = repoDishes.dishHitList()
        fire(Action.UpdateDishList(dishList))
    }

}

interface DishHitList {

    sealed class State : ReduxState {
        object Init : State()
        data class Loaded(val list: List<Dish>) : State()
    }

    sealed class Action : ReduceAction<State, DishHitListViewModel> {
        data class UpdateDishList(val list: List<Dish>) : Action() {

            override fun reduce(state: State, viewModel: DishHitListViewModel): State {
                return when (state) {
                    State.Init -> State.Loaded(list)
                    is State.Loaded -> state.copy(list = list)
                }
            }

        }
    }

    sealed class Effect : ReduxSideEffect {
        object OnRefresh : Effect()
    }
}