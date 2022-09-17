package com.nhinguyen.sample.utils

import android.content.Context
import android.content.res.Resources
import android.util.DisplayMetrics
import android.view.WindowManager

class ScreenUtils {
    companion object {

        fun getScreenWidth(context: Context): Int {
            val windowManager = context.getSystemService(Context.WINDOW_SERVICE) as WindowManager
            val dm = DisplayMetrics()
            windowManager.defaultDisplay.getMetrics(dm)
            return dm.widthPixels
        }

        fun getScreenHeight(context: Context): Int {
            val windowManager = context.getSystemService(Context.WINDOW_SERVICE) as WindowManager
            val dm = DisplayMetrics()
            windowManager.defaultDisplay.getMetrics(dm)
            return dm.heightPixels
        }

        fun dpToPx(dp: Int): Float {
            return dp * Resources.getSystem().displayMetrics.density
        }

        fun pxToDp(px: Int): Float {
            return px / Resources.getSystem().displayMetrics.density
        }
    }
}