package com.ooftf.arch.frame.mvvm.activity

import android.os.Bundle
import android.view.LayoutInflater
import androidx.annotation.CallSuper
import androidx.databinding.ViewDataBinding
import androidx.viewbinding.ViewBinding
import com.ooftf.basic.utils.getGenericParamType
import java.lang.reflect.ParameterizedType

/**
 *
 * @author ooftf
 * @email 994749769@qq.com
 * @date 2021/1/7
 */
abstract class BaseBindingActivity<B : ViewDataBinding> : BaseViewBindingActivity<B>() {

    @CallSuper
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding.lifecycleOwner = this
    }

}