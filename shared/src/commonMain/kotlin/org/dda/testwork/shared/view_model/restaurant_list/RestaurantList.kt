package org.dda.testwork.shared.view_model.restaurant_list

import org.dda.testwork.shared.api.dto.RestaurantItem
import org.dda.testwork.shared.redux.*
import org.dda.testwork.shared.repo.RepoRestaurants
import org.dda.testwork.shared.view_model.restaurant_list.RestaurantList.*


class RestaurantListPresenter(
    private val repoRestaurants: RepoRestaurants
) : BaseReduxViewModel<RestaurantListState, Action, Effect>() {

    override val redux = initState {
        RestaurantListState.PreRequest("")
    }.withActions { prevState, action ->
        action.reduce(prevState, this)
    }.withSideEffects { state, effect ->
        when (effect) {
            Effect.ApplyFilter -> doOnApplyFilter(state)
        }.checkWhen()
    }

    override val exceptionHandler: ExceptionHandler
        get() = TODO("Not yet implemented")


    private fun doOnApplyFilter(state: RestaurantListState) = launchUiProgress {
        logDebug { "doOnApplyFilter(${state.query})" }
        val list = repoRestaurants.findRestaurantList(state.query)
        fire(
            Action.UpdateRestaurantList(
                query = state.query,
                list = list
            )
        )
    }
}

interface RestaurantList{
    sealed class RestaurantListState(
        open val query: String
    ) : ReduxState {

        data class PreRequest(override val query: String) : RestaurantListState(query)

        data class Loaded(override val query: String, val list: List<RestaurantItem>) : RestaurantListState(query)

    }

    sealed class Action : ReduxAction {
        abstract fun reduce(state: RestaurantListState, presenter: RestaurantListPresenter): RestaurantListState

        data class UpdateQuery(val query: String) : Action() {

            override fun reduce(state: RestaurantListState, presenter: RestaurantListPresenter): RestaurantListState {
                return when (state) {
                    is RestaurantListState.PreRequest -> state.copy(query = query)
                    is RestaurantListState.Loaded -> RestaurantListState.PreRequest(query = query)
                }.also {
                    presenter fire Effect.ApplyFilter
                }
            }

        }

        data class UpdateRestaurantList(val query: String, val list: List<RestaurantItem>) : Action() {

            override fun reduce(state: RestaurantListState, presenter: RestaurantListPresenter): RestaurantListState {
                return when (state) {
                    is RestaurantListState.PreRequest -> RestaurantListState.Loaded(query = query, list = list)
                    is RestaurantListState.Loaded -> state.copy(query = query, list = list)
                }
            }

        }
    }

    sealed class Effect : ReduxSideEffect {
        object ApplyFilter : Effect()
    }
}

