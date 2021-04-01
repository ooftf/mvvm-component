package com.ooftf.arch.frame.mvvm.activity

import android.os.Bundle
import androidx.annotation.CallSuper
import androidx.databinding.ViewDataBinding
import androidx.lifecycle.ViewModelProvider
import com.ooftf.arch.frame.mvvm.BR
import com.ooftf.arch.frame.mvvm.vm.BaseViewModel
import com.ooftf.basic.utils.getGenericParamType
import com.ooftf.mapping.lib.ui.BaseLiveDataObserve

/**
 *
 * @author ooftf
 * @email 994749769@qq.com
 * @date 2019/8/2 0002
 */
abstract class BaseMvvmActivity<B : ViewDataBinding, V : BaseViewModel> : BaseBindingActivity<B>() {
    val viewModel: V by lazy {
        val viewModelFactory = getViewModelFactory()
        if (viewModelFactory == null) {
            ViewModelProvider(this).get(getVClass())
        } else {
            ViewModelProvider(this, viewModelFactory).get(getVClass())
        }
    }
    lateinit var baseLiveDataObserve: BaseLiveDataObserve

    @CallSuper
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel.setLifecycleOwner(this)
        viewModel.setActivity(this)
        binding.setVariable(getVariableId(), viewModel)
        baseLiveDataObserve = viewModel.baseLiveData.attach(this, this)
    }

    open fun getViewModelFactory(): ViewModelProvider.Factory? {
        return defaultViewModelProviderFactory
    }


    /**
     * 如果报异常代表泛型设置有问题
     */
    private fun getVClass(): Class<V> {
        return this.getGenericParamType(1) as Class<V>
    }

    open fun getVariableId() = BR.viewModel


}