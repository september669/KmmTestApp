package org.dda.testwork.androidApp.ui.base.groupie

import android.widget.EdgeEffect
import androidx.recyclerview.widget.RecyclerView
import com.xwray.groupie.Group
import com.xwray.groupie.GroupDataObserver
import com.xwray.groupie.GroupieViewHolder


fun RecyclerView.installBounceEdgesVertical() {
    edgeEffectFactory = makeBounceEdge(isVertical = true)
}

fun RecyclerView.installBounceEdgesHorizontal() {
    edgeEffectFactory = makeBounceEdge(isVertical = false)
}

/** The magnitude of translation distance while the list is over-scrolled. */
private const val OVERSCROLL_TRANSLATION_MAGNITUDE = 0.2f

/** The magnitude of translation distance when the list reaches the edge on fling. */
private const val FLING_TRANSLATION_MAGNITUDE = 0.5f

inline fun <reified T : GroupieItem<*, *>> RecyclerView.forEachVisibleHolder(
    action: (T) -> Unit
) {
    for (i in 0 until childCount) {
        (getChildViewHolder(getChildAt(i)) as? GroupieViewHolder)?.let { holder ->
            holder.item as? T
        }?.also { groupieItem ->
            action(groupieItem)
        }
    }
}

private fun makeBounceEdge(isVertical: Boolean): RecyclerView.EdgeEffectFactory {

    return object : RecyclerView.EdgeEffectFactory() {
        override fun createEdgeEffect(recyclerView: RecyclerView, direction: Int): EdgeEffect {
            return object : EdgeEffect(recyclerView.context) {

                override fun onPull(deltaDistance: Float) {
                    super.onPull(deltaDistance)
                    handlePull(deltaDistance)
                }

                override fun onPull(deltaDistance: Float, displacement: Float) {
                    super.onPull(deltaDistance, displacement)
                    handlePull(deltaDistance)
                }

                private fun handlePull(deltaDistance: Float) {
                    // This is called on every touch event while the list is scrolled with a finger.
                    // We simply update the view properties without animation.
                    if (isVertical) {
                        val sign = if (direction == DIRECTION_BOTTOM) -1 else 1
                        val translationYDelta = sign * recyclerView.width * deltaDistance * OVERSCROLL_TRANSLATION_MAGNITUDE
                        recyclerView.forEachVisibleHolder { holder: GroupieItem<*, *> ->
                            holder.translationY.cancel()
                            holder.itemView?.apply {
                                translationY += translationYDelta
                            }
                        }
                    } else {
                        val sign = if (direction == DIRECTION_RIGHT) -1 else 1
                        val translationYDelta = sign * recyclerView.height * deltaDistance * OVERSCROLL_TRANSLATION_MAGNITUDE
                        recyclerView.forEachVisibleHolder { holder: GroupieItem<*, *> ->
                            holder.translationX.cancel()
                            holder.itemView?.apply {
                                translationX += translationYDelta
                            }
                        }
                    }
                }

                override fun onRelease() {
                    super.onRelease()
                    // The finger is lifted. This is when we should start the animations to bring
                    // the view property values back to their resting states.
                    if (isVertical) {
                        recyclerView.forEachVisibleHolder { holder: GroupieItem<*, *> ->
                            holder.translationY.start()
                        }
                    } else {
                        recyclerView.forEachVisibleHolder { holder: GroupieItem<*, *> ->
                            holder.translationX.start()
                        }
                    }
                }

                override fun onAbsorb(velocity: Int) {
                    super.onAbsorb(velocity)
                    if (isVertical) {
                        val sign = if (direction == DIRECTION_BOTTOM) -1 else 1
                        // The list has reached the edge on fling.
                        val translationVelocity = sign * velocity * FLING_TRANSLATION_MAGNITUDE
                        recyclerView.forEachVisibleHolder { holder: GroupieItem<*, *> ->
                            holder.translationY
                                .setStartVelocity(translationVelocity)
                                .start()
                        }
                    } else {
                        val sign = if (direction == DIRECTION_RIGHT) -1 else 1
                        // The list has reached the edge on fling.
                        val translationVelocity = sign * velocity * FLING_TRANSLATION_MAGNITUDE
                        recyclerView.forEachVisibleHolder { holder: GroupieItem<*, *> ->
                            holder.translationX
                                .setStartVelocity(translationVelocity)
                                .start()
                        }

                    }
                }
            }
        }
    }
}

fun groupDataObserver(
    onChanged: ((group: Group) -> Unit)? = null,
    onItemInserted: ((group: Group, position: Int) -> Unit)? = null,
    onItemChanged: ((group: Group, position: Int) -> Unit)? = null,
    onItemPayloadChanged: ((group: Group, position: Int, payload: Any?) -> Unit)? = null,
    onItemRemoved: ((group: Group, position: Int) -> Unit)? = null,
    onItemRangeChanged: ((group: Group, positionStart: Int, itemCount: Int) -> Unit)? = null,
    onItemRangePayloadChanged: ((group: Group, positionStart: Int, itemCount: Int, payload: Any?) -> Unit)? = null,
    onItemRangeInserted: ((group: Group, positionStart: Int, itemCount: Int) -> Unit)? = null,
    onItemRangeRemoved: ((group: Group, positionStart: Int, itemCount: Int) -> Unit)? = null,
    onItemMoved: ((group: Group, fromPosition: Int, toPosition: Int) -> Unit)? = null
): GroupDataObserver {
    return object : GroupDataObserver {
        override fun onChanged(group: Group) {
            onChanged?.invoke(group)
        }

        override fun onItemInserted(group: Group, position: Int) {
            onItemInserted?.invoke(group, position)
        }

        override fun onItemChanged(group: Group, position: Int) {
            onItemChanged?.invoke(group, position)
        }

        override fun onItemChanged(group: Group, position: Int, payload: Any?) {
            onItemPayloadChanged?.invoke(group, position, payload)
        }

        override fun onItemRemoved(group: Group, position: Int) {
            onItemRemoved?.invoke(group, position)
        }

        override fun onItemRangeChanged(group: Group, positionStart: Int, itemCount: Int) {
            onItemRangeChanged?.invoke(group, positionStart, itemCount)
        }

        override fun onItemRangeChanged(group: Group, positionStart: Int, itemCount: Int, payload: Any?) {
            onItemRangePayloadChanged?.invoke(group, positionStart, itemCount, payload)
        }

        override fun onItemRangeInserted(group: Group, positionStart: Int, itemCount: Int) {
            onItemRangeInserted?.invoke(group, positionStart, itemCount)
        }

        override fun onItemRangeRemoved(group: Group, positionStart: Int, itemCount: Int) {
            onItemRangeRemoved?.invoke(group, positionStart, itemCount)
        }

        override fun onItemMoved(group: Group, fromPosition: Int, toPosition: Int) {
            onItemMoved?.invoke(group, fromPosition, toPosition)
        }

    }
}