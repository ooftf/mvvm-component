package com.ooftf.arch.frame.mvvm.vm;

import android.app.Application

/**
 *
 * @author ooftf
 * @email 994749769@qq.com
 * @date 2019/12/30
 */
abstract class BasePageViewModel<T>(application: Application) : BaseListViewModel<T>(application) {


    override fun refresh() {
        requestData(getStartPage())
    }

    override fun nextPage() {
        requestData(items.size / getPageCount() + getStartPage())
    }

    open fun getStartPage() = 0

    open fun getPageCount(): Int = 10
    open fun requestData(page: Int) {

    }

    open fun handleResponseList(page: Int, total: Int, data: List<T>) {
        if (page == getStartPage()) {
            items.clear()
        }
        items.addAll(data)
        if (items.isEmpty()) {
            baseLiveData.switchToEmpty()
        }
        if (items.size >= total) {
            baseLiveData.finishLoadMoreWithNoMoreData()
        }
    }

    open fun handleResponseList(page: Int, data: List<T>) {
        if (page == getStartPage()) {
            items.clear()
        }
        items.addAll(data)
        if (items.isEmpty()) {
            baseLiveData.switchToEmpty()
        }
        if (data.size < getPageCount()) {
            baseLiveData.finishLoadMoreWithNoMoreData()
        }
    }
}