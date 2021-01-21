package org.dda.testwork.shared.api

import io.ktor.client.*
import io.ktor.client.request.*
import io.ktor.http.*
import org.dda.testwork.shared.api.dto.Dish
import org.dda.testwork.shared.api.dto.RestaurantItem
import org.dda.testwork.shared.api.dto.RestaurantReview

class ChibbisService(
    private val baseUrl: Url,
    private val client: HttpClient
) {

    suspend fun getRestaurantList(): List<RestaurantItem> {
        return client.get(baseUrl + "restaurants")
    }

    suspend fun getDishHits(): List<Dish> {
        return client.get(baseUrl + "hits")
    }

    suspend fun getRestaurantReviews(): List<RestaurantReview> {
        return client.get(baseUrl + "reviews")
    }

}


private inline operator fun Url.plus(path: String): Url {
    return URLBuilder(this).pathPart(listOf(path)).build()
}

/**
 * Remove it after update KTOR
 * it is copyed from https://github.com/ktorio/ktor/blob/9d791b26dbf277011a67ac9df6414a8621f2c6c7/ktor-http/common/src/io/ktor/http/URLBuilder.kt
 */
private fun URLBuilder.pathPart(parts: List<String>): URLBuilder {
    var paths = parts
        .map { part ->
            part.dropWhile { it == '/' }.dropLastWhile { it == '/' }.encodeURLQueryComponent()
        }
        .filter { it.isNotEmpty() }
        .joinToString("/")

    // make sure that there's a slash separator at the end of current path
    if (!encodedPath.endsWith('/')) {
        paths = "/${paths}"
    }
    encodedPath += paths

    return this
}