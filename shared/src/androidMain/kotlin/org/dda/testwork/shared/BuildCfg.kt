package org.dda.testwork.shared


actual object BuildCfg {

    actual val isDebug = BuildConfig.DEBUG

    actual val loggingAppTag: String = BuildConfig.LOGGING_TAG

    actual object API {
        actual val baseUrl: String = BuildConfig.API_URL
    }

}