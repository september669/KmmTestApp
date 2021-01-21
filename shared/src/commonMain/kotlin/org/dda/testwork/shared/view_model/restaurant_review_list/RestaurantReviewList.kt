package org.dda.testwork.shared.view_model.restaurant_review_list

import org.dda.ankoLogger.logDebug
import org.dda.testwork.shared.api.dto.RestaurantReview
import org.dda.testwork.shared.redux.*
import org.dda.testwork.shared.repo.RepoRestaurantReview
import org.dda.testwork.shared.utils.checkWhen
import org.dda.testwork.shared.view_model.restaurant_review_list.RestaurantReviewList.*


class RestaurantReviewListViewModel(
    private val repoDishes: RepoRestaurantReview
) : BaseReduxViewModel<State, Action, Effect, Nothing>() {

    override val exceptionHandler: ExceptionHandler
        get() = TODO("Not yet implemented")

    override val redux = initState {
        State.Init.also {
            launchUi {
                fire(Effect.OnRefresh)
            }
        }
    }.withActions { prevState, action ->
        action.reduce(prevState, this)
    }.withSideEffects { state, effect ->
        when (effect) {
            Effect.OnRefresh -> doRefresh(state)
        }.checkWhen()
    }


    private fun doRefresh(state: State) = launchUiProgress {
        logDebug { "doRefresh(${state.toLogString()})" }
        val list = repoDishes.restaurantReviews()
        fire(Action.UpdateDishList(list))
    }

}


interface RestaurantReviewList {

    sealed class State : ReduxState {
        object Init : State()
        data class Loaded(val list: List<RestaurantReview>) : State()

    }

    sealed class Action : ReduceAction<State, RestaurantReviewListViewModel> {

        data class UpdateDishList(val list: List<RestaurantReview>) : Action() {
            override fun reduce(state: State, viewModel: RestaurantReviewListViewModel): State {
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