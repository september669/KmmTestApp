package org.dda.testwork.androidApp.ui.utils

import android.content.Context
import android.content.pm.PackageManager
import android.content.res.AssetFileDescriptor
import android.graphics.drawable.Drawable
import android.text.style.TextAppearanceSpan
import android.util.TypedValue
import androidx.annotation.*
import androidx.core.content.ContextCompat


interface AndroidContextHolder {

    val androidContext: Context

    fun @receiver:StringRes Int.stringFromId(): String = androidContext.resources.getString(this)

    fun @receiver:StringRes Int.stringFromId(vararg arg: Any): String = androidContext.resources.getString(this, *arg)

    fun @receiver:PluralsRes Int.getQuantityString(quantity: Int, vararg formatArgs: Any): String {
        return if (formatArgs.isNotEmpty()) {
            androidContext.resources.getQuantityString(this, quantity, *formatArgs)
        } else {
            androidContext.resources.getQuantityString(this, quantity, quantity)
        }
    }

    fun @receiver:RawRes Int.assetFileDescriptor(): AssetFileDescriptor =
        androidContext.resources.openRawResourceFd(this)

    @Dimension
    fun @receiver:DimenRes Int.dimenDpFromId(): Float =
        androidContext.resources.getDimension(this) / androidContext.resources.displayMetrics.density

    @Dimension
    fun @receiver:DimenRes Int.dimenPxFromId(): Float = androidContext.resources.getDimension(this)

    @Dimension
    fun @receiver:DimenRes Int.dimenIntPxFromId(): Int = androidContext.resources.getDimension(this).toInt()

    @Dimension
    fun @receiver:DimenRes Int.dimenPxSize(): Int = androidContext.resources.getDimensionPixelSize(this)

    @ColorInt
    fun @receiver:ColorRes Int.colorFromId(): Int = ContextCompat.getColor(androidContext, this)

    fun @receiver:StyleRes Int.textAppearanceSpan(): TextAppearanceSpan = TextAppearanceSpan(androidContext, this)

    @ColorInt
    fun getThemeColor(@AttrRes attrId: Int): Int {
        val ta = TypedValue()
        androidContext.theme.resolveAttribute(attrId, ta, true)
        return ta.data
    }

    fun @receiver:DrawableRes Int.getDrawableW(): Drawable = ContextCompat.getDrawable(androidContext, this)!!


    fun checkSelfPermission(permission: String, action: () -> Unit) {
        if (ContextCompat.checkSelfPermission(androidContext, permission) == PackageManager.PERMISSION_GRANTED) {
            action()
        }
    }

}