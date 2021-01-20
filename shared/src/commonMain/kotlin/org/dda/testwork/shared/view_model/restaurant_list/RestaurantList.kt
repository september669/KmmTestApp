package org.dda.testwork.shared.view_model.restaurant_list

import org.dda.ankoLogger.logDebug
import org.dda.testwork.shared.api.dto.RestaurantItem
import org.dda.testwork.shared.redux.*
import org.dda.testwork.shared.repo.RepoRestaurants
import org.dda.testwork.shared.utils.checkWhen
import org.dda.testwork.shared.view_model.restaurant_list.RestaurantList.*


class RestaurantListViewModel(
    private val repoRestaurants: RepoRestaurants
) : BaseReduxViewModel<State, Action, Effect>() {

    override val redux = initState {
        State.PreRequest("").also {
            launchUi {
                fire(Action.UpdateQuery(""))
            }
        }
    }.withActions { prevState, action ->
        action.reduce(prevState, this)
    }.withSideEffects { state, effect ->
        when (effect) {
            Effect.ApplyFilter -> doOnApplyFilter(state)
        }.checkWhen()
    }

    override val exceptionHandler: ExceptionHandler
        get() = TODO("Not yet implemented")


    private fun doOnApplyFilter(state: State) = launchUiProgress {
        logDebug { "doOnApplyFilter(${state.toLogString()})" }
        val list = repoRestaurants.findRestaurantList(state.query)
        fire(
            Action.UpdateRestaurantList(
                query = state.query,
                list = list
            )
        )
    }
}

interface RestaurantList {

    sealed class State(
        open val query: String
    ) : ReduxState {

        data class PreRequest(override val query: String) : State(query)

        data class Loaded(
            override val query: String,
            val list: List<RestaurantItem>,
            val isNeedScrollToTop: Boolean = false
        ) : State(query)

    }

    sealed class Action : ReduceAction<State, RestaurantListViewModel> {

        data class UpdateQuery(val query: String) : Action() {

            override fun reduce(state: State, viewModel: RestaurantListViewModel): State {
                return when (state) {
                    is State.PreRequest -> state.copy(query = query)
                    is State.Loaded -> state.copy(query = query)
                }.also {
                    viewModel.launchUi {
                        viewModel fire Effect.ApplyFilter
                    }
                }
            }

        }

        data class UpdateRestaurantList(val query: String, val list: List<RestaurantItem>) : Action() {

            override fun reduce(state: State, viewModel: RestaurantListViewModel): State {
                return when (state) {
                    is State.PreRequest -> State.Loaded(query = query, list = list, isNeedScrollToTop = true)
                    is State.Loaded -> state.copy(query = query, list = list, isNeedScrollToTop = true)
                }
            }

        }

        object DisableScrollToTop : Action() {
            override fun reduce(state: State, viewModel: RestaurantListViewModel): State {
                return when (state) {
                    is State.PreRequest -> state
                    is State.Loaded -> state.copy(isNeedScrollToTop = false)
                }
            }

        }
    }

    sealed class Effect : ReduxSideEffect {
        object ApplyFilter : Effect()
    }
}

