package com.ooftf.mapping.lib.ui

import android.view.View
import android.view.ViewGroup


/**
 * @author ooftf
 * @email 994749769@qq.com
 * @date 2020/10/9
 */
object ViewHelper {
    fun replace(view: View, wrapper: ViewGroup, params: ViewGroup.LayoutParams?) {
        val lp = view.layoutParams
        if (lp != null) {
            wrapper.layoutParams = lp
        }
        val parent = view.parent as ViewGroup?
        if (parent != null) {
            val index = parent.indexOfChild(view)
            parent.removeView(view)
            wrapper.id = view.id
            parent.addView(wrapper, index)
        }
        wrapper.addView(view, params)
    }
}