@file:Suppress("NOTHING_TO_INLINE")

package org.dda.testwork.androidApp.ui.utils

import android.content.Context
import android.os.Looper
import android.util.TypedValue
import android.view.View
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import androidx.annotation.AttrRes
import androidx.annotation.ColorInt


/******************   Some stuff                                       ***************************/

fun isMainThread(): Boolean {
    return Looper.getMainLooper().thread == Thread.currentThread()
}

fun checkIsMainThread(lazyMessage: (() -> Any)? = null) {
    check(isMainThread(), lazyMessage ?: { "checkIsMainThread failed" })
}

fun View.hideSoftwareKeyboard() {
    val imm = context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
    imm.hideSoftInputFromWindow(windowToken, InputMethodManager.HIDE_NOT_ALWAYS)
}


/******************                                                    ***************************/

@ColorInt
fun Context.getThemeColor(@AttrRes attrId: Int): Int {
    val ta = TypedValue()
    theme.resolveAttribute(attrId, ta, true)
    return ta.data
}


/******************    EditText                                         ***************************/

fun EditText.setOnActionDone(block: (text: String) -> Unit) {
    setOnEditorActionListener { v, actionId, _ ->
        when (actionId) {
            EditorInfo.IME_ACTION_DONE -> {
                v.hideSoftwareKeyboard()
                block(text.toString())
                true
            }
            else -> false
        }
    }
}
