package org.dda.testwork.androidApp.ui

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import io.ktor.http.*
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import org.dda.ankoLogger.AnkoLogger
import org.dda.ankoLogger.logDebug
import org.dda.ankoLogger.logError
import org.dda.testwork.androidApp.R
import org.dda.testwork.androidApp.ui.utils.isMainThread
import org.dda.testwork.shared.BuildCfg
import org.dda.testwork.shared.network.ChibbisService
import org.dda.testwork.shared.network.chibbisClient


class ActivitySplashScreen : AppCompatActivity(), AnkoLogger {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash_screen)

        val client = chibbisClient()
        val baseUrl = Url(BuildCfg.API.baseUrl)

        val service = ChibbisService(baseUrl, client)

        GlobalScope.launch {
            val list = service.getRestaurantList()
            logError { "isMain: ${isMainThread()}" }
            list.forEach { restaurantItem ->
                logDebug { "restaurantItem: $restaurantItem" }
            }
        }

        logDebug { "onCreate" }
    }
}