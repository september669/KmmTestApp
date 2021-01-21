package org.dda.testwork.androidApp.ui.base.groupie

import android.graphics.Rect
import android.view.View
import androidx.annotation.Px
import androidx.recyclerview.widget.RecyclerView


data class Offset(
    @Px val left: Int = 0,
    @Px val top: Int = 0,
    @Px val right: Int = 0,
    @Px val bottom: Int = 0
) {
    constructor(@Px offset: Int) : this(offset, offset, offset, offset)
}

abstract class DecorationOffset : RecyclerView.ItemDecoration() {

    abstract fun offsetFor(view: View, recycler: RecyclerView): Offset

    override fun getItemOffsets(
        outRect: Rect,
        view: View,
        parent: RecyclerView,
        state: RecyclerView.State
    ) {
        val offset = offsetFor(view, parent)
        outRect.set(offset.left, offset.top, offset.right, offset.bottom)
    }

}

fun decorationOffset(
    @Px left: Int = 0,
    @Px top: Int = 0,
    @Px right: Int = 0,
    @Px bottom: Int = 0
): DecorationOffset = object : DecorationOffset() {
    override fun offsetFor(view: View, recycler: RecyclerView): Offset {
        return Offset(left = left, top = top, right = right, bottom = bottom)
    }
}

fun decorationOffset(
    block: (position: Int, previousType: Int?, viewTypeCurrent: Int?, nextType: Int?) -> Offset
): DecorationOffset = object : DecorationOffset() {
    override fun offsetFor(view: View, recycler: RecyclerView): Offset {
        val position = recycler.getChildAdapterPosition(view)
        val posPrev = position - 1
        val posNext = position + 1
        val viewType = if (position != RecyclerView.NO_POSITION) recycler.adapter?.getItemViewType(position) else null
        val previousType: Int? = if (posPrev > -1 && posPrev != RecyclerView.NO_POSITION) {
            recycler.adapter?.getItemViewType(position - 1)
        } else {
            null
        }
        val nextType: Int? = if (posNext < recycler.adapter?.itemCount ?: -1 && posNext != RecyclerView.NO_POSITION) {
            recycler.adapter?.getItemViewType(posNext)
        } else {
            null
        }
        return block(position, previousType, viewType, nextType)
    }
}