package com.nhinguyen.sample.utils

import android.app.Activity
import android.content.Context
import android.graphics.Rect
import android.os.Bundle
import android.os.Handler
import android.os.ResultReceiver
import android.util.Log
import android.view.View
import android.view.ViewGroup
import android.view.ViewTreeObserver.OnGlobalLayoutListener
import android.view.Window
import android.view.WindowManager
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import android.widget.FrameLayout

class KeyboardUtils private constructor(
    act: Activity,
    private val isKeyboardVisible: (isVisible: Boolean) -> Unit
) : OnGlobalLayoutListener {
    private var prevValue: Boolean? = null
    private val mRootView: View = (act.findViewById<View>(android.R.id.content) as ViewGroup).getChildAt(0)
    private var mScreenDensity = 1f

    override fun onGlobalLayout() {
        val r = Rect()
        mRootView.getWindowVisibleDisplayFrame(r)
        val heightDiff = mRootView.rootView.height - (r.bottom - r.top)
        val dp = heightDiff / mScreenDensity
        val isVisible = dp > MAGIC_NUMBER
        if ((prevValue == null || isVisible != prevValue)) {
            isKeyboardVisible.invoke(isVisible)
        }
    }

    init {
        mRootView.viewTreeObserver.addOnGlobalLayoutListener(this)
        mScreenDensity = act.resources.displayMetrics.density
    }

    interface OnSoftInputChangedListener {
        fun onSoftInputChanged(height: Int)
    }

    companion object {
        private const val MAGIC_NUMBER = 200

        fun addKeyboardToggleListener(act: Activity, isKeyboardVisible: (isVisible: Boolean) -> Unit) {
            KeyboardUtils(act, isKeyboardVisible)
        }

        fun forceShowSoftInput(activity: Activity) {
            if (!isSoftInputVisible(activity)) {
                toggleSoftInput(activity)
            }
        }

        fun showSoftInput(view: View) {
            if (view.requestFocus()) {
                val imm =
                    view.context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
                imm.showSoftInput(view, InputMethodManager.SHOW_IMPLICIT)
            }
        }

        @JvmOverloads
        fun forceShowSoftInput(view: View, flags: Int = 0) {
            val imm =
                view.context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
                    ?: return
            view.isFocusable = true
            view.isFocusableInTouchMode = true
            view.requestFocus()
            imm.showSoftInput(view, flags, object : ResultReceiver(Handler()) {
                override fun onReceiveResult(resultCode: Int, resultData: Bundle) {
                    if (resultCode == InputMethodManager.RESULT_UNCHANGED_HIDDEN || resultCode == InputMethodManager.RESULT_HIDDEN) {
                        toggleSoftInput(view.context)
                    }
                }
            })
            imm.toggleSoftInput(
                InputMethodManager.SHOW_FORCED,
                InputMethodManager.HIDE_IMPLICIT_ONLY
            )
        }

        fun hideSoftInput(activity: Activity) {
            var view = activity.currentFocus
            if (view == null) {
                val decorView = activity.window.decorView
                val focusView = decorView.findViewWithTag<View>("keyboardTagView")
                if (focusView == null) {
                    view = EditText(activity)
                    view.tag = "keyboardTagView"
                    (decorView as ViewGroup).addView(view, 0, 0)
                } else {
                    view = focusView
                }
                view.requestFocus()
            }
            hideSoftInput(view)
        }

        fun hideSoftInput(view: View) {
            val imm =
                view.context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
                    ?: return
            imm.hideSoftInputFromWindow(view.windowToken, 0)
        }

        fun toggleSoftInput(context: Context) {
            val imm = context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            imm.toggleSoftInput(0, 0)
        }

        private var sDecorViewDelta = 0

        fun isSoftInputVisible(activity: Activity): Boolean {
            return getDecorViewInvisibleHeight(activity.window) > 0
        }

        private fun getDecorViewInvisibleHeight(window: Window): Int {
            val decorView = window.decorView ?: return 0
            val outRect = Rect()
            decorView.getWindowVisibleDisplayFrame(outRect)
            Log.d(
                "KeyboardUtils",
                "getDecorViewInvisibleHeight: " + (decorView.bottom - outRect.bottom)
            )
            val delta = Math.abs(decorView.bottom - outRect.bottom)
            if (delta <= getNavBarHeight(window.context) + getStatusBarHeight(window.context)) {
                sDecorViewDelta = delta
                return 0
            }
            return delta - sDecorViewDelta
        }

        fun registerSoftInputChangedListener(
            activity: Activity,
            listener: OnSoftInputChangedListener
        ) {
            registerSoftInputChangedListener(activity.window, listener)
        }

        fun registerSoftInputChangedListener(
            window: Window,
            listener: OnSoftInputChangedListener
        ) {
            val flags = window.attributes.flags
            if (flags and WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS != 0) {
                window.clearFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS)
            }
            val contentView = window.findViewById<FrameLayout>(android.R.id.content)
            val decorViewInvisibleHeightPre = intArrayOf(getDecorViewInvisibleHeight(window))
            contentView.viewTreeObserver
                .addOnGlobalLayoutListener {
                    val height = getDecorViewInvisibleHeight(window)
                    if (decorViewInvisibleHeightPre[0] != height) {
                        listener.onSoftInputChanged(height)
                        decorViewInvisibleHeightPre[0] = height
                    }
                }
        }

        private fun getStatusBarHeight(context: Context): Int {
            val resources = context.resources
            val resourceId = resources.getIdentifier("status_bar_height", "dimen", "android")
            return resources.getDimensionPixelSize(resourceId)
        }

        private fun getNavBarHeight(context: Context): Int {
            val res = context.resources
            val resourceId = res.getIdentifier("navigation_bar_height", "dimen", "android")
            return if (resourceId != 0) {
                res.getDimensionPixelSize(resourceId)
            } else {
                0
            }
        }
    }
}
