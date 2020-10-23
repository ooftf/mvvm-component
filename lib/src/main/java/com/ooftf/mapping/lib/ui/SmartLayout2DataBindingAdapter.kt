package com.ooftf.mapping.lib.ui

import androidx.databinding.BindingAdapter
import androidx.lifecycle.Observer
import com.ooftf.mapping.lib.LogUtil.e
import com.ooftf.mapping.lib.R
import com.scwang.smart.refresh.layout.SmartRefreshLayout
import com.scwang.smart.refresh.layout.api.RefreshLayout
import com.scwang.smart.refresh.layout.constant.RefreshState

/**
 * @author ooftf
 * @email 994749769@qq.com
 * @date 2019/10/15
 */
object SmartLayout2DataBindingAdapter {
    @JvmStatic
    @BindingAdapter(value = ["refreshState"], requireAll = false)
    fun setRefreshState(smartRefreshLayout: SmartRefreshLayout, state: Int?) {
        if (state == 0) {
            if (smartRefreshLayout.state == RefreshState.Refreshing) {
                smartRefreshLayout.finishRefresh(0, true, null)
                e("finishRefresh  ok")
            } else {
                e("finishRefresh  no")
            }
        }
    }
    @JvmStatic
    @BindingAdapter(value = ["loadMoreState"], requireAll = false)
    fun setLoadMoreState(smartRefreshLayout: SmartRefreshLayout, state: Int?) {
        if (state == UIEvent.SMART_LAYOUT_LOADMORE_FINISH) {
            smartRefreshLayout.resetNoMoreData()
            smartRefreshLayout.finishLoadMore()
            e("finishLoadMore")
        } else if (state == UIEvent.SMART_LAYOUT_LOADMORE_FINISH_SUCCESS) {
            smartRefreshLayout.resetNoMoreData()
            smartRefreshLayout.finishLoadMore()
            e("finishLoadMoreSuccess")
        } else if (state == UIEvent.SMART_LAYOUT_LOADMORE_FINISH_AND_NO_MORE) {
            smartRefreshLayout.finishLoadMoreWithNoMoreData()
            e("finishLoadMoreWithNoMoreData")
        }
    }
    @JvmStatic
    @BindingAdapter(value = ["loadMoreListener"], requireAll = false)
    fun setOnLoadMoreListener(smartRefreshLayout: SmartRefreshLayout, f: Runnable?) {
        if (f == null) {
            return
        }
        e("setOnLoadMoreListener")
        smartRefreshLayout.setOnLoadMoreListener { refreshLayout: RefreshLayout? ->
            try {
                f.run()
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }
    @JvmStatic
    @BindingAdapter(value = ["refreshListener"], requireAll = false)
    fun setOnRefreshListener(smartRefreshLayout: SmartRefreshLayout, f: Runnable?) {
        if (f == null) {
            return
        }
        smartRefreshLayout.setOnRefreshListener { refreshLayout: RefreshLayout? ->
            try {
                f.run()
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }
    @JvmStatic
    @BindingAdapter(value = ["data"], requireAll = false)
    fun setUiMapping(smartRefreshLayout: SmartRefreshLayout, data: ISmartLayoutData?) {
        if (data == null) {
            return
        }
        smartRefreshLayout.setOnLoadMoreListener { refreshLayout: RefreshLayout? ->
            try {
                data.nextPage()
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
        smartRefreshLayout.setOnRefreshListener { refreshLayout: RefreshLayout? ->
            try {
                data.refresh()
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
        var observerFresh = smartRefreshLayout.getTag(R.id.observer_refresh)
        var observerMore = smartRefreshLayout.getTag(R.id.observer_more)
        if (observerFresh == null) {
            observerFresh = Observer<Int> { integer: Int ->
                setRefreshState(smartRefreshLayout, integer)
            }
            smartRefreshLayout.setTag(R.id.observer_refresh, observerFresh)
            data.getLifecycleOwner()?.let {
                data.getRefreshState().observe(it, observerFresh)
            }
        }
        if (observerMore == null) {
            observerMore = Observer<Int> { integer: Int ->
                setLoadMoreState(smartRefreshLayout, integer)
            }
            smartRefreshLayout.setTag(R.id.observer_more, observerMore)
            data.getLifecycleOwner()?.let {
                data.getLoadMoreState().observe(it, observerMore)

            }
        }
    }
}