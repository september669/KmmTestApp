package org.dda.testwork.shared.network.dto

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable


//class Restaurants : ArrayList<RestaurantsItem>()

@Serializable
data class RestaurantItem(
    @SerialName("DeliveryCost")
    val deliveryCost: Int,
    @SerialName("DeliveryTime")
    val deliveryTime: Int,
    @SerialName("Logo")
    val logo: String,
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
)

@Serializable
data class Specialization(
    @SerialName("Name")
    val name: String
)