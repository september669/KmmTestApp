package org.dda.testwork.shared


actual object BuildCfg {

    actual val isDebug: Boolean = kotlin.native.Platform.isDebugBinary

    actual val loggingAppTag: String = ""

    actual object API {
        actual val baseUrl: String = ""
    }

}