package org.dda.testwork.shared.api.dto

import kotlinx.serialization.SerialName


//class Restaurants : ArrayList<RestaurantsItem>()

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
}

@Serializable
data class Specialization(
    @SerialName("Name")
    val name: String
)