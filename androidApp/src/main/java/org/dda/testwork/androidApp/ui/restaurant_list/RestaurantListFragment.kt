package org.dda.testwork.androidApp.ui.restaurant_list

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import by.kirich1409.viewbindingdelegate.viewBinding
import org.dda.testwork.androidApp.R
import org.dda.testwork.androidApp.databinding.FragmentRestaurantListBinding
import org.dda.testwork.androidApp.databinding.FragmentRestaurantListBinding.inflate
import org.dda.testwork.androidApp.ui.base.BaseFragmentRefreshable
import org.dda.testwork.shared.mvp.redux.ReduxState

class RestaurantListFragment : BaseFragmentRefreshable<RestaurantListState>(R.layout.fragment_restaurant_list) {
    companion object {
        const val screenKey = "RestaurantListFragment"
    }


    val binding by viewBinding(FragmentRestaurantListBinding::bind)

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        inflate(inflater, container, false)
        return super.onCreateView(inflater, container, savedInstanceState)
    }

    override fun renderContent(content: RestaurantListState) {
        TODO("Not yet implemented")
    }

}


class RestaurantListState : ReduxState {

}