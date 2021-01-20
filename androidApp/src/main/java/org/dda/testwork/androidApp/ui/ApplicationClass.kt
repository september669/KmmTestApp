package org.dda.testwork.androidApp.ui


import android.content.Context
import androidx.multidex.MultiDex
import androidx.multidex.MultiDexApplication
import org.dda.ankoLogger.AnkoLogger
import org.dda.ankoLogger.configAnkoLogger
import org.dda.testwork.shared.BuildCfg
import org.dda.testwork.shared.di.moduleViewModel
import org.kodein.di.DI
import org.kodein.di.DIAware
import org.kodein.di.android.x.androidXModule


class ApplicationClass : MultiDexApplication(), DIAware, AnkoLogger {

    override val di by DI.lazy {
        import(androidXModule(this@ApplicationClass))
        importOnce(moduleViewModel)
    }

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