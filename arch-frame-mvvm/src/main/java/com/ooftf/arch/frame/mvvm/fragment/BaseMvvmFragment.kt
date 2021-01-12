package com.ooftf.arch.frame.mvvm.fragment

import android.view.View
import androidx.annotation.CallSuper
import androidx.databinding.ViewDataBinding
import androidx.lifecycle.ViewModelProvider
import com.ooftf.arch.frame.mvvm.BR
import com.ooftf.arch.frame.mvvm.vm.BaseViewModel
import com.ooftf.basic.utils.getGenericParamType
import com.ooftf.mapping.lib.ui.BaseLiveDataObserve
import java.lang.reflect.ParameterizedType

/**
 * 需要对继承ViewDataBinding进行keep
 * @author ooftf
 * @email 994749769@qq.com
 * @date 2019/10/9
 */
abstract class BaseMvvmFragment<B : ViewDataBinding, V : BaseViewModel> : BaseBindingFragment<B>() {
    var viewModel: V? = null
    lateinit var baseLiveDataObserve: BaseLiveDataObserve

    @CallSuper
    final override fun onLoad(rootView: View) {
        super.onLoad(rootView)
        viewModel = createViewModel()
        viewModel?.run {
            setLifecycleOwner(this@BaseMvvmFragment)
            activity?.let {
                setActivity(it)
            }
            setFragment(this@BaseMvvmFragment)
            binding.setVariable(getVariableId(), this)
            baseLiveDataObserve = baseLiveData.attach(this@BaseMvvmFragment)
            onLoad(rootView, this)
        }
    }


    abstract fun onLoad(rootView: View, viewModel: V)

    open fun createViewModel() =
            ViewModelProvider(this, getViewModelFactory()).get(getVClass())


    open fun getViewModelFactory(): ViewModelProvider.Factory {
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