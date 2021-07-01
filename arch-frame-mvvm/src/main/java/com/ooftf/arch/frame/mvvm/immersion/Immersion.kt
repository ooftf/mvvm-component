package com.ooftf.arch.frame.mvvm.immersion

import android.app.Activity
import android.graphics.Color
import android.os.Build
import android.view.*
import android.widget.FrameLayout
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.graphics.and
import androidx.core.view.WindowCompat
import com.blankj.utilcode.util.BarUtils
import com.blankj.utilcode.util.KeyboardUtils
import com.ooftf.arch.frame.mvvm.R
import com.ooftf.basic.AppHolder
import com.ooftf.basic.utils.getVisibleRectOfScreen
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
    val wm by lazy {
        AppHolder.app.getSystemService(AppCompatActivity.WINDOW_SERVICE) as WindowManager
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
        if (!BarUtils.isSupportNavBar()) {
            return
        }
        val navigationHeight = BarUtils.getNavBarHeight()
        val contentView = activity.findViewById<ViewGroup>(android.R.id.content)
        var tag = activity.window.decorView.getTag(R.id.tag_content_layout_listener)
        if (tag == null) {
            tag =
                object : View.OnLayoutChangeListener {
                    var mLeft = 0
                    var mTop = 0
                    var mRight = 0
                    var mBottom = 0
                    override fun onLayoutChange(
                        v: View?,
                        left: Int,
                        top: Int,
                        right: Int,
                        bottom: Int,
                        oldLeft: Int,
                        oldTop: Int,
                        oldRight: Int,
                        oldBottom: Int
                    ) {

                        var paddingLeft = 0
                        var paddingRight = 0
                        var paddingBottom = 0
                        var paddingTop = 0
                        var navigationRect =
                            activity.window.decorView.findViewById<View>(android.R.id.navigationBarBackground)
                                ?.getVisibleRectOfScreen() ?: return

                        if (navigationRect.height() == 0) {
                            return
                        }
                        val decorViewRect =
                            activity.window.decorView.getVisibleRectOfScreen()
                        var and = navigationRect.and(decorViewRect)
                        if(and.height() == 0||and.width()==0){
                            return
                        }
                        navigationRect.and(decorViewRect)
                        if (navigationRect.left == decorViewRect.left
                            && navigationRect.right == decorViewRect.right
                            && navigationRect.bottom == decorViewRect.bottom
                            && navigationRect.top > decorViewRect.top
                        ) {//位于下方
                            paddingLeft = 0
                            paddingTop = 0
                            paddingRight = 0
                            paddingBottom = and.height()
                        } else if (navigationRect.left > decorViewRect.left
                            && navigationRect.right == decorViewRect.right
                            && navigationRect.bottom == decorViewRect.bottom
                            && navigationRect.top == decorViewRect.top
                        ) {
                            //位于右侧
                            paddingLeft = 0
                            paddingTop = 0
                            paddingRight = and.width()
                            paddingBottom = 0

                        } else if (navigationRect.left == decorViewRect.left
                            && navigationRect.right < decorViewRect.right
                            && navigationRect.bottom == decorViewRect.bottom
                            && navigationRect.top == decorViewRect.top
                        ) {
                            // 位于左侧
                            paddingLeft = and.width()
                            paddingTop = 0
                            paddingRight = 0
                            paddingBottom = 0
                        }
                        if (paddingLeft != contentView.paddingLeft
                            || paddingTop != contentView.paddingTop
                            || paddingRight != contentView.paddingRight
                            || paddingBottom != contentView.paddingBottom
                        ) {
                            contentView.setPadding(paddingLeft, paddingTop, paddingRight, paddingBottom)
                        }

                        /* val display = wm.defaultDisplay
                         val isPortrait = ScreenUtils.isPortrait()
                         if (mIsPortrait != isPortrait || rotation != display.rotation) {
                             mIsPortrait = isPortrait
                             rotation = display.rotation
                             if (isPortrait) {
                                 contentView.setPaddingRelative(0, 0, 0, navigationHeight)
                                 contentView.requestLayout()
                             } else {
                                 if (display.rotation == 1) {
                                     contentView.setPaddingRelative(0, 0, navigationHeight, 0)
                                     contentView.requestLayout()
                                 } else {
                                     contentView.setPaddingRelative(navigationHeight, 0, 0, 0)
                                     contentView.requestLayout()
                                 }

                             }
                         }*/


                    }

                }
            activity.window.decorView.addOnLayoutChangeListener(tag)
            activity.window.decorView.setTag(R.id.tag_content_layout_listener, tag)
        }
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


    fun setupPreOnCreate(activity: Activity) {
        hideToolbar(activity)
    }

    fun setupAfterOnCreate(activity: Activity, light: Boolean) {
        transparentStatusBar(activity.window)
        lightStatusBar(activity.window, light)
        lightNavigationBar(activity.window, light)
        fitNavigationBar(activity)
        val contentView: FrameLayout =
            activity.window.findViewById(android.R.id.content)
        val contentViewChild = contentView.getChildAt(0)
        if (contentViewChild != null) {
            KeyboardUtils.fixAndroidBug5497(activity)
        }
    }

    fun transparentStatusBar(window: Window) {
        window.statusBarColor = Color.TRANSPARENT
        WindowCompat.setDecorFitsSystemWindows(window, false)
    }

    fun hideToolbar(activity: Activity) {
        activity.theme.applyStyle(R.style.afm_ooftf_NoActionBar, true)
    }

    fun lightStatusBar(window: Window, light: Boolean) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            BarUtils.setStatusBarLightMode(window, light)
        } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            window.statusBarColor = Color.parseColor("#32000000")
        }
    }

    fun lightNavigationBar(window: Window, light: Boolean) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            BarUtils.setNavBarLightMode(window, light)
        } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            window.navigationBarColor = Color.parseColor("#32000000")
        }
    }


}