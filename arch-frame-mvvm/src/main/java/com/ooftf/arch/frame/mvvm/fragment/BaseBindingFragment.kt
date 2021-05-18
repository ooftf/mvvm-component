package com.ooftf.arch.frame.mvvm.fragment

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
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
abstract class BaseBindingFragment<B : ViewDataBinding> : BaseViewBindingFragment<B>() {
    @CallSuper
    override fun onLoad(rootView: View) {
        binding.lifecycleOwner = this
    }

}