package org.dda.testwork.androidApp.ui.restaurant_review_list

import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import com.xwray.groupie.Section
import org.dda.ankoLogger.logDebug
import org.dda.testwork.androidApp.R
import org.dda.testwork.androidApp.databinding.FragmentRestaurantReviewListBinding
import org.dda.testwork.androidApp.ui.base.BaseFragmentRefreshable
import org.dda.testwork.androidApp.ui.base.groupie.decorationOffset
import org.dda.testwork.androidApp.ui.base.groupie.groupAdapterOf
import org.dda.testwork.androidApp.ui.base.groupie.installBounceEdgesVertical
import org.dda.testwork.androidApp.ui.utils.dpToPx
import org.dda.testwork.shared.utils.checkWhen
import org.dda.testwork.shared.view_model.restaurant_review_list.RestaurantReviewList.*
import org.dda.testwork.shared.view_model.restaurant_review_list.RestaurantReviewListViewModel

class RestaurantReviewListFragment :
    BaseFragmentRefreshable<
            FragmentRestaurantReviewListBinding,
            State,
            Action,
            Effect,
            Nothing,
            RestaurantReviewListViewModel
            >(R.layout.fragment_restaurant_review_list) {

    companion object {
        const val screenKey = "RestaurantReviewListFragment"
    }

    private val groupReview = Section()

    override fun makeViewModelBluePrint() = viewModelBluePrint<RestaurantReviewListViewModel>()

    override fun bindView(view: View): FragmentRestaurantReviewListBinding {
        logDebug("bindView()")
        return FragmentRestaurantReviewListBinding.bind(view).also { bind ->
            with(bind.recycler) {
                layoutManager = LinearLayoutManager(requireContext())
                adapter = groupAdapterOf(
                    groupReview
                )
                addItemDecoration(
                    decorationOffset(
                        left = R.dimen.edge_margin.dimenIntPxFromId(),
                        right = R.dimen.edge_margin.dimenIntPxFromId(),
                        bottom = 15.dpToPx
                    )
                )
                installBounceEdgesVertical()
            }
        }
    }

    override fun renderContent(content: State) {
        when (content) {
            State.Init -> {
                groupReview.update(emptyList())
            }
            is State.Loaded -> {
                groupReview.update(
                    content.list.map { item ->
                        ItemRestaurantReview(
                            owner = this,
                            payload = item,
                        )
                    }
                )
            }
        }.checkWhen()
    }

    override fun onRetryButtonClicked() {
        logDebug("onRetryButtonClicked()")
        onRefresh()
    }

    override fun onRefresh() {
        logDebug("onRefresh()")
        viewModel fire Effect.OnRefresh
    }
}