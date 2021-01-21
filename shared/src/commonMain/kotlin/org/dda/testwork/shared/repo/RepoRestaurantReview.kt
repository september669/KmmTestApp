package org.dda.testwork.shared.repo

import org.dda.ankoLogger.AnkoLogger
import org.dda.ankoLogger.logDebug
import org.dda.testwork.shared.api.ChibbisService
import org.dda.testwork.shared.api.dto.RestaurantReview

class RepoRestaurantReview(
    private val service: ChibbisService
) : AnkoLogger {

    suspend fun restaurantReviews(): List<RestaurantReview> {
        logDebug { "restaurantReviews()" }
        return service.getRestaurantReviews()
    }

}