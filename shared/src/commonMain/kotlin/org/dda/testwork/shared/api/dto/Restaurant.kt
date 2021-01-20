package org.dda.testwork.shared.api.dto

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable


@Serializable
data class RestaurantItem(
    @SerialName("DeliveryCost")
    val deliveryCost: Int,
    @SerialName("DeliveryTime")
    val deliveryTime: Int,
    @SerialName("Logo")
    val logoUrl: String,
    @SerialName("MinCost")
    val minCost: Int,
    @SerialName("Name")
    val name: String,
    @SerialName("PositiveReviews")
    val positiveReviews: Int,
    @SerialName("ReviewsCount")
    val reviewsCount: Int,
    @SerialName("Specializations")
    val specializations: List<Specialization>
) {
    val id: String get() = name

    val likeFactor: Float by lazy {
        if (reviewsCount > 0 && positiveReviews >= 0 && positiveReviews <= reviewsCount) {
            positiveReviews / reviewsCount.toFloat()
        } else {
            -1f
        }
    }
}

@Serializable
data class Specialization(
    @SerialName("Name")
    val name: String
)