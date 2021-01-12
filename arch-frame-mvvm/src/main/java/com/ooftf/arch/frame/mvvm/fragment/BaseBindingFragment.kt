package com.ooftf.arch.frame.mvvm.fragment

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.ViewDataBinding
import com.ooftf.basic.utils.getGenericParamType
import java.lang.reflect.ParameterizedType

/**
 *
 * @author ooftf
 * @email 994749769@qq.com
 * @date 2021/1/7
 */
open class BaseBindingFragment<B : ViewDataBinding> : BaseLazyFragment() {
    lateinit var binding: B
    override fun onLoad(rootView: View) {
        binding.lifecycleOwner = this
    }

    override fun getLayoutId(): Int {
        return 0
    }

    /**
     * 如果报异常代表泛型设置有问题
     */
    private fun getBClass(): Class<B> {
        return this.getGenericParamType(0) as Class<B>
    }

    override fun getContentView(inflater: LayoutInflater, container: ViewGroup?): View {
        val bClass = getBClass()
        val method = bClass.getMethod(
                "inflate",
                LayoutInflater::class.java,
                ViewGroup::class.java,
                Boolean::class.java
        )
        binding = method.invoke(null, inflater, container, false) as B
        return binding.root
    }
}