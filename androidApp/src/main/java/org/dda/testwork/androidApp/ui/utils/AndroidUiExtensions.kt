@file:Suppress("NOTHING_TO_INLINE")

package org.dda.testwork.androidApp.ui.utils

import android.content.Context
import android.os.Bundle
import android.os.Looper
import android.util.TypedValue
import android.view.View
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import androidx.annotation.AttrRes
import androidx.annotation.ColorInt
import androidx.fragment.app.Fragment
import kotlinx.serialization.InternalSerializationApi
import kotlinx.serialization.json.Json
import kotlinx.serialization.serializer
import org.dda.ankoLogger.ankoLogger
import org.dda.ankoLogger.logError
import kotlin.reflect.KClass
import kotlin.reflect.KProperty


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

/******************    EditText                                         ***************************/

private fun createArgumentKey(thisRef: Any, property: KProperty<*>): String {
    return "${thisRef::class.java.name}.${property.name}"
}


fun <T : Any> T.bundleJson(clazz: KClass<T>, key: String): Bundle {
    val json = Json.encodeToString(serializer(clazz.java), this)
    val jsonZiped = json.gzip()
    // see https://developer.android.com/guide/components/activities/parcelables-and-bundles
    // For the specific case of savedInstanceState, the amount of data should be kept small because the system process needs
    // to hold on to the provided data for as long as the user can ever navigate back to that activity (even if the activity's
    // process is killed). We recommend that you keep saved state to less than 50k of data.
    if (jsonZiped.size > 50_000) {
        ankoLogger("bundleJson").logError("TransactionTooLargeException warning key: $key json size: ${json.toByteArray().size} gzip size: ${jsonZiped.size} json: $json")
    }
    return Bundle().apply { putByteArray(key, jsonZiped) }
}


@OptIn(InternalSerializationApi::class)
fun <T : Any> Bundle.unbundleJson(clazz: KClass<T>, key: String): T? {
    return getByteArray(key)?.unGzip()?.let { json -> Json.decodeFromString<T>(clazz.serializer(), json) }
}


class ArgumentJson<T : Any>(val clazz: KClass<T>) : kotlin.properties.ReadWriteProperty<Fragment, T> {

    private var value: T? = null
    private var isValueInit: Boolean = false

    override operator fun getValue(thisRef: Fragment, property: KProperty<*>): T {
        return if (!isValueInit) {
            isValueInit = true
            val key = createArgumentKey(thisRef, property)
            thisRef.arguments?.unbundleJson(clazz, key)?.also {
                value = it
            }!!
        } else {
            value!!
        }
    }

    override operator fun setValue(thisRef: Fragment, property: KProperty<*>, value: T) {
        this.value = value
        if (thisRef.arguments == null) {
            thisRef.arguments = Bundle()
        }
        val key = createArgumentKey(thisRef, property)
        thisRef.arguments?.putAll(value.bundleJson(clazz, key))
    }
}

fun <T : Any> argJson(clazz: KClass<T>): ArgumentJson<T> = ArgumentJson(clazz)