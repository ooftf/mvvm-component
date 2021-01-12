package com.ooftf.mapping.lib.ui

import android.view.View
import android.view.ViewGroup
import com.scwang.smartrefresh.layout.SmartRefreshLayout


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
        wrapper.id = view.id
        when {
            parent is SmartRefreshLayout -> {
                parent.setRefreshContent(wrapper)
            }
            parent is com.scwang.smart.refresh.layout.SmartRefreshLayout -> {
                parent.setRefreshContent(wrapper, params?.width ?: 0, params?.height ?: 0)
            }
            parent != null -> {
                val index = parent.indexOfChild(view)
                parent.removeView(view)
                parent.addView(wrapper, index)
            }
        }
        wrapper.addView(view, params)
    }
}