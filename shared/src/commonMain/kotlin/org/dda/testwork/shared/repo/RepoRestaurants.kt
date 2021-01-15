package org.dda.testwork.shared.repo

import org.dda.ankoLogger.AnkoLogger
import org.dda.testwork.shared.api.ChibbisService
import org.dda.testwork.shared.api.dto.RestaurantItem

class RepoRestaurants(
    private val service: ChibbisService
) : AnkoLogger {

    suspend fun findRestaurantList(filter: Regex): List<RestaurantItem> {
        return service.getRestaurantList().filter { restaurantItem ->
            filter.matches(restaurantItem.name)
        }
    }

}