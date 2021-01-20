package org.dda.testwork.shared.repo

import org.dda.ankoLogger.AnkoLogger
import org.dda.testwork.shared.api.ChibbisService
import org.dda.testwork.shared.api.dto.RestaurantItem
import org.dda.testwork.shared.utils.isNotNullOrEmptyOrBlank

class RepoRestaurants(
    private val service: ChibbisService
) : AnkoLogger {

    suspend fun findRestaurantList(filter: String): List<RestaurantItem> {
        val list = service.getRestaurantList()
        return if (filter.isNotNullOrEmptyOrBlank()) {
            list.filter { restaurantItem ->
                restaurantItem.name.contains(filter, ignoreCase = true)
            }
        } else {
            list
        }
    }

}