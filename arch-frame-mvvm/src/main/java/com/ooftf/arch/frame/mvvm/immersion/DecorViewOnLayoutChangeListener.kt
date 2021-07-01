package com.ooftf.arch.frame.mvvm.immersion

import android.graphics.Rect
import android.util.Log
import android.view.Gravity
import android.view.View
import android.view.ViewGroup
import android.view.Window
import androidx.core.graphics.and
import com.blankj.utilcode.util.BarUtils
import com.ooftf.basic.utils.getVisibleRectOfScreen

class DecorViewOnLayoutChangeListener(val window: Window) : View.OnLayoutChangeListener {
    var current: Rect? = null
    override fun onLayoutChange(
        v: View,
        left: Int,
        top: Int,
        right: Int,
        bottom: Int,
        oldLeft: Int,
        oldTop: Int,
        oldRight: Int,
        oldBottom: Int
    ) {

        val outRect = Rect()
        window.decorView.getWindowVisibleDisplayFrame(outRect)
        if(outRect != current){
            resizeContentPadding(window, v)
        }
    }

    private fun resizeContentPadding(window: Window, view: View) {
        val contentView = window.findViewById<ViewGroup>(android.R.id.content)
        if (isSoftInputVisible(window)) {// 如果不加这个判断，在键盘显示状态下，右旋转横屏，再左旋转竖屏，就会再键盘上面显示Padding
            contentView.setPadding(
                0,
                0,
                0,
                0
            )
            return
        }
        var paddingLeft = 0
        var paddingRight = 0
        var paddingBottom = 0
        var paddingTop = 0
        var navigation =
            window.decorView.findViewById<View>(android.R.id.navigationBarBackground) ?: return
        var navigationRect =
            navigation.getVisibleRectOfScreen()
        if (navigationRect.height() == 0 || navigationRect.width() == 0) {
            return
        }
        val contentViewRect =
            contentView.getVisibleRectOfScreen()
        var and = navigationRect.and(contentViewRect)
        Log.e(
            view.javaClass.simpleName, "and::" + and.toShortString()
        )
        if (and.height() == 0 || and.width() == 0) {
            return
        }

        when (getNavigationGravity(contentViewRect, navigationRect)) {
            Gravity.RIGHT -> {
                paddingLeft = 0
                paddingTop = 0
                paddingRight = and.width()
                paddingBottom = 0
            }
            Gravity.LEFT -> {
                paddingLeft = and.width()
                paddingTop = 0
                paddingRight = 0
                paddingBottom = 0
            }
            Gravity.BOTTOM -> {
                paddingLeft = 0
                paddingTop = 0
                paddingRight = 0
                paddingBottom = and.height()
            }
        }
        contentView.setPadding(
            paddingLeft,
            paddingTop,
            paddingRight,
            paddingBottom
        )
    }

    fun getNavigationGravity(content: Rect, navigation: Rect): Int {
        when {
            navigation.centerX() > content.centerX() -> {
                return Gravity.RIGHT
            }
            navigation.centerX() < content.centerX() -> {
                return Gravity.LEFT
            }
            navigation.centerY() > content.centerY() -> {
                return Gravity.BOTTOM
            }
            navigation.centerY() < content.centerY() -> {
                return Gravity.TOP
            }
            else -> {
                return Gravity.CENTER
            }
        }
    }
    fun isSoftInputVisible(window: Window): Boolean {
        return getDecorViewInvisibleHeight(window) > 0
    }

    private var sDecorViewDelta = 0
    private fun getDecorViewInvisibleHeight(window: Window): Int {
        val decorView = window.decorView
        val outRect = Rect()
        decorView.getWindowVisibleDisplayFrame(outRect)
        val delta = Math.abs(decorView.bottom - outRect.bottom)
        if (delta <= BarUtils.getNavBarHeight() + BarUtils.getStatusBarHeight()) {
            sDecorViewDelta = delta
            return 0
        }
        return delta - sDecorViewDelta
    }
}