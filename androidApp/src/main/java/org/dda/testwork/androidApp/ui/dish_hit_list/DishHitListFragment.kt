package org.dda.testwork.androidApp.ui.dish_hit_list

import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import com.xwray.groupie.Section
import kotlinx.coroutines.flow.MutableSharedFlow
import org.dda.ankoLogger.logDebug
import org.dda.testwork.androidApp.R
import org.dda.testwork.androidApp.databinding.FragmentDishHitListBinding
import org.dda.testwork.androidApp.ui.base.BaseFragmentRefreshable
import org.dda.testwork.androidApp.ui.base.groupie.groupAdapterOf
import org.dda.testwork.androidApp.ui.base.groupie.installBounceEdgesVertical
import org.dda.testwork.shared.utils.checkWhen
import org.dda.testwork.shared.view_model.dish_hit_list.DishHitList.*
import org.dda.testwork.shared.view_model.dish_hit_list.DishHitListViewModel

class DishHitListFragment :
    BaseFragmentRefreshable<
            FragmentDishHitListBinding,
            State,
            Action,
            Effect,
            OneTimeAction,
            DishHitListViewModel
            >(R.layout.fragment_dish_hit_list) {

    companion object {
        const val screenKey = "DishHitListFragment"
    }

    private val groupDishHitItem = Section()

    private val effectFlow = MutableSharedFlow<Effect>()

    override fun makeViewModelBluePrint() = viewModelBluePrint<DishHitListViewModel> { viewModel ->
        with(viewModel) {
            effectFlow.collectOnEach { effect ->
                fire(effect)
            }
        }
    }

    override fun bindView(view: View): FragmentDishHitListBinding {
        logDebug("bindView()")
        return FragmentDishHitListBinding.bind(view).also { bind ->
            with(bind.recycler) {
                layoutManager = LinearLayoutManager(requireContext())
                adapter = groupAdapterOf(
                    groupDishHitItem
                )
                installBounceEdgesVertical()
            }

        }
    }

    override fun renderContent(content: State) {
        logDebug { "renderContent(${content.toLogString()})" }

        when (content) {
            State.Init -> {
                groupDishHitItem.update(emptyList())
            }
            is State.Loaded -> {
                groupDishHitItem.update(
                    content.list.map { item ->
                        ItemDishHit(
                            owner = this,
                            payload = item,
                            effectFlow = effectFlow
                        )
                    }
                )
            }
        }.checkWhen()
    }

    override fun renderOnce(action: OneTimeAction) {
        logDebug("renderOnce($action)")
        when (action) {
            is OneTimeAction.ShowHuge -> {
                ImageDialog().apply {
                    dish = action.dish
                }.show(childFragmentManager, null)
            }
        }.checkWhen()
    }

    override fun onRetryButtonClicked() {
        logDebug("onRetryButtonClicked()")
        onRefresh()
    }

    override fun onRefresh() {
        logDebug { "onRefresh()" }
        viewModel fire Effect.OnRefresh
    }


}