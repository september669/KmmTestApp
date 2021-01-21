package org.dda.testwork.androidApp.ui.dish_hit_list

import android.view.View
import com.bumptech.glide.Glide
import org.dda.testwork.androidApp.R
import org.dda.testwork.androidApp.databinding.DialogImageEnlargeBinding
import org.dda.testwork.androidApp.ui.base.dialog.BaseRoundedDialogFragment
import org.dda.testwork.androidApp.ui.utils.argJson
import org.dda.testwork.shared.api.dto.Dish


class ImageDialog : BaseRoundedDialogFragment<DialogImageEnlargeBinding>(R.layout.dialog_image_enlarge) {

    var dish by argJson(Dish::class)

    override fun bindView(view: View): DialogImageEnlargeBinding {
        return DialogImageEnlargeBinding.bind(view).also { bind ->
            Glide.with(this)
                .load(dish.productImage)
                .placeholder(R.drawable.ic_loading)
                .into(bind.image)

        }
    }
}