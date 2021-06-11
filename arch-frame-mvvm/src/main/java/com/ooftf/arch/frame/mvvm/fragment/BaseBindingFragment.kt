package com.ooftf.arch.frame.mvvm.fragment

import android.view.View
import androidx.annotation.CallSuper
import androidx.databinding.ViewDataBinding

/**
 *
 * @author ooftf
 * @email 994749769@qq.com
 * @date 2021/1/7
 */
abstract class BaseBindingFragment<B : ViewDataBinding> : BaseViewBindingFragment<B>() {
    @CallSuper
    override fun onLoad(rootView: View) {
        super.onLoad(rootView)
        binding.lifecycleOwner = this
    }

}