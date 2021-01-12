package com.ooftf.arch.frame.mvvm.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.CallSuper

/**
 *
 * @author ooftf
 * @email 994749769@qq.com
 * @date 2018/11/8 0008
 */
abstract class BaseLazyFragment : BaseFragment(), LazyFragmentProxy.LazyFragmentOwner {
    private val lazyFragmentProxy = LazyFragmentProxy<BaseLazyFragment>(this)

    final override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {
        super.onCreateView(inflater, container, savedInstanceState)
        return lazyFragmentProxy.onCreateView(inflater, container, savedInstanceState)
    }

    @CallSuper
    final override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        lazyFragmentProxy.onViewCreated(view, savedInstanceState)
    }


    /**
     * 这个时候view已经创建
     */
    abstract override fun onLoad(rootView: View)
    fun isLoaded() = lazyFragmentProxy.isLoaded
    override fun setUserVisibleHint(isVisibleToUser: Boolean) {
        super.setUserVisibleHint(isVisibleToUser)
        lazyFragmentProxy.setUserVisibleHint(isVisibleToUser)
    }

    override fun onHiddenChanged(hidden: Boolean) {
        super.onHiddenChanged(hidden)
        lazyFragmentProxy.onHiddenChanged()
    }

    override fun onDetach() {
        super.onDetach()
        lazyFragmentProxy.onDetach()
    }


    override fun onStart() {
        super.onStart()
        lazyFragmentProxy.onStart()
    }

    open fun getLayoutId(): Int {
        return 0
    }

    override fun getContentView(inflater: LayoutInflater, container: ViewGroup?): View {
        return inflater.inflate(getLayoutId(), container, false)
    }
    //abstract override fun getLayoutId(): Int

    override fun lazyEnabled(): Boolean {
        return true
    }


}