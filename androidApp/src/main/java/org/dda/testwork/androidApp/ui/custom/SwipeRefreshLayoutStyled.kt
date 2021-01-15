package org.dda.testwork.androidApp.ui.custom

import android.content.Context
import android.util.AttributeSet
import android.view.View
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import org.dda.testwork.androidApp.R
import org.dda.testwork.androidApp.ui.utils.getThemeColor

class SwipeRefreshLayoutStyled : SwipeRefreshLayout {
    private var swipeableChildren = emptyList<View>()

    constructor(context: Context) : super(context) {
        init(context)
    }

    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs) {
        init(context)
    }

    private fun init(context: Context) {
        setColorSchemeColors(context.getThemeColor(R.attr.colorAccent))
    }

    fun setSwipeableChildren(vararg ids: Int) {
        swipeableChildren = ids.map { findViewById<View>(it) }.toList()
    }

    override fun canChildScrollUp(): Boolean {
        return swipeableChildren.any {
            it.isShown && it.canScrollVertically(-1)
        }
    }

    fun clearRefreshState() {
        isRefreshing = false

        @Suppress("DEPRECATION")
        destroyDrawingCache()

        clearAnimation()
    }
}
