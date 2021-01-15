@file:Suppress("NOTHING_TO_INLINE")

package org.dda.testwork.androidApp.ui.utils

import android.content.Context
import android.os.Looper
import android.util.TypedValue
import androidx.annotation.AttrRes
import androidx.annotation.ColorInt


/******************   Some stuff                                       ***************************/

fun isMainThread(): Boolean {
    return Looper.getMainLooper().thread == Thread.currentThread()
}

fun checkIsMainThread(lazyMessage: (() -> Any)? = null) {
    check(isMainThread(), lazyMessage ?: { "checkIsMainThread failed" })
}

/******************                                                    ***************************/

@ColorInt
fun Context.getThemeColor(@AttrRes attrId: Int): Int {
    val ta = TypedValue()
    theme.resolveAttribute(attrId, ta, true)
    return ta.data
}
