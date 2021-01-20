package org.dda.testwork.androidApp.ui.restaurant_list

import android.view.View
import androidx.core.view.isVisible
import androidx.core.widget.doAfterTextChanged
import androidx.recyclerview.widget.LinearLayoutManager
import com.xwray.groupie.Section
import kotlinx.coroutines.delay
import org.dda.ankoLogger.logDebug
import org.dda.ankoLogger.logError
import org.dda.testwork.androidApp.R
import org.dda.testwork.androidApp.databinding.FragmentRestaurantListBinding
import org.dda.testwork.androidApp.ui.base.BaseFragmentRefreshable
import org.dda.testwork.androidApp.ui.base.groupie.groupAdapterOf
import org.dda.testwork.androidApp.ui.base.groupie.installBounceEdgesVertical
import org.dda.testwork.androidApp.ui.utils.setOnActionDone
import org.dda.testwork.androidApp.ui.utils.setTextIfDiffer
import org.dda.testwork.shared.utils.checkWhen
import org.dda.testwork.shared.utils.isNotNullOrEmptyOrBlank
import org.dda.testwork.shared.view_model.restaurant_list.RestaurantList.*
import org.dda.testwork.shared.view_model.restaurant_list.RestaurantListViewModel


class RestaurantListFragment :
    BaseFragmentRefreshable<
            FragmentRestaurantListBinding,
            State,
            Action,
            Effect,
            RestaurantListViewModel
            >(R.layout.fragment_restaurant_list) {

    companion object {
        const val screenKey = "RestaurantListFragment"
    }

    override fun makeViewModelBluePrint() = viewModelBluePrint<RestaurantListViewModel>()

    private val groupRestaurantItem = Section()

    override fun bindView(view: View): FragmentRestaurantListBinding {
        return FragmentRestaurantListBinding.bind(view).also { bind ->

            setSwipeableChildren(R.id.recycler)

            with(bind.recycler) {
                layoutManager = LinearLayoutManager(requireContext())
                adapter = groupAdapterOf(
                    groupRestaurantItem
                )
                //isEnableDefaultChangeAnimations = false
                installBounceEdgesVertical()
            }

            bind.searchInput.setOnActionDone { text ->
                viewModel fire Action.UpdateQuery(text)
            }
            bind.searchInput.doAfterTextChanged {
                bind.searchClear.isVisible = it.isNotNullOrEmptyOrBlank()
            }
            bind.searchClear.setOnClickListener {
                viewModel fire Action.UpdateQuery(query = "")
            }
        }
    }

    override fun onRetryButtonClicked() {
        TODO("Not yet implemented")
    }

    override fun onRefresh() {
        logDebug("onRefresh()")
        viewModel fire Action.UpdateQuery(binding.searchInput.text.toString())
    }

    override fun renderContent(content: State) {
        logError { "renderContent($content)" }

        binding.searchInput.setTextIfDiffer(content.query)

        when (content) {
            is State.PreRequest -> {
                groupRestaurantItem.update(emptyList())
            }
            is State.Loaded -> {
                groupRestaurantItem.update(
                    content.list.map { restaurantItem ->
                        ItemRestaurant(
                            owner = this,
                            payload = restaurantItem
                        )
                    }
                )

                if (content.isNeedScrollToTop) {
                    launchUi {
                        delay(200)
                        if (isResumed) {
                            binding.recycler.smoothScrollToPosition(0)
                        }
                        viewModel fire Action.DisableScrollToTop
                    }
                }

                Unit
            }
        }.checkWhen()

    }


}
