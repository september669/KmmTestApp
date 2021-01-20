package org.dda.testwork.androidApp.ui.restaurant_list

import android.annotation.SuppressLint
import android.view.View
import androidx.core.view.isVisible
import com.bumptech.glide.Glide
import org.dda.testwork.androidApp.R
import org.dda.testwork.androidApp.databinding.FragmentRestaurantListItemBinding
import org.dda.testwork.androidApp.ui.base.groupie.GroupieItem
import org.dda.testwork.androidApp.ui.utils.formatOrInt
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

    @SuppressLint("SetTextI18n")
    override fun bind(viewBinding: FragmentRestaurantListItemBinding, position: Int) {

        viewBinding.name.text = payload.name
        viewBinding.specializations.text = payload.specializations.joinToString(separator = " / ") { it.name }
        viewBinding.likeText.isVisible = payload.likeFactor > 0.01
        viewBinding.likeIcon.isVisible = viewBinding.likeText.isVisible
        viewBinding.likeText.text = "${(payload.likeFactor * 100).formatOrInt(digits = 0)}%"

        Glide.with(viewBinding.logo)
            .load(payload.logoUrl)
            .placeholder(R.drawable.ic_loading)
            .into(viewBinding.logo)

    }

    override fun initializeViewBinding(view: View): FragmentRestaurantListItemBinding = FragmentRestaurantListItemBinding.bind(view)

}