package org.dda.testwork.androidApp.ui.utils

import android.view.View
import android.widget.*
import androidx.annotation.StringRes
import androidx.core.view.isInvisible
import androidx.core.view.isVisible
import com.google.android.material.textfield.TextInputLayout
import kotlin.math.min

fun ViewAnimator.setDisplayedChildIfDiffer(whichChild: Int) {
    if (displayedChild != whichChild) displayedChild = whichChild
}

fun ViewFlipper.setDisplayedChildIfDiffer(child: View) {
    val index = indexOfChild(child)
    if (displayedChild != index) {
        displayedChild = index
    }
}

fun View.setVisibleIfDiffer(value: Boolean) {
    if (isVisible != value) {
        isVisible = value
    }
}

fun List<View>.setVisibleIfDiffer(value: Boolean) {
    forEach {
        it.setVisibleIfDiffer(value)
    }
}

fun View.setVisibleIfDiffer(visibility: Int) {
    if (getVisibility() != visibility) {
        setVisibility(visibility)
    }
}

fun View.setInvisibleIfDiffer(value: Boolean) {
    if (isInvisible != value) {
        isInvisible = value
    }
}

fun TextInputLayout.setErrorIfDiffer(value: String?, also: TextInputLayout.() -> Unit = {}) {
    if (error != value) {
        error = value
        also()
    }
}

fun TextInputLayout.setErrorIfDiffer(@StringRes strId: Int, also: TextInputLayout.() -> Unit = {}) {
    val value = context.resources.getString(strId)
    setErrorIfDiffer(value, also)
}

fun TextView.setHintIfDiffer(value: String?, also: () -> Unit = {}) {
    if (hint != value) {
        hint = value
        also()
    }
}

fun TextView.setHintIfDiffer(@StringRes strId: Int, also: () -> Unit = {}) {
    val value = context.resources.getString(strId)
    setHintIfDiffer(value, also)
}

fun TextView.setTextIfDiffer(
    valueIn: String?,
    thisTextValue: CharSequence? = this.text,
    useTrim: Boolean = true,
    tryRespectCursor: Boolean = true,
    also: (TextView.() -> Unit)? = null
): Boolean {

    // W/A TextView don't storing null
    val value = valueIn ?: ""

    val isDiffer = if (useTrim) {
        thisTextValue?.toString()?.trim() != value.trim()
    } else {
        thisTextValue?.toString() != value
    }
    if (isDiffer) {
        val cursorEnd0 = selectionEnd

        this.text = value

        if (tryRespectCursor && this is EditText) {
            setSelection(
                min(cursorEnd0, this.text?.length ?: Int.MAX_VALUE)
            )
        }

        also?.invoke(this)
    }
    return isDiffer
}

fun TextView.setTextIfDiffer(
    @StringRes strId: Int,
    useTrim: Boolean = true,
    tryRespectCursor: Boolean = true,
    also: (TextView.() -> Unit)? = null
): Boolean {
    val value = context.resources.getString(strId)
    return setTextIfDiffer(
        valueIn = value,
        useTrim = useTrim,
        tryRespectCursor = tryRespectCursor,
        also = also
    )
}

fun TextView.setTextIfDiffer(value: CharSequence?, hideIfNull: Boolean) {
    setTextIfDiffer(value.toString())
    if (hideIfNull) {
        isVisible = value != null
    }
}

fun TextView.setInputTypeIfDiffer(value: Int) {
    if (inputType != value) inputType = value
}

fun CompoundButton.setCheckedfIfDiffer(checked: Boolean) {
    if (isChecked != checked) isChecked = checked
}