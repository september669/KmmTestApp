package org.dda.testwork.shared

import org.dda.ankoLogger.AnkoLogger


class Greeting: AnkoLogger {
    fun greeting(): String {
        return "Hello, ${Platform().platform}!"
    }
}
