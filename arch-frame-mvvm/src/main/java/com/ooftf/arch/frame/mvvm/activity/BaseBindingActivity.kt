package com.ooftf.arch.frame.mvvm.activity

import android.os.Bundle
import android.view.LayoutInflater
import androidx.annotation.CallSuper
import androidx.databinding.ViewDataBinding
import com.ooftf.basic.utils.getGenericParamType
import java.lang.reflect.ParameterizedType

/**
 *
 * @author ooftf
 * @email 994749769@qq.com
 * @date 2021/1/7
 */
open class BaseBindingActivity<B : ViewDataBinding> : BaseActivity() {
    lateinit var binding: B

    @CallSuper
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        bindLayout()
        binding.lifecycleOwner = this
    }

    /**
     * 如果报异常代表泛型设置有问题
     */
    private fun getBClass(): Class<B> {
        return this.getGenericParamType(0) as Class<B>
    }

    /**
     * 设置layout，生成binding
     */
    private fun bindLayout() {
        val bClass = getBClass()
        val method = bClass.getMethod("inflate", LayoutInflater::class.java)
        binding = method.invoke(null, layoutInflater) as B
        setContentView(binding.root)
    }
}