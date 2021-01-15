@file:Suppress("NOTHING_TO_INLINE")

package org.dda.testwork.androidApp.ui.utils

import android.os.Looper


/******************   Some stuff                                       ***************************/

fun isMainThread(): Boolean {
    return Looper.getMainLooper().thread == Thread.currentThread()
}

fun checkIsMainThread(lazyMessage: (() -> Any)? = null) {
    check(isMainThread(), lazyMessage ?: { "checkIsMainThread failed" })
}
