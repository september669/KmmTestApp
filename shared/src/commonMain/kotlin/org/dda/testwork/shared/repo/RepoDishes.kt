package org.dda.testwork.shared.repo

import org.dda.ankoLogger.AnkoLogger
import org.dda.ankoLogger.logDebug
import org.dda.testwork.shared.api.ChibbisService
import org.dda.testwork.shared.api.dto.Dish


class RepoDishes(
    private val service: ChibbisService
) : AnkoLogger {

    suspend fun dishHitList(): List<Dish> {
        logDebug { "dishHitList()" }
        return service.getDishHits()
    }

}