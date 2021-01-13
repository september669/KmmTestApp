package org.dda.testwork.androidApp.ui


import android.app.Application
import android.content.Context
import androidx.multidex.MultiDex
import org.dda.ankoLogger.AnkoLogger
import org.dda.ankoLogger.configAnkoLogger
import org.dda.testwork.shared.BuildCfg


class ApplicationClass : Application(), AnkoLogger {

    override fun attachBaseContext(base: Context) {
        super.attachBaseContext(base)
        MultiDex.install(this)
    }

    override fun onCreate() {
        super.onCreate()
        initLogger()
    }

    private fun initLogger() {
        configAnkoLogger(
            applicationTag = BuildCfg.loggingAppTag
        )
    }
}