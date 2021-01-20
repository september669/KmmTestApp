package org.dda.testwork.androidApp.ui.dish_hit_list

import android.view.View
import com.bumptech.glide.Glide
import org.dda.testwork.androidApp.R
import org.dda.testwork.androidApp.databinding.FragmentDishHitListItemBinding
import org.dda.testwork.androidApp.ui.base.groupie.GroupieItem
import org.dda.testwork.shared.api.dto.Dish
import org.dda.testwork.shared.coroutine_context.CoroutineExecutionContext

class ItemDishHit(
    owner: CoroutineExecutionContext,
    payload: Dish,
) : GroupieItem<Dish, FragmentDishHitListItemBinding>(
    owner = owner,
    payload = payload
) {

    override val itemId = ItemDishHit::class.java.name + payload.id

    override val itemLayout = R.layout.fragment_dish_hit_list_item

    override fun bind(viewBinding: FragmentDishHitListItemBinding, position: Int) {

        viewBinding.productText.text = payload.productName

        Glide.with(viewBinding.productImage)
            .load(payload.productImage)
            .placeholder(R.drawable.ic_loading)
            .into(viewBinding.productImage)

    }

    override fun initializeViewBinding(view: View) = FragmentDishHitListItemBinding.bind(view)
}