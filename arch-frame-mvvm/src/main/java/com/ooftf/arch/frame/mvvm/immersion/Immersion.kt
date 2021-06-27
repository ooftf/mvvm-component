package com.ooftf.arch.frame.mvvm.immersion

import android.app.Activity
import android.graphics.Color
import android.os.Build
import android.util.DisplayMetrics
import android.view.*
import android.widget.FrameLayout
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.view.ViewCompat
import androidx.core.view.WindowCompat
import com.blankj.utilcode.util.KeyboardUtils
import com.ooftf.arch.frame.mvvm.R
import com.ooftf.basic.AppHolder
import com.ooftf.basic.utils.setPaddingBottom
import com.ooftf.basic.utils.setPaddingTop

/**
 *
 * @author ooftf
 * @email 994749769@qq.com
 * @date 2021/6/26
 */
object Immersion {
    val statusBarHeight: Int by lazy {
        AppHolder.app.resources.getDimensionPixelSize(
            AppHolder.app.resources.getIdentifier(
                "status_bar_height",
                "dimen",
                "android"
            )
        )
    }


    fun fitStatusBar(vararg view: View) {
        view.forEach {
            val hasSet = it.getTag(R.id.tag_immersion_status_padding_top)
            if (hasSet == true) {
                return@forEach
            }
            val layoutParams = it.layoutParams
            it.setTag(R.id.tag_immersion_status_padding_top, true)
            it.setPaddingTop(it.paddingTop + statusBarHeight)
            if (isNeedReset(layoutParams)) {
                layoutParams.height = layoutParams.height + statusBarHeight
                it.layoutParams = layoutParams
            }
        }
    }

    fun fitNavigationBar(activity: Activity) {

        val navigationHeight = getNavigationHeight(activity)
        if (navigationHeight == 0) {
            return
        }

        val contentView = activity.findViewById<ViewGroup>(android.R.id.content)
        contentView.setPaddingBottom(navigationHeight)
        /* var navigationBarView: View? =
             activity.window.decorView.findViewById(R.id.id_immersion_navigation_bar)
         if (navigationBarView == null) {
             navigationBarView = View(activity)
             navigationBarView.id = R.id.id_immersion_navigation_bar
             (activity.window.decorView as? ViewGroup)?.addView(navigationBarView)
         }

         val params: FrameLayout.LayoutParams = FrameLayout.LayoutParams(
             FrameLayout.LayoutParams.MATCH_PARENT,
             navigationHeight
         )
         params.gravity = Gravity.BOTTOM
         navigationBarView.layoutParams = params*/
    }

    private fun isNeedReset(layoutParams: ViewGroup.LayoutParams): Boolean {
        if (layoutParams.height == ViewGroup.LayoutParams.MATCH_PARENT) {
            return false
        }
        if (layoutParams.height == ViewGroup.LayoutParams.WRAP_CONTENT) {
            return false
        }
        if (layoutParams is ConstraintLayout.LayoutParams) {
            if (layoutParams.height == 0) {
                return false
            }
        }
        return true

    }


    fun getNavigationHeight(activity: Activity): Int {
        val windowManager = activity.windowManager
        val d = windowManager.defaultDisplay
        val realDisplayMetrics = DisplayMetrics()
        d.getRealMetrics(realDisplayMetrics)
        val realHeight = realDisplayMetrics.heightPixels
        val displayMetrics = DisplayMetrics()
        d.getMetrics(displayMetrics)
        val displayHeight = displayMetrics.heightPixels
        return realHeight - displayHeight
    }


    fun setup(activity: Activity, light: Boolean) {
        transparentStatusBar(activity.window)
        hideToolbar(activity)
        lightStatusBar(activity.window, light)
        fitNavigationBar(activity)
        val contentView: FrameLayout = activity.window.findViewById<FrameLayout>(android.R.id.content)
        val contentViewChild = contentView.getChildAt(0)
        if(contentViewChild!=null){
            KeyboardUtils.fixAndroidBug5497(activity)
        }
    }

    fun transparentStatusBar(window: Window) {
        //window.statusBarColor = Color.parseColor("#ff0000")
        //theme.applyStyle(R.style.transparentStatusBar, true)
        WindowCompat.setDecorFitsSystemWindows(window, false)
        //window.setBackgroundDrawable(ColorDrawable(Color.WHITE))
    }

    fun hideToolbar(activity: Activity) {
        activity.theme.applyStyle(R.style.afm_ooftf_NoActionBar, true)
    }

    fun lightStatusBar(window: Window, light: Boolean) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            ViewCompat.getWindowInsetsController(window.decorView)?.isAppearanceLightStatusBars =
                light
        } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            window.statusBarColor = Color.parseColor("#32000000")
        }
    }

}