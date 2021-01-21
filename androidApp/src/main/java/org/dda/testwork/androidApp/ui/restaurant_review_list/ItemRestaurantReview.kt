package org.dda.testwork.androidApp.ui.restaurant_review_list

import android.view.View
import kotlinx.datetime.LocalDateTime
import org.dda.testwork.androidApp.R
import org.dda.testwork.androidApp.databinding.FragmentRestaurantReviewListItemBinding
import org.dda.testwork.androidApp.ui.base.groupie.GroupieItem
import org.dda.testwork.shared.api.dto.RestaurantReview
import org.dda.testwork.shared.coroutine_context.CoroutineExecutionContext

class ItemRestaurantReview(
    owner: CoroutineExecutionContext,
    payload: RestaurantReview,
) : GroupieItem<RestaurantReview, FragmentRestaurantReviewListItemBinding>(
    owner = owner,
    payload = payload
) {

    override val itemId = ItemRestaurantReview::class.java.name + payload.id

    override val itemLayout = R.layout.fragment_restaurant_review_list_item

    override fun bind(viewBinding: FragmentRestaurantReviewListItemBinding, position: Int) {

        viewBinding.likeIcon.setImageResource(
            if (payload.isPositive) {
                R.drawable.ic_thumb_up
            } else {
                R.drawable.ic_thumb_down
            }
        )

        viewBinding.title.text = R.string.user_about_fmt.stringFromId(payload.userFIO, payload.restaurantName)
        viewBinding.review.text = payload.message
        viewBinding.date.text = payload.dateAdded.toLclString()


    }

    override fun initializeViewBinding(view: View) = FragmentRestaurantReviewListItemBinding.bind(view)


    fun LocalDateTime.toLclString(): String {
        return "$dayOfMonth.$monthNumber.$year $hour:$minute"
    }
}