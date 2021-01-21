package org.dda.testwork.androidApp.ui.base.dialog

import android.content.Context
import android.content.res.Configuration
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Build
import android.os.Bundle
import android.view.*
import androidx.annotation.DrawableRes
import androidx.fragment.app.DialogFragment
import androidx.viewbinding.ViewBinding
import com.github.florent37.kotlin.pleaseanimate.please
//import com.github.florent37.kotlin.pleaseanimate.please
import org.dda.ankoLogger.AnkoLogger
import org.dda.ankoLogger.logDebug
import org.dda.testwork.androidApp.R
import org.dda.testwork.androidApp.ui.utils.AndroidContextHolder
import kotlin.random.Random

abstract class BaseRoundedDialogFragment<VB : ViewBinding>(val layoutId: Int) :
    DialogFragment(), AnkoLogger, AndroidContextHolder {

    @DrawableRes
    open val dialogBackground = R.drawable.background_dialog_rounded

    open val widthPercentPortrait = 0.8f

    open val widthPercentLandscape = 0.4f

    open val enterFrom: Int? = null // @see Gravity.LEFT

    open val animationEnterDuration = 500L

    override val androidContext: Context
        get() = requireContext()

    private var _binding: VB? = null
    protected open val binding: VB
        get() = _binding!!

    protected abstract fun bindView(view: View): VB

    private val animationEnterFrom
        get() = enterFrom.let { enterFromLcl ->
            if (enterFromLcl != null && enterFromLcl in listOf(
                    Gravity.LEFT,
                    Gravity.RIGHT,
                    Gravity.TOP,
                    Gravity.BOTTOM
                )
            ) {
                enterFromLcl
            } else if (Random.nextBoolean()) {
                Gravity.LEFT
            } else {
                Gravity.RIGHT
            }
        }


    init {
        setStyle(DialogFragment.STYLE_NO_FRAME, R.style.TransparentDialogTheme)
    }

    private fun setTransparent() {
        dialog?.window?.apply {
            logDebug("setStyleTransparent()")
            setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))// not working, see W/A in init block
            requestFeature(Window.FEATURE_NO_TITLE)
            attributes?.windowAnimations = -1
        }
    }


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        setTransparent()
        return inflater.inflate(layoutId, container, false).also { rootView ->
            _binding = bindView(rootView)
        }
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            //TODO dda: check
            view.clipToOutline = true
        }
        view.setBackgroundResource(dialogBackground)
        super.onViewCreated(view, savedInstanceState)
        // set transparency for animation
        dialog?.window?.decorView?.alpha = 0.0f
    }

    override fun onStart() {
        super.onStart()
        dialog?.window?.decorView?.let { decorView ->

            please(duration = 0) {
                animate(decorView) {
                    scale(0.001f, 0.001f)
                }
            }.withEndAction {
                decorView.alpha = 0.2f
            }.thenCouldYou(duration = (animationEnterDuration * 0.7).toLong()) {
                animate(decorView) {
                    originalScale()
                }
            }.thenCouldYou(duration = animationEnterDuration) {
                animate(decorView) {
                    alpha(1f)
                    withEndAction { decorView.alpha = 1f }
                }
            }.start()

        }
    }

    override fun onResume() {
        super.onResume()
        val widthPercent = when (resources.configuration.orientation) {
            Configuration.ORIENTATION_PORTRAIT -> widthPercentPortrait
            Configuration.ORIENTATION_LANDSCAPE -> widthPercentLandscape
            else -> widthPercentPortrait
        }
        if (widthPercent > 0f) {
            // set default dialog width
            val widthPixels = activity?.resources?.displayMetrics?.widthPixels
            if (widthPixels != null) {
                val params = dialog!!.window!!.attributes
                params.width = (widthPixels * widthPercent).toInt() //ViewGroup.LayoutParams.MATCH_PARENT
                dialog!!.window!!.attributes = params as WindowManager.LayoutParams
            }
        }
    }
}