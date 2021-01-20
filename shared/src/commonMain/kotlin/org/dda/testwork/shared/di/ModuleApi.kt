package org.dda.testwork.shared.di

import io.ktor.client.*
import io.ktor.http.*
import org.dda.ankoLogger.logDebug
import org.dda.testwork.shared.BuildCfg
import org.dda.testwork.shared.api.ChibbisService
import org.dda.testwork.shared.api.chibbisClient
import org.kodein.di.DI
import org.kodein.di.bind
import org.kodein.di.instance
import org.kodein.di.singleton


const val moduleApiName = "ChibbisApi"

val moduleApi = DI.Module(name = moduleApiName) {

    bind<HttpClient>() with singleton {
        diLogger.logDebug("bind<HttpClient>()")
        chibbisClient()
    }

    bind<ChibbisService>() with singleton {
        diLogger.logDebug("bind<ChibbisService>()")
        ChibbisService(
            baseUrl = Url(BuildCfg.API.baseUrl),
            client = instance()
        )
    }

}