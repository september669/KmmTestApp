package org.dda.testwork.shared.api.dto


import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class Dish(
    @SerialName("ProductDescription")
    val productDescription: String,
    @SerialName("ProductImage")
    val productImage: String,
    @SerialName("ProductName")
    val productName: String,
    @SerialName("ProductPrice")
    val productPrice: Int,
    @SerialName("RestaurantId")
    val restaurantId: Int,
    @SerialName("RestaurantLogo")
    val restaurantLogo: String,
    @SerialName("RestaurantName")
    val restaurantName: String
) {
    val id: String by lazy {
        restaurantId.toString() +
                productName +
                productPrice
    }
}