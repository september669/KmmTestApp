package org.dda.testwork.androidApp.ui.restaurant_list

import android.os.Bundle
import android.view.View
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.xwray.groupie.Section
import dev.icerock.moko.mvvm.createViewModelFactory
import org.dda.ankoLogger.logError
import org.dda.testwork.androidApp.R
import org.dda.testwork.androidApp.databinding.FragmentRestaurantListBinding
import org.dda.testwork.androidApp.ui.base.BaseFragmentRefreshable
import org.dda.testwork.androidApp.ui.base.groupie.groupAdapterOf
import org.dda.testwork.androidApp.ui.base.groupie.installBounceEdgesVertical
import org.dda.testwork.shared.utils.checkWhen
import org.dda.testwork.shared.view_model.restaurant_list.RestaurantList.*
import org.dda.testwork.shared.view_model.restaurant_list.RestaurantListPresenter
import org.kodein.di.DIAware
import org.kodein.di.android.x.closestDI
import org.kodein.di.direct
import org.kodein.di.instance


class RestaurantListFragment :
    BaseFragmentRefreshable<
            FragmentRestaurantListBinding,
            RestaurantListState,
            Action,
            Effect,
            RestaurantListPresenter
            >(R.layout.fragment_restaurant_list),
    DIAware {

    companion object {
        const val screenKey = "RestaurantListFragment"
    }

    override val di by closestDI()

    override val viewModelClass = RestaurantListPresenter::class.java

    override fun viewModelFactory(): ViewModelProvider.Factory {
        return createViewModelFactory {
            direct.instance<RestaurantListPresenter>().also { vm ->
                vm fire Action.UpdateQuery("")
            }
        }
    }

    private val groupRestaurantItem = Section()

    override fun bindView(view: View): FragmentRestaurantListBinding {
        return FragmentRestaurantListBinding.bind(view).also { bind ->

            with(bind.productCardRecycler) {
                layoutManager = LinearLayoutManager(requireContext())
                adapter = groupAdapterOf(
                    groupRestaurantItem
                )
                //isEnableDefaultChangeAnimations = false
                installBounceEdgesVertical()
            }

        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


    }

    override fun renderContent(content: RestaurantListState) {
        logError { "renderContent($content)" }

        when (content) {
            is RestaurantListState.PreRequest -> {
                groupRestaurantItem.update(emptyList())
            }
            is RestaurantListState.Loaded -> {
                groupRestaurantItem.update(
                    content.list.map { restaurantItem ->
                        ItemRestaurant(
                            owner = this,
                            payload = restaurantItem
                        )
                    }
                )
            }
        }.checkWhen()

    }


}
