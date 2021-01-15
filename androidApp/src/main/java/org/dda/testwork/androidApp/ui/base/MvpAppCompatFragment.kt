package org.dda.testwork.androidApp.ui.base

import android.os.Bundle
import androidx.annotation.ContentView
import androidx.annotation.LayoutRes
import androidx.fragment.app.Fragment
import moxy.MvpDelegate
import moxy.MvpDelegateHolder
import java.lang.reflect.Field

open class MvpAppCompatFragment :Fragment,  MvpDelegateHolder {

    private var mvpDelegate: MvpDelegate<out MvpAppCompatFragment>? = null

    protected open val isAutoMvpAttach = true

    constructor() : super()

    @ContentView
    constructor(@LayoutRes contentLayoutId: Int) : super(contentLayoutId)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        getMvpDelegate().onCreate(savedInstanceState)
    }

    protected fun doMvpAttach() {
        getMvpDelegate().onAttach()
    }

    protected fun doMvpDetach() {
        getMvpDelegate().onDetach()
    }

    protected fun doMvpDestroyView() {
        getMvpDelegate().onDetach()
        getMvpDelegate().onDestroyView()
    }

    override fun onStart() {
        super.onStart()
        if (isAutoMvpAttach) {
            doMvpAttach()
        }
    }

    override fun onResume() {
        super.onResume()
        if (isAutoMvpAttach) {
            doMvpAttach()
        }
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        getMvpDelegate().onSaveInstanceState(outState)
        getMvpDelegate().onDetach()
    }

    override fun onStop() {
        super.onStop()
        doMvpDetach()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        doMvpDestroyView()
    }

    override fun onDestroy() {
        super.onDestroy()

        //We leave the screen and respectively all fragments will be destroyed
        if (requireActivity().isFinishing) {
            getMvpDelegate().onDestroy()
            return
        }

        // When we rotate device isRemoving() return true for fragment placed in backstack
        // http://stackoverflow.com/questions/34649126/fragment-back-stack-and-isremoving
        if (activity?.isChangingConfigurations == true) {
            return
        }

        var anyParentIsRemoving = false
        var parent = parentFragment
        while (!anyParentIsRemoving && parent != null) {
            anyParentIsRemoving = parent.isRemoving
            parent = parent.parentFragment
        }

        if (!isInBackStackAndroidX()) {
            getMvpDelegate().onDestroy()
        }
    }

    override fun getMvpDelegate(): MvpDelegate<*> {
        if (mvpDelegate == null) {
            mvpDelegate = MvpDelegate(this)
        }

        return mvpDelegate as MvpDelegate<out MvpAppCompatFragment>
    }
}

fun Fragment.isInBackStackAndroidX(): Boolean {
/*
    // see https://github.com/sockeqwe/mosby/blob/6edf7f3674013b57bde3fb11ae139a30680286e3/utils-fragment/src/main/java/android/support/v4/app/BackstackAccessor.java
    // and https://github.com/sockeqwe/mosby/blob/master/mvi/src/main/java/com/hannesdorfmann/mosby3/FragmentMviDelegateImpl.java
    // and this https://github.com/grandcentrix/ThirtyInch/issues/206
    val writer = StringWriter()
    dump("", null, PrintWriter(writer), null)
    val dump: String = writer.toString()
    return !dump.contains("mBackStackNesting=0")
*/

    //  see https://github.com/grandcentrix/ThirtyInch/pull/205/commits/aaea0a2994dda83876d360fef593a8c8a4df0639
    return try {
        val backStackNestingField: Field = Fragment::class.java.getDeclaredField("mBackStackNesting")
        backStackNestingField.isAccessible = true
        val backStackNesting: Int = backStackNestingField.getInt(this)
        backStackNesting > 0
    } catch (e: NoSuchFieldException) {
        throw RuntimeException(e)
    } catch (e: IllegalAccessException) {
        throw RuntimeException(e)
    }
}

