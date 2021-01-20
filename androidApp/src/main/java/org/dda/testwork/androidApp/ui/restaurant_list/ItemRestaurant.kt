package org.dda.testwork.androidApp.ui.restaurant_list

import android.view.View
import com.bumptech.glide.Glide
import org.dda.testwork.androidApp.R
import org.dda.testwork.androidApp.databinding.FragmentRestaurantListItemBinding
import org.dda.testwork.androidApp.ui.base.groupie.GroupieItem
import org.dda.testwork.shared.api.dto.RestaurantItem
import org.dda.testwork.shared.coroutine_context.CoroutineExecutionContext


class ItemRestaurant(
    owner: CoroutineExecutionContext,
    payload: RestaurantItem,
) : GroupieItem<RestaurantItem, FragmentRestaurantListItemBinding>(
    owner = owner,
    payload = payload
) {

    override val itemId = ItemRestaurant::class.java.name + payload.id

    override val itemLayout = R.layout.fragment_restaurant_list_item

    override fun bind(viewBinding: FragmentRestaurantListItemBinding, position: Int) {
        Glide.with(viewBinding.logo)
            .load(payload.logoUrl)
            .placeholder(R.drawable.ic_loading)
            .into(viewBinding.logo)

    }

    override fun initializeViewBinding(view: View): FragmentRestaurantListItemBinding {
        return FragmentRestaurantListItemBinding.bind(view)
    }

}