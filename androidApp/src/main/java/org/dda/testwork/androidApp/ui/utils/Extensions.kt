package org.dda.testwork.androidApp.ui.utils

import java.lang.ref.WeakReference
import java.math.BigDecimal
import java.text.DecimalFormat
import java.text.NumberFormat
import kotlin.math.absoluteValue
import kotlin.properties.ReadWriteProperty
import kotlin.reflect.KProperty


inline fun <reified T : Any> Any.applyTyped(block: T.() -> Unit): T? {
    return if (this is T) {
        block(this)
        this
    } else {
        null
    }
}

/***************************************/

class WeakRef<T>(obj: T? = null) : ReadWriteProperty<Any?, T?> {

    private var wref: WeakReference<T>?

    init {
        this.wref = obj?.let { WeakReference(it) }
    }

    override fun getValue(thisRef: Any?, property: KProperty<*>): T? {
        return wref?.get()
    }

    override fun setValue(thisRef: Any?, property: KProperty<*>, value: T?) {
        wref = value?.let { WeakReference(it) }
    }
}

fun <T> weak(obj: T? = null) = WeakRef(obj)

fun <T> T.weak() = WeakReference(this)


/***************************************/

fun BigDecimal.formatOrInt(
    tolerance: Double = 0.001,
    digits: Int = 2,
    groupingSeparator: Char? = null
): String = toDouble().formatOrInt(
    tolerance = tolerance,
    digits = digits,
    groupingSeparator = groupingSeparator
)

fun Float.formatOrInt(
    tolerance: Double = 0.001,
    digits: Int = 2,
    groupingSeparator: Char? = null
): String = toDouble().formatOrInt(
    tolerance = tolerance,
    digits = digits,
    groupingSeparator = groupingSeparator
)

fun Double.formatOrInt(
    tolerance: Double = 0.001,
    digits: Int = 2,
    groupingSeparator: Char? = null
): String {
    fun NumberFormat.applyGroupingSeparator(): NumberFormat {
        applyTyped<DecimalFormat> {
            if (groupingSeparator != null) {
                isGroupingUsed = true
                decimalFormatSymbols = decimalFormatSymbols.apply {
                    this@apply.groupingSeparator = groupingSeparator
                }
            } else {
                isGroupingUsed = false
            }
        }
        return this
    }

    val intValue = this.toInt()
    return if ((this - intValue.toDouble()).absoluteValue < tolerance) {
        NumberFormat.getIntegerInstance()
            .applyGroupingSeparator()
            .format(this)
    } else {
        DecimalFormat.getInstance().apply {
            maximumFractionDigits = digits
            minimumFractionDigits = 0
            applyGroupingSeparator()
        }.format(this)
    }
}