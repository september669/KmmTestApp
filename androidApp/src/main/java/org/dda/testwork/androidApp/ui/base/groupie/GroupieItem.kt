package org.dda.testwork.androidApp.ui.base.groupie

import android.content.Context
import android.view.View
import androidx.annotation.LayoutRes
import androidx.dynamicanimation.animation.SpringAnimation
import androidx.dynamicanimation.animation.SpringForce
import androidx.viewbinding.ViewBinding
import com.xwray.groupie.*
import com.xwray.groupie.viewbinding.BindableItem
import com.xwray.groupie.viewbinding.GroupieViewHolder
import org.dda.ankoLogger.AnkoLogger
import org.dda.testwork.androidApp.ui.utils.AndroidContextHolder
import org.dda.testwork.androidApp.ui.utils.weak
import org.dda.testwork.shared.coroutine_context.CoroutineExecutionContext
import java.util.*
import java.util.concurrent.ConcurrentHashMap

private class IdMapper {
    private var uniqueId = 0L + 42L
    private val weak = WeakHashMap<Any, MutableMap<String, Long>>()

    fun get(owner: Any, id: String): Long? {
        val idMap = weak[owner] ?: synchronized(uniqueId) {
            weak[owner] ?: ConcurrentHashMap<String, Long>().also {
                weak[owner] = it
            }
        }
        return idMap[id] ?: synchronized(uniqueId) {
            idMap[id] ?: (uniqueId++).also {
                idMap[id] = it
            }
        }
    }
}

private val idMapper = IdMapper()


abstract class GroupieItem<Payload, VB : ViewBinding>(
    owner: CoroutineExecutionContext,
    val payload: Payload
) : BindableItem<VB>(), AnkoLogger, AndroidContextHolder {

    private var contextHolder by weak<Context>()

    protected val owner by weak(owner)

    override val androidContext get() = contextHolder!!

    protected var viewHolder by weak<GroupieViewHolder<VB>>()

    val itemView: View? get() = viewHolder?.itemView

    abstract val itemId: String

    @get:LayoutRes
    abstract val itemLayout: Int

    final override fun getLayout() = itemLayout

    final override fun getId(): Long = idMapper.get(owner!!, itemId)!!

    final override fun hasSameContentAs(other: Item<*>): Boolean {
        return if (other !is GroupieItem<*, *>) {
            false
        } else {
            payload == other.payload
        }
    }

    override fun createViewHolder(itemView: View): com.xwray.groupie.viewbinding.GroupieViewHolder<VB> {
        return super.createViewHolder(itemView).also {
            this.viewHolder = it
        }
    }

    final override fun bind(
        viewHolder: GroupieViewHolder<VB>,
        position: Int,
        payloads: MutableList<Any>,
        onItemClickListener: OnItemClickListener?,
        onItemLongClickListener: OnItemLongClickListener?
    ) {
        this.viewHolder = viewHolder
        contextHolder = viewHolder.itemView.context
        super.bind(viewHolder, position, payloads, onItemClickListener, onItemLongClickListener)
    }


    override fun bind(viewHolder: GroupieViewHolder<VB>, position: Int, payloads: MutableList<Any>) {
        this.viewHolder = viewHolder
        contextHolder = viewHolder.itemView.context
        if (payloads.isEmpty()) {
            // if payloads.isNotEmpty() then getChangePayload is overloaded
            super.bind(viewHolder, position, payloads)
        }
    }

    override fun getChangePayload(newItem: com.xwray.groupie.Item<*>): Any? {
        //  override it if you need diff for update ViewHolder correctly and prevent some blinks
        return super.getChangePayload(newItem)
    }

    //////////////////////////////////

    /**
     * A [SpringAnimation] for this RecyclerView item. This animation used for bounce effect
     */
    private val translationYMap = WeakHashMap<View, SpringAnimation>()
    val translationY: SpringAnimation
        get() = itemView!!.let { itemViewLcl ->
            translationYMap[itemViewLcl] ?: SpringAnimation(itemViewLcl, SpringAnimation.TRANSLATION_Y)
                .setSpring(
                    SpringForce()
                        .setFinalPosition(0f)
                        .setDampingRatio(SpringForce.DAMPING_RATIO_MEDIUM_BOUNCY)
                        .setStiffness(SpringForce.STIFFNESS_LOW)
                ).also {
                    translationYMap[itemViewLcl] = it
                }
        }
    private val translationXMap = WeakHashMap<View, SpringAnimation>()
    val translationX: SpringAnimation
        get() = itemView!!.let { itemViewLcl ->
            translationXMap[itemViewLcl] ?: SpringAnimation(itemViewLcl, SpringAnimation.TRANSLATION_X)
                .setSpring(
                    SpringForce()
                        .setFinalPosition(0f)
                        .setDampingRatio(SpringForce.DAMPING_RATIO_MEDIUM_BOUNCY)
                        .setStiffness(SpringForce.STIFFNESS_LOW)
                ).also {
                    translationXMap[itemViewLcl] = it
                }
        }
}

fun GroupieItem<*, *>.asSection(): Section = Section(this)
fun List<GroupieItem<*, *>>.asSection(): Section = Section(this)

fun <Payload, VB : ViewBinding> sectionOf(vararg element: GroupieItem<Payload, VB>): Section = Section(listOf(*element))

fun <Payload, VB : ViewBinding> groupAdapterOf(vararg element: GroupieItem<Payload, VB>): GroupAdapter<GroupieViewHolder<VB>> {
    return GroupAdapter<GroupieViewHolder<VB>>().apply {
        setHasStableIds(true)
        update(listOf(sectionOf(*element)))
    }
}

fun groupAdapterOf(vararg element: Group): GroupAdapter<GroupieViewHolder<*>> {
    return GroupAdapter<GroupieViewHolder<*>>().apply {
        setHasStableIds(true)
        update(listOf(*element))
    }
}