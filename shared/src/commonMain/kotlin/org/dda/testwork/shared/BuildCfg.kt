package org.dda.testwork.shared


expect object BuildCfg {

    val isDebug: Boolean

    val loggingAppTag: String

    object API {
        val baseUrl: String
    }
}