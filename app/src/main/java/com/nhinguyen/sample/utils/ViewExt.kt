package com.nhinguyen.sample.utils

import android.app.Activity
import android.os.SystemClock
import android.text.Editable
import android.text.TextWatcher
import android.text.method.HideReturnsTransformationMethod
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import androidx.appcompat.app.AlertDialog
import androidx.core.view.children

/**
 * Extension function to simplify setting an afterTextChanged action to EditText components.
 */
fun EditText.afterTextChanged(afterTextChanged: (String) -> Unit) {
    this.addTextChangedListener(object : TextWatcher {
        override fun afterTextChanged(editable: Editable?) {
            afterTextChanged.invoke(editable.toString())
        }

        override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {}

        override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {}
    })
}

fun EditText.isShowingPasswordText(): Boolean {
    return transformationMethod is HideReturnsTransformationMethod
}

fun ViewGroup.viewsRecursive(): List<View> {
    val views = this.children.toList()
    return views.flatMap {
        when (it) {
            is ViewGroup -> {
                val listViews: MutableList<View> = it.viewsRecursive().toMutableList()
                listViews.add(it as View)
                listViews
            }

            else -> listOf(it)
        }
    }
}

fun View.click(debounceTime: Long = 1000L, action: (view: View) -> Unit) {
    this.setOnClickListener(object : View.OnClickListener {
        private var lastClickTime: Long = 0
        override fun onClick(v: View) {
            if (SystemClock.elapsedRealtime() - lastClickTime < debounceTime) {
                lastClickTime = SystemClock.elapsedRealtime()
                return
            }
            action(v)
            lastClickTime = SystemClock.elapsedRealtime()
        }
    })
}

fun View.margin(
    left: Int? = null,
    top: Int? = null,
    right: Int? = null,
    bottom: Int? = null
) {
    layoutParams<ViewGroup.MarginLayoutParams> {
        left?.run { leftMargin = 0 }
        top?.run { topMargin = this }
        right?.run { rightMargin = 0 }
        bottom?.run { bottomMargin = 0 }
    }
}

inline fun <reified T : ViewGroup.LayoutParams> View.layoutParams(block: T.() -> Unit) {
    if (layoutParams is T) block(layoutParams as T)
}

fun AlertDialog.hideNavigationBottomWhenDialogShowing(activity: Activity) {
    window?.decorView?.systemUiVisibility =
        activity.window.decorView.systemUiVisibility
}