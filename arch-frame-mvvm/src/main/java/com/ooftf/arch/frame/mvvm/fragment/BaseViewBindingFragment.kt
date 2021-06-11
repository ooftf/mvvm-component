package com.ooftf.arch.frame.mvvm.fragment

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import androidx.viewbinding.ViewBinding
import com.ooftf.basic.utils.getGenericParamType

/**
 *
 * @author ooftf
 * @email 994749769@qq.com
 * @date 2021/1/7
 */
abstract class BaseViewBindingFragment<B : ViewBinding> : BaseLazyFragment() {
    val binding: B by lazy {
        val bClass = getBClass()
        val method = bClass.getMethod(
            "inflate",
            LayoutInflater::class.java,
            ViewGroup::class.java,
            Boolean::class.java
        )
        method.invoke(null, layoutInflater, view, false) as B
    }

    override fun getLayoutId(): Int {
        return 0
    }

    override fun onLoad(rootView: View) {
        (rootView as ViewGroup).addView(binding.root)
    }

    /**
     * 如果报异常代表泛型设置有问题
     */
    private fun getBClass(): Class<B> {
        return this.getGenericParamType(0) as Class<B>
    }

    override fun getContentView(inflater: LayoutInflater, container: ViewGroup?): View {
        return FrameLayout(context)
    }
}