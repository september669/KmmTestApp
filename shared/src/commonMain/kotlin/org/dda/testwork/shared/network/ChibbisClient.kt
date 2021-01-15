package org.dda.testwork.shared.network

import io.ktor.client.*
import io.ktor.client.engine.cio.*
import io.ktor.client.features.json.*
import io.ktor.client.features.json.serializer.*

fun chibbisClient(): HttpClient {
    return HttpClient(CIO) {
        install(JsonFeature) {
            val json = kotlinx.serialization.json.Json { ignoreUnknownKeys = true }
            serializer = KotlinxSerializer(json)
        }
        engine {
            //threadsCount = 4
            //pipelining = true
        }
    }
}